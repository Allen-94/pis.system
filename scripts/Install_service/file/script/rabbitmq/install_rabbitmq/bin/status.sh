#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
SERVER_IP="<SERVER_IP>"

#rabbitmq,Master,MQ-Master,192.168.2.33,192.168.2.32,0,vrm01,192.168.0.100,
#rabbitmq,Slave,MQ-Slave,192.168.2.34,,1,vrm02,192.168.0.101,

function getParam()
{
    HOST_NAME="$(hostname)"
    CONFIG_FILE="/opt/Core_service_password.csv"
    STATUS=2
    LOCAL_IP="$(ifconfig eth0 |grep 'inet addr'|awk -F ':' '{print $2}'|awk '{print $1}')"
    O_HOSTS=$(cat /opt/hostList|grep -vw "${LOCAL_IP}" |awk -F ',' '{print $1}')
    O_HOSTS=(${O_HOSTS})
    VRM_NAME=$(cat /opt/hostList|grep -w "${LOCAL_IP}" |awk -F ',' '{print $2}')
    VRM_IP=$(cat /opt/hostList|grep -w "${LOCAL_IP}" |awk -F ',' '{print $3}')
    NODE_TYPE=""
    local ret=$(grep -w "${LOCAL_IP}" /opt/Core_service_password.csv |wc -l)
    if [ ${ret} -eq 0 ];then
        NODE_TYPE="Slave"
    else
        NODE_TYPE="Master"
    fi
    return 0
}

function checkStatus()
{
    local ret=$(/usr/bin/nmap -sS 127.0.0.1 -p 5672 | grep 5672 | awk '{printf $2}')
    if [ "X${ret}" == "Xopen" ];then
        /etc/init.d/keepalived status >/dev/null 2>&1
        if [ $? -eq 0 ];then
            STATUS=0
            ret=$(ifconfig |grep -w "${SERVER_IP}" |wc -l)
            if [ ${ret} -eq 1 ];then
                NODE_TYPE="Master"
            else
                NODE_TYPE="Slave"
            fi
        else
            for i in ${O_HOSTS[@]}; do
                ret=$(ssh ${i} "/usr/bin/nmap -sS 127.0.0.1 -p 5672 | grep 5672")
                ret=$(echo ${ret} | awk '{printf $2}')
                if [ "X${ret}" == "Xopen" ];then
                    NODE_TYPE="Slave"
                    break
                fi
            done
        fi
    else
        for i in ${O_HOSTS[@]}; do
            ret=$(ssh ${i} "/usr/bin/nmap -sS 127.0.0.1 -p 5672 | grep 5672")
            ret=$(echo ${ret} | awk '{printf $2}')
            if [ "X${ret}" == "Xopen" ];then
                NODE_TYPE="Slave"
                break
            fi
        done
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

