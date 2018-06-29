#!/bin/bash
RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/bin/common.sh
CONF_DIR="${RUN_PATH}/config"
FILE_DIR="${RUN_PATH}/file"
LOG_FILE="${RUN_PATH}/logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/sys_install_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}

function checkPkg()
{
    [ -f ${FILE_DIR}/software/otp_src_*.tar.gz ] || log_error "Package 'otp_src' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/rabbitmq-server-*.noarch.rpm ] || log_error "Package 'rabbitmq-server' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/simplejson-*.tar.gz ] || log_error "Package 'simplejson' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/glibc-*.tar.gz ] || log_error "Package 'glibc' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/hadoop-*.tar.gz ] || log_error "Package 'hadoop' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/jdk1.8*.tar.gz ] || log_error "Package 'jdk1.8' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/zookeeper-*.tar.gz ] || log_error "Package 'zookeeper' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f ${FILE_DIR}/software/greenplum-db.tar.gz ] || log_error "Package 'greenplum-db' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f "${CONF_DIR}/Install_service.csv" ] || log_error "The configuration file '${CONF_DIR}/Install_service.csv' does not exist!"
    [ $? -ne 0 ] && return 1
    [ -f "${CONF_DIR}/Install_mode.txt" ] || log_error "The configuration file '${CONF_DIR}/Install_mode.txt' does not exist!"
    [ $? -ne 0 ] && return 1
    return 0
}

function cpPkg()
{
    [ -d /tmp/sys_install ] && rm -rf /tmp/sys_install
    mkdir -p /tmp/sys_install
    yes|cp -r ${RUN_PATH}/bin ${RUN_PATH}/logs ${RUN_PATH}/config ${RUN_PATH}/file /tmp/sys_install/
    return 0
}

function main()
{
    checkPkg
    [ $? -ne 0 ] && return 1
    cpPkg
    [ $? -ne 0 ] && return 1
    bash -x /tmp/sys_install/bin/install_service.sh
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}

exit $?