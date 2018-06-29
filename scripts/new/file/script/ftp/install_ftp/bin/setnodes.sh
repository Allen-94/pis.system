#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/changeRabbitmq_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
MODE="$@"
OPTION_NU="$#"
CONFIG_FILE="${RUN_PATH}/../config/config.conf"
NODES_CONF="${RUN_PATH}/../config/nodes.conf"
FILE_PATH="${RUN_PATH}/../file"
CLUSTER_STATUS="yes"


function setHosts()
{
    grep "_host" ${CONFIG_FILE} |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")' >> /etc/hosts
    bash ${RUN_PATH}/makeSshKey.sh ${ROOT_PWD} , ${HOST}
    [ $? -ne 0 ] && return 1
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        scp /etc/hosts ${i}:/etc/ >/dev/null 2>&1
    done
    return 0
}

function getConfig()
{
    USER_NAME=$(getValue "ftp_user_name")
    USER_PWD=$(getValue "ftp_user_password")
    ROOT_PWD=$(getValue "root_password")
    OLD_HOST=$(getValue "other_host" |awk '{print $2}')
    LOCAL_NAME=$(getValue "local_host" |awk '{print $2}')
    CONFIG_FILE=${NODES_CONF}
    ADD_HOST=$(getValue "nodes_host" |awk '{print $2}')
    ALL_HOST="${OLD_HOST} ${ADD_HOST}"
    if [ "X${MODE}" == "X-add" ];then
        setAllHosts
        [ $? -ne 0 ] && return 1
    fi
    let nu=0
    local hosts=(${ADD_HOST})
    getClusterStatus
    
    HDFS_IP=$(getValue "hdfs_ip")
    HDFS_ROOT_PWD=$(getValue "hdfs_root_pwd")
    return 0
}

function installFtp()
{
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r ${FILE_PATH}/vsftpd-*.rpm ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} "rpm -ivh /tmp/vsftpd-*.rpm"
            ssh ${i} "rm -rf /tmp/vsftpd-*.rpm"
            ssh ${i} "useradd -d /home/ftp -s /sbin/nologin ${USER_NAME}"
            ssh ${i} "echo ${USER_PWD}|passwd --stdin ${USER_NAME}"
            
            scp -r /etc/vsftpd/vsftpd.conf ${i}:/etc/vsftpd/ >/dev/null 2>&1
            scp -r /etc/vsftpd/vuser_passwd.db ${i}:/etc/vsftpd/ >/dev/null 2>&1
            ssh ${i} "mkdir -p /etc/vsftpd/vuser_conf/"
            scp -r /etc/vsftpd/vuser_conf/${USER_NAME} ${i}:/etc/vsftpd/vuser_conf/ >/dev/null 2>&1
            ssh ${i} "echo ${USER_NAME} >> /etc/vsftpd/chroot_list"
            
            scp -r ${RUN_PATH}/installNfsClient.sh ${i}:/tmp/ >/dev/null 2>&1
            scp -r ${FILE_PATH}/keyutils-*.rpm ${i}:/tmp/ >/dev/null 2>&1
            scp -r ${FILE_PATH}/lib*.rpm ${i}:/tmp/ >/dev/null 2>&1
            scp -r ${FILE_PATH}/python-argparse-*.rpm ${i}:/tmp/ >/dev/null 2>&1
            scp -r ${FILE_PATH}/rpcbind-*.rpm ${i}:/tmp/ >/dev/null 2>&1
            scp -r ${FILE_PATH}/nfs-utils-*.rpm ${i}:/tmp/ >/dev/null 2>&1

            ssh ${i} "bash /tmp/installNfsClient.sh /tmp"
            ssh ${i} "rm -rf /tmp/installNfsClient.sh"
            ssh ${i} "rm -rf /tmp/*.rpm"
            
            ssh ${i} "chkconfig vsftpd on"
            ssh ${i} "service vsftpd start"
            
            ssh ${i} 'echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf'
        fi
    done
    return 0
}

function setNfs()
{
    local sendPwd="${RUN_PATH}/sendPwd.exp"
    local runPwd="${RUN_PATH}/runPwd.exp"
    makeExp ${sendPwd} ${runPwd}
    ${runPwd} ${HDFS_ROOT_PWD} ssh ${HDFS_IP} hostname
    local all_ip=$(getAllHostIp ${runPwd} ${HDFS_ROOT_PWD} ${HDFS_IP})
    all_ip=(${all_ip})

    let nu=0
    hosts=(${ALL_HOST})
    for i in ${hosts[@]}; do
        [ ${nu} -eq ${#all_ip[@]}] && let nu=0
        ssh ${i} 'umount /var/ftp'
        ssh ${i} "mount -t nfs -o vers=3,proto=tcp,nolock,noacl,sync ${all_ip[${nu}]}:/ /var/ftp/"
        let nu=nu+1
    done

    rm -rf ${sendPwd} ${runPwd}
    return 0
}

function removeCluster()
{
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xyes" ];then
            ssh ${i} "rpm -e vstpd"
            [ $? -ne 0 ] && return 1
            ssh ${i} 'echo "in_the_cluster=no" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="no"
        fi
    done
    return 0
}


function main()
{
    getConfig
    [ $? -ne 0 ] && return 1
    if [ ${OPTION_NU} -eq 1 ];then
        if [ "X${MODE}" == "X-add" ];then
            installFtp
            [ $? -ne 0 ] && return 1
            setNfs
            [ $? -ne 0 ] && return 1
        elif [ "X${MODE}" == "X-del" ];then
            removeCluster
            [ $? -ne 0 ] && return 1
        elif [ "X${MODE}" == "X--help" ];then
            echo "Usage: setnodes [OPTION]
Shell options:
       -add
       -del"
        else
            echo "setnodes: unrecognized option '${MODE}'
Usage: setnodes [OPTION]
Try \`setnodes --help' for more information."
            return 2
        fi
    elif [ ${OPTION_NU} -eq 0 ];then
        echo "Usage: setnodes [OPTION]
Try \`setnodes --help' for more information."
        return 2
    else
        echo "setnodes: unrecognized option '${MODE}'
Usage: setnodes [OPTION]
Try \`setnodes --help' for more information."
        return 2
    fi
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?

