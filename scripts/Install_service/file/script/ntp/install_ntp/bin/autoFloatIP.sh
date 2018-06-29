#!/bin/bash

NODE_NAME=""
NODE_NAME2=""
NODE_STATUS=""
FLOAT_IP="<SERVER_IP>"
ETH="<ETH>"

function getHostIp()
{
    LOCAL_IP="$(ifconfig eth0 |grep 'inet addr'|awk -F ':' '{print $2}'|awk '{print $1}')"
    O_HOST=$(grep -vw "${LOCAL_IP}" /opt/hostList |awk -F ',' '{print $1}'|awk 'gsub(/^ *| *$/,"")')
    return 0
}

function nodeStatus()
{
    /etc/init.d/ntpd status
    NODE_STATUS=$?
    return 0
}

function setFloatIp()
{
    if [ ${NODE_STATUS} -eq 0 ];then
        local ret=$(ifconfig |grep -w "${FLOAT_IP}" |wc -l)
        if [ ${ret} -eq 0 ];then
            ping ${O_HOST} -c 3
            if [ $? -ne 0 ];then
                ifconfig ${ETH} ${FLOAT_IP} up
                echo 'Master' > /opt/typeFlag
            else
                ret=$(ssh ${O_HOST} "ifconfig |grep -w '${FLOAT_IP}' |wc -l")
                if [ ${ret} -eq 0 ];then
                    ifconfig ${ETH} ${FLOAT_IP} up
                    echo 'Master' > /opt/typeFlag
                fi
            fi
        fi
    else
        /etc/init.d/ntpd start
        sleep 6s
        /etc/init.d/ntpd status
        if [ $? -ne 0 ];then
            local ret=$(ifconfig |grep -w "${FLOAT_IP}" |wc -l)
            if [ ${ret} -ne 0 ];then
                ip addr del ${FLOAT_IP} dev ${ETH}
                ifconfig ${ETH} down
                echo 'Slave' > /opt/typeFlag
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