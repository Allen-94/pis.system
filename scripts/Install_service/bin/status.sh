#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
SENDPWD="${RUN_PATH}/sendPwd.exp"
RUNPWD="${RUN_PATH}/runPwd.exp"
CORE_CONFIG="/opt/Core_service_password.csv"
OPTION="$@"
OPTION_NU="$#"

###hadoop,Master,vm1,192.168.2.33,192.168.2.32,0,host1,192.168.0.100,
###hadoop,Slave,vm2,192.168.2.34,,0,host1,192.168.0.100,
###greenplum,Data,vm3,192.168.2.35,,0,,,


function getConf()
{
    SERVICE_LIST=$(grep -v "^#" /opt/service/conf.conf |grep "^cluster_pkg"|awk -F '=' '{print $2}')
    SERVICE_LIST=(${SERVICE_LIST})
    return 0
}

## hostList
##  
##

###new：集群名称,节点类型,所在虚拟机名称,虚拟机IP,浮动IP,服务状态,所在服务器名,所在服务器IP, ---status.sh
###/opt/hdfs.config
### num=0
### master=192.168.2.22 nn1
### slave=192.189.2.23 nn2
### model=0
### server_name=vm1
### server_ip=192.168.0.100
### 
### a=$(cat -n /opt/hdfs.conf|grep "slave"|awk '{print $1}')
### sed -i "${a}c slave=192.168.2.23 nn2" /opt/hdfs.conf
###
### hdfs haadmin -getServiceState nn1
### grep -v "^greenplum" ../config/Core_service_password.csv >new_Core_service_password.csv
### grep "^greenplum" ../config/Core_service_password.csv|sed 's|,[0-9]*,|,2,|g' >>new_Core_service_password.csv
###


function getStatus()
{
    local ip=$1
    local pwd=$2
    ${RUNPWD} ${pwd} ssh ${ip} "hostname"
    [ $? -ne 0 ] && return 1
    
    local all_ip=$(getAllHostIp ${RUNPWD} ${pwd} ${ip})
    [ $? -ne 0 ] && return 1
    
    local hosts=(${all_ip})
    for i in ${hosts[@]}; do
        local stat=$(${RUNPWD} ${pwd} ssh ${i} "bash /opt/service/status.sh")
        local ret=$?
        [ ${ret} -ne 0 ] && [ ${ret} -ne 127 ] && return 1
        echo ${stat}|awk '{print $NF}'|awk 'gsub(/^ *| *$/,"")' >> /opt/status.csv
        done
    return 0
}

function checkStatus()
{

    for i in ${SERVICE_LIST[@]}; do
        local cluster=$(echo ${i}|awk -F ':' '{print $1}')
        local pwd=$(grep "^${cluster}" ${CORE_CONFIG}|awk -F "," '{print $5}'|awk 'gsub(/^ *| *$/,"")')
        local ip=$(grep "^${cluster}" ${CORE_CONFIG}|awk -F "," '{print $3}'|awk 'gsub(/^ *| *$/,"")')
        getStatus ${ip} ${pwd}
        [ $? -ne 0 ] && return 1
    done
    
    return 0
}
function main()
{
    makeExp ${SENDPWD} ${RUNPWD}
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    getConf
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    rm -rf /opt/status.csv
    touch /opt/status.csv
    checkStatus
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    sed -i "/^directory/d" /opt/status.csv
    sed -i "/^password/d" /opt/status.csv
    return 0
}

main
exit $?

