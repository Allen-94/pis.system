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

function getHdParameters()
{
    [ -f ${FILE_DIR}/software/glibc-*.tar.gz ] || log_error "Package 'glibc' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/hadoop-*.tar.gz ] || log_error "Package 'hadoop' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/jdk1.8*.tar.gz ] || log_error "Package 'jdk1.8' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/zookeeper-*.tar.gz ] || log_error "Package 'zookeeper' does not exist!"
    [ $? -ne 0 ] && return 1
    
    HD_ROOT_PWD=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $6}'|awk 'gsub(/^ *| *$/,"")')
    SERVER_IP=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $5}'|awk 'gsub(/^ *| *$/,"")')
    HD_USER_PWD=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $12}'|awk 'gsub(/^ *| *$/,"")')
    MASTER_HOST=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{printf $4;printf " ";print $3}'|awk 'gsub(/^ *| *$/,"")')
    HD_MASTER_IP=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $4}'|awk 'gsub(/^ *| *$/,"")')
    
    sed -i "/^${CLUSTER},.*/d" ${CORE_CONFIG}
    echo "${CLUSTER},${SERVER_IP},${HD_MASTER_IP},0,${HD_ROOT_PWD},hadoop,${HD_USER_PWD},${SOFTWARE}" >> ${CORE_CONFIG}
    return 0
}

function makeHdPkg()
{
    local tempDir="${RUN_PATH}/temp"
    mkdir -p ${tempDir}
    yes|cp -r ${RUN_PATH}/install_hadoop ${tempDir}
    yes|cp -r ${FILE_DIR}/software/glibc-*.tar.gz ${tempDir}/install_hadoop/file
    yes|cp -r ${FILE_DIR}/software/hadoop-*.tar.gz ${tempDir}/install_hadoop/file
    yes|cp -r ${FILE_DIR}/software/jdk1.8*.tar.gz ${tempDir}/install_hadoop/file
    yes|cp -r ${FILE_DIR}/software/zookeeper-*.tar.gz ${tempDir}/install_hadoop/file
    local configFile="${tempDir}/install_hadoop/config/config.conf"
    [ -f ${configFile} ] && rm -rf ${configFile}
    touch ${configFile}
    
    echo "root_password=${HD_ROOT_PWD}
master_host=${MASTER_HOST}" >>${configFile}
    grep "^${CLUSTER}" ${CONFIG_FILE}|grep -E "Slave|Data"|awk -F "," '{printf "data_host=";printf $4;printf " ";print $3}'|awk 'gsub(/^ *| *$/,"")' >>${configFile}
    echo "hd_user_password=${HD_USER_PWD}
dfs_replication_number=3
server_ip=${SERVER_IP}
ethernet_port=eth0:vip" >>${configFile}

    local hostList="${tempDir}/install_hadoop/config/hostList"
    local all_ip=$(grep "^${CLUSTER}" ${CONFIG_FILE}|awk -F "," '{printf $4","$7","$8;print ""}'|awk 'gsub(/^ *| *$/,"")')
    getHostList ${hostList} x ${all_ip}

    yes|cp -r ${RUN_PATH}/../../../bin/common.sh ${tempDir}/install_hadoop/bin
    yes|cp -r ${FILE_DIR}/script/public_script/makeSshKey.sh ${tempDir}/install_hadoop/bin
    yes|cp -r ${FILE_DIR}/script/public_script/addUser.sh ${tempDir}/install_hadoop/bin
    yes|cp -r ${FILE_DIR}/script/public_script/installJdk.sh ${tempDir}/install_hadoop/bin
    yes|cp -r ${FILE_DIR}/script/public_script/upgradeGlibc.sh ${tempDir}/install_hadoop/bin
    sed -i "s|<CLUSTER>|${CLUSTER}|g" "${tempDir}/install_hadoop/bin/status.sh"
    
    yes|cp ${CONFIG_DIR}/permit_nfs_ip.txt ${tempDir}/install_hadoop/config/
    
    chmod +x -R ${tempDir}/install_hadoop/
    cd ${tempDir}
    [ ! -d ${OUT_DIR} ] && mkdir -p ${OUT_DIR}
    tar -zcvf ${OUT_DIR}/install_hadoop.tar.gz install_hadoop
    cd - >/dev/null 2>&1
    
    rm -rf ${tempDir}
    return 0
}


function main()
{
    getHdParameters
    [ $? -ne 0 ] && return 1
    makeHdPkg
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?
