#!/bin/bash

NODE_STATUS=""
FLOAT_IP="<SERVER_IP>"
ETH="<ETH>"
HOST=""
HOST_IP=""

ON_NAME=""
[ "X${ON_NAME}" != "X$(cat /opt/service/onName.txt)" ] && exit 0

##yes|gpactivatestandby -d /home/gpadmin/gpdata/gpmaster/gpseg-1

function getHostIp()
{
    HOST=$(cat /opt/hostList |head -2|awk -F ',' '{print $1}'|awk 'gsub(/^ *| *$/,"")')
    HOST=(${HOST})
    local ret=$(ifconfig |grep -w "${HOST[0]}" |wc -l)
    if [ ${ret} -gt 0 ];then
        HOST_IP=${HOST[1]}
    else
        HOST_IP=${HOST[0]}
    fi
    return 0
}

function nodeStatus()
{
    su - gpadmin -c "\q|psql" >/dev/null 2>&1
    NODE_STATUS=$?
    return 0
}

function setFloatIp()
{
    if [ ${NODE_STATUS} -eq 0 ];then
        ##当前节点为主节点，数据库运行正常
        local ret=$(ifconfig |grep -w "${FLOAT_IP}" |wc -l)
        if [ ${ret} -eq 0 ];then
            ifconfig ${ETH} ${FLOAT_IP} up
            echo "Master" > /opt/typeFlag
            ssh ${HOST_IP} "echo 'Slave' > /opt/typeFlag"
        fi
    else
        ##当前节点数据库未运行，可能为备机，也可能为异常
        local flag=0
        local name=$(ssh ${HOST_IP} "hostname")
        ssh ${HOST_IP} "su - gpadmin -c '\q|psql'"
        local otherStatus=$?
        ##对端节点数据库运行状态0正常
        
        ##当前节点是否为备机，ret=1是为备机
        local ret=$(su - gpadmin -c "\q|psql >~/temp.log 2>&1;cat ~/temp.log|grep starting |wc -l;rm ~/temp.log")
        
        if [ ${ret} -eq 0 ];then
            if [ ${otherStatus} -eq 0 ];then
                rm -rf /home/gpadmin/gpdata/gpmaster/gpseg-1_bak
                mv /home/gpadmin/gpdata/gpmaster/gpseg-1 /home/gpadmin/gpdata/gpmaster/gpseg-1_bak
                ssh ${HOST_IP} "su - gpadmin -c 'yes|gpinitstandby -s $(hostname)'"
                [ $? -ne 0 ] && mv /home/gpadmin/gpdata/gpmaster/gpseg-1_bak /home/gpadmin/gpdata/gpmaster/gpseg-1 && return 1
                
                echo "Slave" > /opt/typeFlag
                ssh ${HOST_IP} "echo 'Master' > /opt/typeFlag"
            else
                su - gpadmin -c '/opt/greenplum/greenplum-db/bin/gpstart -a'
            fi
        else
            if [ ${otherStatus} -ne 0 ];then
                su - gpadmin -c "yes|gpactivatestandby -d /home/gpadmin/gpdata/gpmaster/gpseg-1"
                [ $? -ne 0 ] && return 1
                ssh ${HOST_IP} "rm -rf /home/gpadmin/gpdata/gpmaster/gpseg-1_bak"
                ssh ${HOST_IP} "mv /home/gpadmin/gpdata/gpmaster/gpseg-1 /home/gpadmin/gpdata/gpmaster/gpseg-1_bak"
                su - gpadmin -c "yes|gpinitstandby -s ${name}"
                [ $? -ne 0 ] && ssh ${HOST_IP} "mv /home/gpadmin/gpdata/gpmaster/gpseg-1_bak /home/gpadmin/gpdata/gpmaster/gpseg-1" && return 1
                ifconfig ${ETH} ${FLOAT_IP} up
                echo "Master" > /opt/typeFlag
                ssh ${HOST_IP} "echo 'Slave' > /opt/typeFlag"
                flag=1
            fi
        fi
        
        if [ ${flag} -eq 0 ];then
            ret=$(ifconfig |grep -w "${FLOAT_IP}" |wc -l)
            if [ ${ret} -ne 0 ];then
                ip addr del ${FLOAT_IP} dev ${ETH}
                ifconfig ${ETH} down
            fi
        fi
    fi
    return 0
}

function main()
{
    source /etc/profile
    getHostIp
    [ $? -ne 0 ] && return 1
    nodeStatus
    [ $? -ne 0 ] && return 1
    setFloatIp
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?