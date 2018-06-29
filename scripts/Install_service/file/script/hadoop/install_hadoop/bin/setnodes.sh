#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/changeHadoop_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
CONFIG_OLD="${RUN_PATH}/../config/config.conf"
CONFIG_ADD="${RUN_PATH}/../config/nodes.conf"
MODE="$@"
OPTION_NU="$#"
CLUSTER_STATUS="yes"

function getConfig()
{
    CONFIG_FILE=${CONFIG_OLD}
    USER_PWD=$(getValue "hd_user_password")
    ROOT_PWD=$(getValue "root_password")
    OLD_HOST=$(getValue "data_host" |awk '{print $2}')
    CONFIG_FILE=${CONFIG_ADD}
    ADD_HOST=$(getValue "nodes_host" |awk '{print $2}')
    ALL_HOST="${OLD_HOST} ${ADD_HOST}"
    if [ "X${MODE}" == "X-add" ];then
        setAllHosts
        [ $? -ne 0 ] && return 1
    fi
    let nu=0
    local hosts=(${ADD_HOST})
    getClusterStatus
    return 0
}

function makeHadoopUser()
{
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp ${RUN_PATH}/addUser.sh ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} bash /tmp/addUser.sh ${USER_PWD} hadoop 530
            ssh ${i} rm -rf /tmp/addUser.sh
        fi
    done
    return 0
}

function installJdk()
{
    let nu=0
    local file_path="${RUN_PATH}/../file"
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp ${RUN_PATH}/installJdk.sh ${file_path}/jdk*.tar.gz ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} bash /tmp/installJdk.sh /tmp
            ssh ${i} rm -rf ${RUN_PATH}/installJdk.sh /tmp/jdk*.tar.gz
        fi
    done
    return 0
}

function makeHadoopSshKey()
{
    cp ${RUN_PATH}/makeSshKey.sh /home/hadoop/
    chown hadoop:hadoop /home/hadoop/makeSshKey.sh
    local hosts=(${OLD_HOST})
    for i in ${hosts[@]}; do
        ssh ${i} 'su - hadoop -c "rm -rf ~/.ssh"'
    done
    su - hadoop -c "rm -rf ~/.ssh"
    hosts=(${ALL_HOST})
    hosts=${hosts[@]}
    su - hadoop -c "bash /home/hadoop/makeSshKey.sh ${USER_PWD} , ${hosts}"
    [ $? -ne 0 ] && rm -rf /home/hadoop/makeSshKey.sh && return 1
    rm -rf /home/hadoop/makeSshKey.sh
    return 0
}

function upgradeGlibc()
{
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            ssh ${i} mkdir -p /tmp/glibc/
            scp -r ${RUN_PATH}/../file/glibc*.tar.gz ${i}:/tmp/glibc/ >/dev/null 2>&1
            scp -r upgradeGlibc.sh ${i}:/tmp/glibc/ >/dev/null 2>&1
            ssh ${i} bash /tmp/glibc/upgradeGlibc.sh >/dev/null 2>&1
            ssh ${i} rm -rf /tmp/glibc/
        fi
    done
    return 0
}

function installHd()
{
    let nu=0
    local hosts=(${ADD_HOST})
    grep "^data_host" ${CONFIG_OLD} |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")' |awk '{print $1}' > /usr/hadoop/etc/hadoop/slaves
    grep "^nodes_host" ${CONFIG_FILE} |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")' |awk '{print $1}' >> /usr/hadoop/etc/hadoop/slaves
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r /usr/hadoop ${i}:/usr/ >/dev/null 2>&1
            scp -r /etc/profile ${i}:/etc/ >/dev/null 2>&1
            ssh ${i} source /etc/profile
            ssh ${i} chown -R hadoop:hadoop /usr/hadoop/
            ssh ${i} 'echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="yes"
        elif [ "X${CLUSTER_STATUS[${nu}]}" == "Xno" ];then
            sed -i "s|^${i}||g" /usr/hadoop/etc/hadoop/exclude
            ssh ${i} 'echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="yes"
        fi
    done
    return 0
}

function startNewHd()
{
    local hosts=(${ADD_HOST})
    local status=$(jps |wc -l)
    if [ ${status} -lt 4 ];then
        su - hadoop -c "/usr/hadoop/sbin/start-all.sh"
        su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh start namenode"
        su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh start secondarynamenode"
        hosts=(${ALL_HOST})
    fi
    for i in ${hosts[@]}; do
        ssh ${i} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh start datanode"'
        ssh ${i} 'su - hadoop -c "/usr/hadoop/sbin/yarn-daemon.sh start nodemanager"'
        ssh ${i} 'su - hadoop -c "/usr/hadoop/sbin/start-balancer.sh"'
    done
    su - hadoop -c "/usr/hadoop/bin/hadoop dfsadmin -refreshNodes"
    su - hadoop -c "/usr/hadoop/sbin/start-balancer.sh"
    return 0
}

function removeCluster()
{
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xyes" ];then
            echo "${i}" >> /usr/hadoop/etc/hadoop/exclude
            su - hadoop -c "/usr/hadoop/bin/hadoop dfsadmin -refreshNodes"
            su - hadoop -c "/usr/hadoop/sbin/start-balancer.sh"
            ssh ${i} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh stop datanode"'
            ssh ${i} 'su - hadoop -c "/usr/hadoop/sbin/yarn-daemon.sh stop nodemanager"'
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
            makeHadoopUser
            [ $? -ne 0 ] && return 1
            upgradeGlibc
            [ $? -ne 0 ] && return 1
            installJdk
            [ $? -ne 0 ] && return 1
            makeHadoopSshKey
            [ $? -ne 0 ] && return 1
            installHd
            [ $? -ne 0 ] && return 1
            startNewHd
            [ $? -ne 0 ] && return 1
        elif [ "X${MODE}" == "X-del" ];then
            removeCluster
            [ $? -ne 0 ] && return 1
        elif [ "X${MODE}" == "X-clear" ];then
            clearCluster
            [ $? -ne 0 ] && return 1
        elif [ "X${MODE}" == "X--help" ];then
            echo "Usage: setnodes [OPTION]
Shell options:
       -add
       -del
       -clear"
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

