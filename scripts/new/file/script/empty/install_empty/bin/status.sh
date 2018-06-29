#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
SERVER_IP="<SERVER_IP>"

function getParam()
{
    HOST_NAME="$(hostname)"
    LOCAL_IP="$(ifconfig eth0 |grep 'inet addr'|awk -F ':' '{print $2}'|awk '{print $1}')"
    VRM_NAME=$(cat /opt/hostList|grep -w "${LOCAL_IP}" |awk -F ',' '{print $2}')
    VRM_IP=$(cat /opt/hostList|grep -w "${LOCAL_IP}" |awk -F ',' '{print $3}')
    NODE_TYPE="$(cat /opt/typeFlag)"
    return 0
}

function checkStatus()
{
    if [ "X${NODE_TYPE}" == "XMaster" ];then
        echo "<CLUSTER>,${NODE_TYPE},${HOST_NAME},${LOCAL_IP},${SERVER_IP},0,${VRM_NAME},${VRM_IP},;"
    else
        echo "<CLUSTER>,${NODE_TYPE},${HOST_NAME},${LOCAL_IP},,0,${VRM_NAME},${VRM_IP},;"
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

