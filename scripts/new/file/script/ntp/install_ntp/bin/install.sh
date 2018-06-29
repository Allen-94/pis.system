#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/installNtp_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
CONFIG_FILE="${RUN_PATH}/../config/config.conf"
FILE_PATH="${RUN_PATH}/../file"
CONF_DIR="${RUN_PATH}/../config/"

function getConfig()
{
    ROOT_PWD=$(getValue "root_password")
    HOST=$(getValue "other_host" |awk '{print $2}')
    setHosts
    [ $? -ne 0 ] && return 1
    return 0
}

function installNtp()
{
    rpm -ivh ${FILE_PATH}/ntpdate*
    [ $? -ne 0 ] && return 1
    rpm -ivh ${FILE_PATH}/ntp-*
    [ $? -ne 0 ] && return 1
    
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        scp -r ${FILE_PATH}/ntp* ${i}:/tmp/
        ssh ${i} "rpm -ivh /tmp/ntpdate*"
        [ $? -ne 0 ] && return 1
        ssh ${i} "rpm -ivh /tmp/ntp-*"
        [ $? -ne 0 ] && return 1
        ssh ${i} "rm -rf /tmp/ntp*"
    done
    return 0
}

function setNtpd()
{
    echo "restrict 0.0.0.0 mask 0.0.0.0 nomodify notrap
server 127.127.1.0
fudge 127.127.1.0 stratum 10" >> /etc/ntp.conf

    mv -f ${RUN_PATH}/../config/hostList /opt/

    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        scp -r /etc/ntp.conf ${i}:/etc/
        scp -r /opt/hostList ${i}:/opt/ >/dev/null 2>&1
    done
    return 0
}

function startNtpd()
{
    /sbin/chkconfig ntpd on
    [ $? -ne 0 ] && return 1
    /etc/init.d/ntpd start
    [ $? -ne 0 ] && return 1
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        ssh ${i} "/sbin/chkconfig ntpd on"
        [ $? -ne 0 ] && return 1
        ssh ${i} "/etc/init.d/ntpd start"
        [ $? -ne 0 ] && return 1
    done
    return 0
}

function setOther()
{
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        setServerIp ${i}
        [ $? -ne 0 ] && return 1
    done

    return 0
}

function typeFlag()
{
    local server_ip=$(getValue "server_ip")
    sed -i "s|<SERVER_IP>|${server_ip}|g" "${RUN_PATH}/status.sh"
    mkdir -p /opt/service
    yes|cp ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh /opt/service/
    
    echo "Master" > /opt/typeFlag
    
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        ssh ${i} "echo 'Slave' > /opt/typeFlag"
        ssh ${i} "mkdir -p /opt/service"
        scp -r ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${i}:/opt/service/
    done
    
    return 0
}

function main()
{
    getConfig
    [ $? -ne 0 ] && return 1
    installNtp
    [ $? -ne 0 ] && return 1
    setNtpd
    [ $? -ne 0 ] && return 1
    startNtpd
    [ $? -ne 0 ] && return 1
    setOther
    [ $? -ne 0 ] && return 1
    typeFlag
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?