#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
CONF_DIR="${RUN_PATH}/../config"
FILE_DIR="${RUN_PATH}/../file"
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/sys_install_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
CORE_CONFIG="${CONF_DIR}/Core_service_password.csv"
SENDPWD="${RUN_PATH}/sendPwd.exp"
RUNPWD="${RUN_PATH}/runPwd.exp"
ALL_HOSTS=""

function getService()
{
    SERVICE_LIST=$(grep -v "^#" ${CONF_DIR}/conf.conf |grep "^cluster_pkg"|awk -F '=' '{print $2}')
    SERVICE_LIST=(${SERVICE_LIST})
    local pwd=$(grep -v "^#" ${CONF_DIR}/conf.conf |grep "^local_pwd"|awk -F '=' '{print $2}')
    local ip=$(ifconfig eth0|grep "inet addr"|awk -F ":" '{print $2}'|awk '{print $1}')
    local host_name=$(grep -v "^#" ${CONF_DIR}/conf.conf |grep "^host_name"|awk -F '=' '{print $2}')
    local host_ip=$(grep -v "^#" ${CONF_DIR}/conf.conf |grep "^host_ip"|awk -F '=' '{print $2}')
    #192.168.2.17,vrm01,192.168.0.100
    echo "${ip},${host_name},${host_ip}" >/opt/hostList
    echo "SystemManagement,,${ip},0,${pwd},,,SystemManagement" > ${CORE_CONFIG}
    return 0
}

function builtPkg()
{
    for i in ${SERVICE_LIST[@]}; do
        local cluster=$(echo ${i}|awk -F ':' '{print $1}')
        local pkg=$(echo ${i}|awk -F ':' '{print $2}')
        local pkg_dir="${FILE_DIR}/script/${pkg}"
        mkdir -p ${pkg_dir}/${cluster}_pkg
        bash ${pkg_dir}/packaging.sh ${cluster} ${pkg} ${pkg_dir}/${cluster}_pkg
    done
    return 0
}

function tempKey()
{
    local user=$1
    local pwd=$2
    if [ ! -f ~/.ssh/authorized_keys ];then
        yes|ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
        cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
    fi
    ${RUNPWD} ${pwd} ssh ${user} "yes|ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa"
    [ $? -ne 0 ] && return 1
    ${SENDPWD} ${pwd} ${user}:~/.ssh/id_dsa.pub ~/.ssh/id_dsa.pub.bak
    [ $? -ne 0 ] && return 1
    cat ~/.ssh/id_dsa.pub.bak >> ~/.ssh/authorized_keys
    [ $? -ne 0 ] && return 1
    rm -rf ~/.ssh/id_dsa.pub.bak
    chmod 600 ~/.ssh/authorized_keys
    ${SENDPWD} ${pwd} ~/.ssh/authorized_keys ${user}:~/.ssh/
    return 0
}

function sendCore()
{
    local ip=""
    local pwd=""
    local hosts=(${ALL_HOSTS})
    for i in ${hosts[@]}; do
        ip=$(echo ${i}|awk -F "," '{print $1}')
        pwd=$(echo ${i}|awk -F "," '{print $2}')
        ${RUNPWD} ${pwd} ssh ${ip} "hostname"
        ${SENDPWD} ${pwd} ${CORE_CONFIG} ${ip}:/opt/ >/dev/null 2>&1
        ${SENDPWD} ${pwd} ${RUN_PATH}/sendCore.sh ${ip}:/opt/ >/dev/null 2>&1
        ${RUNPWD} ${pwd} ssh ${ip} "bash /opt/sendCore.sh /opt/Core_service_password.csv /opt/"
        ${RUNPWD} ${pwd} ssh ${ip} "bash /opt/sendCore.sh /opt/sendCore.sh /opt/"
    done
    return 0
}

function install()
{
    for i in ${SERVICE_LIST[@]}; do
        local cluster=$(echo ${i}|awk -F ':' '{print $1}')
        local pkg=$(echo ${i}|awk -F ':' '{print $2}')
        local pkg_dir="${FILE_DIR}/script/${pkg}"
        local ip=$(grep "^${cluster}" ${CONF_DIR}/Install_service.csv|grep "Master"|awk -F "," '{print $4}'|awk 'gsub(/^ *| *$/,"")')
        local pwd=$(grep "^${cluster}" ${CONF_DIR}/Install_service.csv|grep "Master"|awk -F "," '{print $6}'|awk 'gsub(/^ *| *$/,"")')
        
        ALL_HOSTS="${ALL_HOSTS} ${ip},${pwd}"
        
        tempKey ${ip} ${pwd}
        hostname
        sleep 2s
        tempKey ${ip} ${pwd}
        scp -r ${pkg_dir}/${cluster}_pkg/install_*.tar.gz ${ip}:/tmp >/dev/null 2>&1
        ssh ${ip} "tar -zxvf /tmp/install_*.tar.gz -C /tmp"
        [ $? -ne 0 ] && return 1
        ssh ${ip} "bash -x /tmp/install_*/bin/install.sh"
        [ $? -ne 0 ] && return 1
        ${SENDPWD} ${pwd} ${ip}:/tmp/install_*/logs/*.log ${RUN_PATH}/../logs >/dev/null 2>&1
        
    done
    return 0
}

function main()
{
    [ $? -ne 0 ] && return 1
    makeExp ${SENDPWD} ${RUNPWD}
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    rm -rf ${CORE_CONFIG} && touch ${CORE_CONFIG}
    getService
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    builtPkg
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    install
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    sendCore
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    rm -rf ${SENDPWD} ${RUNPWD}
    
    mkdir -p /opt/service
    yes|cp ${RUN_PATH}/listen.sh ${RUN_PATH}/common.sh ${CONF_DIR}/conf.conf ${CORE_CONFIG} ${FILE_DIR}/software/system-api*.jar /opt/service
    yes|cp ${CORE_CONFIG} /opt/
    chmod +x -R /opt/service
    bash /opt/service/listen.sh
    echo "* * * * * bash /opt/service/listen.sh" >> /var/spool/cron/root
    echo "* * * * * sleep 15; bash /opt/service/listen.sh" >> /var/spool/cron/root
    echo "* * * * * sleep 30; bash /opt/service/listen.sh" >> /var/spool/cron/root
    echo "* * * * * sleep 45; bash /opt/service/listen.sh" >> /var/spool/cron/root
    
    bash ${FILE_DIR}/script/public_script/installJdk.sh ${FILE_DIR}/software
    source /etc/profile
    java -jar /opt/service/system-api*.jar >> /opt/service/system-api.out  2>&1 &
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?