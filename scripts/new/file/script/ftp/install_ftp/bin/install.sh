#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/installFtp_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
CONFIG_FILE="${RUN_PATH}/../config/config.conf"
FILE_PATH="$(cd ${RUN_PATH}/../file;pwd)"
CONF_DIR="$(cd ${RUN_PATH}/../config;pwd)"
CLUSTER_STATUS="none"

function getConfig()
{
    USER_NAME=$(getValue "ftp_user_name")
    USER_PWD=$(getValue "ftp_user_password")
    ROOT_PWD=$(getValue "root_password")
    HOST=$(getValue "other_host" |awk '{print $2}')
    HDFS_IP=$(getValue "hdfs_ip")
    HDFS_ROOT_PWD=$(getValue "hdfs_root_pwd")
    HOST_IP=$(grep "_host" ${CONFIG_FILE} |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")'|awk '{print $1}')
    HOST_IP=(${HOST_IP})
    ls /opt/cluster_status.conf >/dev/null 2>&1
    if [ $? -eq 0 ];then
        CLUSTER_STATUS=$(grep "^in_the_cluster" /opt/cluster_status.conf |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")')
    fi
    setHosts
    [ $? -ne 0 ] && return 1
    let nu=0
    local hosts=(${HOST})
    getClusterStatus
    return 0
}

function installFtp()
{
    mv -f ${RUN_PATH}/../config/hostList /opt/
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        {
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r /opt/hostList ${i}:/opt
            scp -r ${FILE_PATH}/vsftpd-*.rpm ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} "rpm -ivh /tmp/vsftpd-*.rpm"
            ssh ${i} "rm -rf /tmp/vsftpd-*.rpm"
            ssh ${i} "useradd -d /home/ftp -s /sbin/nologin ${USER_NAME}"
            ssh ${i} "echo ${USER_PWD}|passwd --stdin ${USER_NAME}"
        fi
        } &
    done
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        rpm -ivh ${FILE_PATH}/vsftpd-*.rpm
        useradd -d /home/ftp -s /sbin/nologin ${USER_NAME}
        echo "${USER_PWD}"|passwd --stdin ${USER_NAME}
    fi
    wait
    mv -f ${RUN_PATH}/../config/hostList /opt/
    for i in ${hosts[@]}; do
        scp -r /opt/hostList ${i}:/opt
    done
    return 0
}

function setFtp()
{
    sed -i "s|<FTP_USER>|${USER_NAME}|g" "${CONF_DIR}/vsftpd.conf"
    [ ! -f /etc/vsftpd/vuser_passwd.txt ] && touch /etc/vsftpd/vuser_passwd.txt
    echo "${USER_NAME}
${USER_PWD}" > ${RUN_PATH}/vuser_passwd.txt
    db_load -T -t hash -f ${RUN_PATH}/vuser_passwd.txt ${RUN_PATH}/vuser_passwd.db
    mkdir -p /etc/vsftpd/vuser_conf/
    echo "local_root=/var/ftp/
write_enable=YES
anon_umask=022
anon_world_readable_only=NO
anon_upload_enable=YES
anon_mkdir_write_enable=YES
anon_other_write_enable=YES" >> ${CONF_DIR}/${USER_NAME}
    echo "auth required pam_userdb.so db=/etc/vsftpd/vuser_passwd
account required pam_userdb.so db=/etc/vsftpd/vuser_passwd" > ${RUN_PATH}/vsftpd

    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        {
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r ${CONF_DIR}/vsftpd.conf ${i}:/etc/vsftpd/ >/dev/null 2>&1
            scp -r ${RUN_PATH}/vuser_passwd.db ${i}:/etc/vsftpd/ >/dev/null 2>&1
            ssh ${i} "mkdir -p /etc/vsftpd/vuser_conf/"
            scp -r ${CONF_DIR}/${USER_NAME} ${i}:/etc/vsftpd/vuser_conf/ >/dev/null 2>&1
            ssh ${i} "echo ${USER_NAME} >> /etc/vsftpd/chroot_list"
            ssh ${i} 'echo "in_the_cluster=no" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="no"
        fi
        } &
    done
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        yes|cp ${CONF_DIR}/vsftpd.conf /etc/vsftpd/
        yes|cp ${CONF_DIR}/${USER_NAME} /etc/vsftpd/vuser_conf/
        yes|cp ${RUN_PATH}/vuser_passwd.db /etc/vsftpd/
        yes|cp ${RUN_PATH}/vsftpd /etc/vsftpd//etc/pam.d/
        echo "${USER_NAME}" >> /etc/vsftpd/chroot_list
        echo "in_the_cluster=no" >/dev/null 2>&1 > /opt/cluster_status.conf
        CLUSTER_STATUS[0]="no"
    fi
    wait
    rm -rf ${CONF_DIR}/${USER_NAME} ${RUN_PATH}/vuser_passwd.txt ${RUN_PATH}/vuser_passwd.db ${RUN_PATH}/vsftpd
    return 0
}

function startFtp()
{
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
    {
        ssh ${i} "chkconfig vsftpd on"
        ssh ${i} "service vsftpd start"
     } &
    done
    wait
    chkconfig vsftpd on
    service vsftpd start
    return 0
}

function installNfsClient()
{
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
    {
        scp -r ${RUN_PATH}/installNfsClient.sh ${i}:/tmp/ >/dev/null 2>&1
        scp -r ${FILE_PATH}/keyutils-*.rpm ${i}:/tmp/ >/dev/null 2>&1
        scp -r ${FILE_PATH}/lib*.rpm ${i}:/tmp/ >/dev/null 2>&1
        scp -r ${FILE_PATH}/python-argparse-*.rpm ${i}:/tmp/ >/dev/null 2>&1
        scp -r ${FILE_PATH}/rpcbind-*.rpm ${i}:/tmp/ >/dev/null 2>&1
        scp -r ${FILE_PATH}/nfs-utils-*.rpm ${i}:/tmp/ >/dev/null 2>&1

        ssh ${i} "bash /tmp/installNfsClient.sh /tmp"
        ssh ${i} "rm -rf /tmp/installNfsClient.sh"
        ssh ${i} "rm -rf /tmp/*.rpm"
     } &
    done
    wait
    bash ${RUN_PATH}/installNfsClient.sh ${FILE_PATH}
    return 0
}

function setHdfs()
{
    local sendPwd="${RUN_PATH}/sendPwd.exp"
    local runPwd="${RUN_PATH}/runPwd.exp"
    makeExp ${sendPwd} ${runPwd}
    ${runPwd} ${HDFS_ROOT_PWD} ssh ${HDFS_IP} hostname
    local all_ip=$(getAllHostIp ${runPwd} ${HDFS_ROOT_PWD} ${HDFS_IP})
    all_ip=(${all_ip})
    
    mount -t nfs -o vers=3,proto=tcp,nolock,noacl,sync ${all_ip[0]}:/ /var/ftp/
    if [ $? -ne 0 ];then
        mount -t nfs -o vers=3,proto=tcp,nolock,noacl,sync ${all_ip[1]}:/ /var/ftp/
    fi
        
    echo "* * * * * bash -x /opt/service/autoMount.sh
* * * * * sleep 15; bash -x /opt/service/autoMount.sh
* * * * * sleep 30; bash -x /opt/service/autoMount.sh
* * * * * sleep 45; bash -x /opt/service/autoMount.sh">>/var/spool/cron/root
    mkdir -p /opt/service
    mkdir -p ${RUN_PATH}/temp
    yes|cp ${RUN_PATH}/autoMount.sh ${RUN_PATH}/temp/
    
    sed -i "s|<IP1>|${all_ip[0]}|g" "${RUN_PATH}/temp/autoMount.sh"
    sed -i "s|<IP2>|${all_ip[1]}|g" "${RUN_PATH}/temp/autoMount.sh"
    
    yes|cp ${RUN_PATH}/temp/autoMount.sh /opt/service/
    
    ##let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        ##let nu=nu+1
        ##[ ${#all_ip[@]} -eq ${nu} ] && nu=0
        ssh ${i} "mount -t nfs -o vers=3,proto=tcp,nolock,noacl,sync ${all_ip[0]}:/ /var/ftp/"
        if [ $? -ne 0 ];then
            ssh ${i} "mount -t nfs -o vers=3,proto=tcp,nolock,noacl,sync ${all_ip[1]}:/ /var/ftp/"
        fi
        ssh ${i} 'echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf'
        ssh ${i} "mkdir -p /opt/service"
        scp -r /var/spool/cron/root ${i}:/var/spool/cron/
        ##cmd="mount -t nfs -o vers=3,proto=tcp,nolock,noacl,sync ${all_ip[${nu}]}:/ /var/ftp/"
        ##row=$(grep -n "mount -t" ${RUN_PATH}/temp/autoMount.sh |awk -F ':' '{print $1}')
        ##sed -i "${row}d" ${RUN_PATH}/temp/autoMount.sh
        ##let row=row-1
        ##sed -i "${row}a \ \ \ \ ${cmd}" ${RUN_PATH}/temp/autoMount.sh
        scp -r ${RUN_PATH}/temp/autoMount.sh ${i}:/opt/service/
    done
    echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf
    rm -rf ${sendPwd} ${runPwd} ${RUN_PATH}/temp
}


function typeFlag()
{
    local server_ip=$(getValue "server_ip")
    sed -i "s|<SERVER_IP>|${server_ip}|g" "${RUN_PATH}/status.sh"
    sed -i "s|<HDFS_IP>|${HDFS_IP}|g" "${RUN_PATH}/mount.sh"
    sed -i "s|<HDFS_ROOT_PWD>|${HDFS_ROOT_PWD}|g" "${RUN_PATH}/mount.sh"
    yes|cp ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${RUN_PATH}/mount.sh /opt/service/
    
    echo "Master" > /opt/typeFlag
    ssh ${HOST_IP[1]} "echo 'Slave' > /opt/typeFlag"
    scp -r ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${RUN_PATH}/mount.sh ${HOST_IP[1]}:/opt/service/

    for((i=2;i<${#HOST_IP[@]};i++));do
        ssh ${HOST_IP[${i}]} "echo 'Slave' > /opt/typeFlag"
        scp -r ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${RUN_PATH}/mount.sh ${HOST_IP[${i}]}:/opt/service/
    done
    return 0
}


function main()
{
    getConfig
    [ $? -ne 0 ] && return 1
    installFtp
    [ $? -ne 0 ] && return 1
    setFtp
    [ $? -ne 0 ] && return 1
    startFtp
    [ $? -ne 0 ] && return 1
    installNfsClient
    [ $? -ne 0 ] && return 1
    setHdfs
    [ $? -ne 0 ] && return 1
    typeFlag
    [ $? -ne 0 ] && return 1
    setServerIp ${HOST_IP[1]}
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?

