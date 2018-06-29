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

function getRbParameters()
{
    [ -f ${FILE_DIR}/software/keyutils-*.rpm ] || log_error "Package 'keyutils' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/libevent-*.rpm ] || log_error "Package 'libevent' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/libgssglue-*.rpm ] || log_error "Package 'libgssglue' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/libtirpc-*.rpm ] || log_error "Package 'libtirpc' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/nfs-utils-1*.rpm ] || log_error "Package 'nfs-utils' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/nfs-utils-lib-*.rpm ] || log_error "Package 'nfs-utils-lib' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/python-argparse-*.rpm ] || log_error "Package 'python-argparse' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/rpcbind-*.rpm ] || log_error "Package 'rpcbind' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/vsftpd-*.rpm ] || log_error "Package 'vsftpd*.rpm' does not exist!"
    [ $? -ne 0 ] && return 1
    ROOT_PWD=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $6}'|awk 'gsub(/^ *| *$/,"")')
    SERVER_IP=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $5}'|awk 'gsub(/^ *| *$/,"")')
    USER=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $11}'|awk 'gsub(/^ *| *$/,"")')
    USER_PWD=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $12}'|awk 'gsub(/^ *| *$/,"")')
    MASTER_HOST=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{printf $4;printf " ";print $3}'|awk 'gsub(/^ *| *$/,"")')
    MASTER_IP=$(grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Master"|awk -F "," '{print $4}'|awk 'gsub(/^ *| *$/,"")')
    HADOOP_ROOT_PWD=$(grep "hadoop" ${CONFIG_FILE} |grep "Master" |head -1|awk -F "," '{print $6}'|awk 'gsub(/^ *| *$/,"")')
    HADOOP_IP=$(grep "hadoop" ${CONFIG_FILE} |grep "Master" |head -1|awk -F "," '{print $4}'|awk 'gsub(/^ *| *$/,"")')
    
    sed -i "/^${CLUSTER},.*/d" ${CORE_CONFIG}
    echo "${CLUSTER},${SERVER_IP},${MASTER_IP},0,${ROOT_PWD},${USER},${USER_PWD},${SOFTWARE}" >> ${CORE_CONFIG}
    return 0
}

function makeRbPkg()
{
    local tempDir="${RUN_PATH}/temp"
    mkdir -p ${tempDir}
    yes|cp -r ${RUN_PATH}/install_ftp ${tempDir}
    
    yes|cp -r ${FILE_DIR}/software/keyutils-*.rpm ${tempDir}/install_ftp/file
    yes|cp -r ${FILE_DIR}/software/lib*.rpm ${tempDir}/install_ftp/file
    yes|cp -r ${FILE_DIR}/software/nfs-utils-*.rpm ${tempDir}/install_ftp/file
    yes|cp -r ${FILE_DIR}/software/python-argparse-*.rpm ${tempDir}/install_ftp/file
    yes|cp -r ${FILE_DIR}/software/rpcbind-*.rpm ${tempDir}/install_ftp/file
    yes|cp -r ${FILE_DIR}/software/vsftpd-*.rpm ${tempDir}/install_ftp/file

    local configFile="${tempDir}/install_ftp/config/config.conf"
    [ -f ${configFile} ] && rm -rf ${configFile}
    touch ${configFile}
    
    echo "root_password=${ROOT_PWD}
ftp_user_name=${USER}
ftp_user_password=${USER_PWD}
local_host=${MASTER_HOST}
server_ip=${SERVER_IP}
ethernet_port=eth0:vip
hdfs_ip=${HADOOP_IP}
hdfs_root_pwd=${HADOOP_ROOT_PWD}" >>${configFile}
    grep "^${CLUSTER}" ${CONFIG_FILE}|grep "Slave"|awk -F "," '{printf "other_host=";printf $4;printf " ";print $3}'|awk 'gsub(/^ *| *$/,"")' >>${configFile}
    
    local hostList="${tempDir}/install_ftp/config/hostList"
    local all_ip=$(grep "^${CLUSTER}" ${CONFIG_FILE}|awk -F "," '{printf $4","$7","$8;print ""}'|awk 'gsub(/^ *| *$/,"")')
    getHostList ${hostList} x ${all_ip}

    yes|cp -r ${RUN_PATH}/../../../bin/common.sh ${tempDir}/install_ftp/bin
    yes|cp -r ${FILE_DIR}/script/public_script/makeSshKey.sh ${tempDir}/install_ftp/bin
    yes|cp -r ${FILE_DIR}/script/public_script/installNfsClient.sh ${tempDir}/install_ftp/bin
    sed -i "s|<CLUSTER>|${CLUSTER}|g" "${tempDir}/install_ftp/bin/status.sh"
    
    chmod +x -R ${tempDir}/install_ftp/
    cd ${tempDir}
    [ ! -d ${OUT_DIR} ] && mkdir -p ${OUT_DIR}
    tar -zcvf ${OUT_DIR}/install_ftp.tar.gz install_ftp
    cd - >/dev/null 2>&1
    
    rm -rf ${tempDir}
    return 0
}

function main()
{
    getRbParameters
    [ $? -ne 0 ] && return 1
    makeRbPkg
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?
