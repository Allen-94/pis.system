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


function getConfig()
{
    ROOT_PWD=$(getValue "root_password")
    HOST=$(getValue "other_host" |awk '{print $2}')
    setHosts
    [ $? -ne 0 ] && return 1
    return 0
}

function install()
{
    mv -f ${RUN_PATH}/../config/hostList /opt/
    echo 'Master' > /opt/typeFlag
    local server_ip=$(getValue "server_ip")
    sed -i "s|<SERVER_IP>|${server_ip}|g" "${RUN_PATH}/status.sh"
    mkdir -p /opt/service
    yes|cp ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh /opt/service/
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        scp -r /opt/hostList ${i}:/opt
        ssh ${i} "echo 'Slave' > /opt/typeFlag"
        ssh ${i} "mkdir -p /opt/service"
        scp -r ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${i}:/opt/service/
    done
    local server_ip=$(getValue "server_ip")
    local ethernet_port=$(getValue "ethernet_port")
    ifconfig ${ethernet_port} ${server_ip} up
    return 0
}

function main()
{
    getConfig
    [ $? -ne 0 ] && return 1
    install
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?

