#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/$(basename $0)_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
SENDPWD="${RUN_PATH}/sendPwd.exp"
RUNPWD="${RUN_PATH}/runPwd.exp"
CONFIG_DIR="$(cd ${RUN_PATH}/../config/;pwd)"
CORE_CONFIG="/opt/Core_service_password.csv"
OPTION="$@"
OPTION_NU="$#"

function getHosts()
{
    SERVICE_LIST=$(grep -v "^#" /opt/service/conf.conf |grep "^cluster_pkg"|awk -F '=' '{print $2}')
    SERVICE_LIST=(${SERVICE_LIST})
    return 0
}

function switchChange()
{
    local cluster=$1
    local pwd=$(grep "^${cluster}" ${CORE_CONFIG}|awk -F "," '{print $5}'|awk 'gsub(/^ *| *$/,"")')
    local ip=$(grep "^${cluster}" ${CORE_CONFIG}|awk -F "," '{print $3}'|awk 'gsub(/^ *| *$/,"")')
    ${RUNPWD} ${pwd} ssh ${ip} "hostname"
    ${RUNPWD} ${pwd} ssh ${ip} "bash /opt/service/change.sh"
    [ $? -ne 0 ] && return 1
    return 0
}

#rabbitMQ→greenplum→Hadoop
function main()
{
    makeExp ${SENDPWD} ${RUNPWD}
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    getHosts
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    if [ ${OPTION_NU} -eq 0 ];then
        log_error "The parameter can not be empty"
    elif [ ${OPTION_NU} -eq 1 ];then
        switchChange ${OPTION}
    else
        local temp=(${OPTION})
        for((i=0;i<${#temp[@]};i++));do
            for((j=${i}+1;j<=${#temp[@]};j++));do
                if [ "X${temp[${i}]}" == "X${temp[${j}]}" ];then
                    log_error "The parameter can not be repeated multiple times"
                    [ $? -ne 0 ] && return 1
                fi
            done
        done
        
        for i in ${temp[@]}; do
            switchChange ${i}
        done
    fi
    rm -rf ${SENDPWD} ${RUNPWD}
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?

