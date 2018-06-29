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

function getGpParameters()
{

    GP_ROOT_PWD=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $6}'|awk 'gsub(/^ *| *$/,"")')
    SERVER_IP=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $5}'|awk 'gsub(/^ *| *$/,"")')
    GP_USER_PWD=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $12}'|awk 'gsub(/^ *| *$/,"")')
    MASTER_HOST=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{printf $4;printf " ";print $3}'|awk 'gsub(/^ *| *$/,"")')
    SLAVE_HOST=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Slave"|awk -F "," '{printf $4;printf " ";print $3}'|awk 'gsub(/^ *| *$/,"")')
    GP_MASTER_IP=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $4}'|awk 'gsub(/^ *| *$/,"")')

    sed -i "/^${CLUSTER},.*/d" ${CORE_CONFIG}
    echo "${CLUSTER},${SERVER_IP},${GP_MASTER_IP},0,${GP_ROOT_PWD},gpadmin,${GP_USER_PWD},${SOFTWARE}" >> ${CORE_CONFIG}
    return 0
}

function makeGpPkg()
{
    local tempDir="${RUN_PATH}/temp"
    mkdir -p ${tempDir}
    echo $?
    yes|cp -r ${RUN_PATH}/install_greenplum ${tempDir}
    yes|cp -r ${FILE_DIR}/software/greenplum-db.tar.gz ${tempDir}/install_greenplum/file
    local configFile="${tempDir}/install_greenplum/config/config.conf"
    [ -f ${configFile} ] && rm -rf ${configFile}
    touch ${configFile}
    
    echo "root_password=${GP_ROOT_PWD}
master_host=${MASTER_HOST}
standby_host=${SLAVE_HOST}" >>${configFile}
    grep "^${CLUSTER}" ${CONFIG_FILE}|grep -E "Data"|awk -F "," '{printf "data_host=";printf $4;printf " ";print $3}'|awk 'gsub(/^ *| *$/,"")' >>${configFile}
    echo "gp_user_password=${GP_USER_PWD}
PGPORT=5432
PGDATABASE=pis100
server_ip=${SERVER_IP}
ethernet_port=eth0:vip" >>${configFile}

    local hostList="${tempDir}/install_greenplum/config/hostList"
    local all_ip=$(grep "^${CLUSTER}" ${CONFIG_FILE}|awk -F "," '{printf $4","$7","$8;print ""}'|awk 'gsub(/^ *| *$/,"")')
    getHostList ${hostList} x ${all_ip}

    yes|cp -r ${RUN_PATH}/../../../bin/common.sh ${tempDir}/install_greenplum/bin
    yes|cp -r ${FILE_DIR}/script/public_script/makeSshKey.sh ${tempDir}/install_greenplum/bin
    yes|cp -r ${FILE_DIR}/script/public_script/addUser.sh ${tempDir}/install_greenplum/bin
    sed -i "s|<CLUSTER>|${CLUSTER}|g" "${tempDir}/install_greenplum/bin/status.sh"
    
    chmod +x -R ${tempDir}/install_greenplum/
    cd ${tempDir}
    [ ! -d ${OUT_DIR} ] && mkdir -p ${OUT_DIR}
    tar -zcvf ${OUT_DIR}/install_greenplum.tar.gz install_greenplum
    cd - >/dev/null 2>&1
    
    rm -rf ${tempDir}
    return 0
}

function main()
{
    getGpParameters
    [ $? -ne 0 ] && return 1
    makeGpPkg
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?
