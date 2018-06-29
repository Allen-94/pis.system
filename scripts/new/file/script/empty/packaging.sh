#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
LOG_FILE="${RUN_PATH}/logs"
. ${RUN_PATH}/../../../bin/common.sh
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/$(basename $0)_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}

CONFIG_DIR="$(cd ${RUN_PATH}/../../../config/;pwd)"
CONFIG_FILE="${CONFIG_DIR}/Install_service.csv"
FILE_DIR="$(cd ${RUN_PATH}/../../../file/;pwd)"
CORE_CONFIG="${CONFIG_DIR}/Core_service_password.csv"
CLUSTER=$1
SOFTWARE=$2
OUT_DIR=$3

function getParameters()
{
    ROOT_PWD=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $6}'|awk 'gsub(/^ *| *$/,"")')
    USER=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $11}'|awk 'gsub(/^ *| *$/,"")')
    USER_PWD=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $12}'|awk 'gsub(/^ *| *$/,"")')
    SERVER_IP=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $5}'|awk 'gsub(/^ *| *$/,"")')
    MASTER_HOST=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{printf $4;printf " ";print $3}'|awk 'gsub(/^ *| *$/,"")')
    MASTER_IP=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $4}'|awk 'gsub(/^ *| *$/,"")')
    sed -i "/^${CLUSTER},.*/d" ${CORE_CONFIG}
    echo "${CLUSTER},${SERVER_IP},${MASTER_IP},0,${ROOT_PWD},,,${SOFTWARE}" >> ${CORE_CONFIG}
    return 0
}

function makePkg()
{
    local tempDir="${RUN_PATH}/temp"
    mkdir -p ${tempDir}
    yes|cp -r ${RUN_PATH}/install_empty ${tempDir}
    local configFile="${tempDir}/install_empty/config/config.conf"
    [ -f ${configFile} ] && rm -rf ${configFile}
    touch ${configFile}
    
    echo "root_password=${ROOT_PWD}
local_host=${MASTER_HOST}
server_ip=${SERVER_IP}
ethernet_port=eth0:vip" >>${configFile}
    grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Slave"|awk -F "," '{printf "other_host=";printf $4;printf " ";print $3}'|awk 'gsub(/^ *| *$/,"")' >>${configFile}

    local hostList="${tempDir}/install_empty/config/hostList"
    local all_ip=$(grep "^${CLUSTER}" ${CONFIG_FILE}|awk -F "," '{printf $4","$7","$8;print ""}'|awk 'gsub(/^ *| *$/,"")')
    getHostList ${hostList} x ${all_ip}
    
    yes|cp -r ${RUN_PATH}/../../../bin/common.sh ${tempDir}/install_empty/bin
    yes|cp -r ${FILE_DIR}/script/public_script/makeSshKey.sh ${tempDir}/install_empty/bin
    sed -i "s|<CLUSTER>|${CLUSTER}|g" "${tempDir}/install_empty/bin/status.sh"

    chmod +x -R ${tempDir}/install_empty/
    cd ${tempDir}
    [ ! -d ${OUT_DIR} ] && mkdir -p ${OUT_DIR}
    tar -zcvf ${OUT_DIR}/install_empty.tar.gz install_empty
    cd - >/dev/null 2>&1
    
    rm -rf ${tempDir}
    return 0
}

function main()
{
    getParameters
    [ $? -ne 0 ] && return 1
    makePkg
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?
