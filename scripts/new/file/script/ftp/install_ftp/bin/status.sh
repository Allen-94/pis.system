#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
SERVER_IP="<SERVER_IP>"


function getParam()
{
    HOST_NAME="$(hostname)"
    STATUS=2
    LOCAL_IP="$(ifconfig eth0 |grep 'inet addr'|awk -F ':' '{print $2}'|awk '{print $1}')"
    VRM_NAME=$(cat /opt/hostList|grep -w "${LOCAL_IP}" |awk -F ',' '{print $2}')
    VRM_IP=$(cat /opt/hostList|grep -w "${LOCAL_IP}" |awk -F ',' '{print $3}')
    NODE_TYPE="$(cat /opt/typeFlag)"
    return 0
}

function checkStatus()
{
    /etc/init.d/vsftpd status
    if [ $? -eq 0 ];then
        local ret=$(ifconfig |grep -w "${SERVER_IP}" |wc -l)
        if [ ${ret} -eq 0 ];then
            STATUS=0
            NODE_TYPE="Slave"
            echo "${NODE_TYPE}" >/opt/typeFlag
        else
            STATUS=0
            NODE_TYPE="Master"
            echo "${NODE_TYPE}" >/opt/typeFlag
        fi
    fi
    
    if [ "X${NODE_TYPE}" == "XMaster" ];then
        echo "<CLUSTER>,${NODE_TYPE},${HOST_NAME},${LOCAL_IP},${SERVER_IP},${STATUS},${VRM_NAME},${VRM_IP},;"
    else
        echo "<CLUSTER>,${NODE_TYPE},${HOST_NAME},${LOCAL_IP},,${STATUS},${VRM_NAME},${VRM_IP},;"
    fi
    
    return 0
}

function main()
{
    getParam
    [ $? -ne 0 ] && return 1
    checkStatus
    [ $? -ne 0 ] && return 1
    return 0
}


main
exit $?

