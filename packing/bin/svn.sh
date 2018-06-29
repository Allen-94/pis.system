#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/svn_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
CONF_DIR="${RUN_PATH}/../config/"
CONFIG_FILE="${CONF_DIR}/config.conf"
FILE_PATH="${RUN_PATH}/../file"

function getConfig()
{
    SVN_SRC=$(grep "^svn_src" ${CONFIG_FILE} |awk -F '=' '{print $2}')
    OUT_PATH=$(grep "^out_path" ${CONFIG_FILE} |awk -F '=' '{print $2}')
    PKG_PATH=$(grep "^pkg_path" ${CONFIG_FILE} |awk -F '=' '{print $2}')
    return 0
}

function chackout()
{
    yes|cp ${CONF_DIR}/svn.simple/* ~/.subversion/auth/svn.simple/
    rm -rf ${OUT_PATH}
    mkdir -p ${OUT_PATH}
    svn checkout ${SVN_SRC} ${OUT_PATH}
    [ $? -ne 0 ] && return 1
    return 0
}

function setProperties()
{
    local pro_dir="${CONF_DIR}/properties"
    sed -i "s|=gp|=prod|g" ${OUT_PATH}/src/main/resources/application.properties
    local path=$(grep "^upload.path" ${OUT_PATH}/src/main/resources/application-prod.properties |awk -F '=' '{print $2}')
    path=(${path})
    for i in ${path[@]}; do
        mkdir -p ${i}
    done
    mv ${OUT_PATH}/src/main/resources/rabbitmq-config2.properties ${OUT_PATH}/src/main/resources/rabbitmq-config.properties
    tar -zxvf ${FILE_PATH}/m2lib.tar.gz -C ~/
    return 0
}

function packing()
{
    cd ${OUT_PATH}
    mvn clean package -DskipTests
    [ $? -ne 0 ] && return 1
    [ ! -d ${PKG_PATH} ] && mkdir -p ${PKG_PATH}
    yes|cp ${OUT_PATH}/target/pis.ui*ar ${PKG_PATH}
    cd -
    return 0
}

function run()
{
    local num=$(ps -ef |grep "java -jar"|grep "pis.ui"|awk '{print $2}')
    kill -9 ${num}

    local str="bash ${RUN_PATH}/$(echo $0|awk -F '/' '{print $NF}')"
    local each=$(grep -n "" /var/spool/cron/root |grep "${str}"|awk -F ":" '{print $1}')
    local each=(${each})
    for((i=${#each[@]}-1;i>=0;i--));do
        sed -i "${each[${i}]}d" /var/spool/cron/root
    done
    echo "0 0 * * * ${str}">>/var/spool/cron/root
    java -jar /tmp/pkg_path/pis.ui*war > /tmp/pis.out  2>&1 &
    [ $? -ne 0 ] && return 1
    return 0
}

function main()
{
    source /etc/profile
    getConfig
    [ $? -ne 0 ] && return 1
    chackout
    [ $? -ne 0 ] && return 1
    setProperties
    [ $? -ne 0 ] && return 1
    packing
    [ $? -ne 0 ] && return 1
    run
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?