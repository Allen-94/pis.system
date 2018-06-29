#!/bin/bash

NODE_NAME=""
NODE_NAME2=""
NODE_STATUS=""
FLOAT_IP="<SERVER_IP>"
ETH="<ETH>"

##hdfs haadmin -getServiceState nn1
#hdfs haadmin -transitionToActive/transitionToStandby --forcemanual nn1
function getNodeName()
{
    local node1=$(cat /usr/hadoop/etc/hadoop/node |head -1 |awk 'gsub(/^ *| *$/,"")')
    node1=(${node1})
    local node2=$(cat /usr/hadoop/etc/hadoop/node |tail -1 |awk 'gsub(/^ *| *$/,"")')
    node2=(${node2})
    local ret=$(ifconfig |grep -w "${node1[0]}" |wc -l)
    if [ ${ret} -gt 0 ];then
        NODE_NAME=${node1[1]}
        NODE_NAME2=${node2[1]}
    else
        NODE_NAME=${node2[1]}
        NODE_NAME2=${node1[1]}
    fi
    return 0
}

function nodeStatus()
{
    NODE_STATUS=$(hdfs haadmin -getServiceState ${NODE_NAME})
    return 0
}

function setFloatIp()
{
    if [ "X${NODE_STATUS}" == "Xactive" ];then
        local ret=$(ifconfig |grep -w "${FLOAT_IP}" |wc -l)
        if [ ${ret} -eq 0 ];then
            ifconfig ${ETH} ${FLOAT_IP} up
            yes|cp /usr/hadoop/etc/hadoop/site/${NODE_NAME}/hdfs-site.xml  /usr/hadoop/etc/hadoop/
            yes|cp /usr/hadoop/etc/hadoop/site/${NODE_NAME}/core-site.xml  /usr/hadoop/etc/hadoop/
            /usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs stop portmap
            su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs stop nfs3"
            sleep 3s
            /usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs start portmap
            su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs start nfs3"
            echo "Master" > /opt/typeFlag
        fi
    else
        if [ "X${NODE_STATUS}" == "Xstandby" ];then
            echo "Slave" > /opt/typeFlag
            ip addr del ${FLOAT_IP} dev ${ETH}
            ifconfig ${ETH} down
        fi
    fi
    return 0
}

function main()
{
    source /etc/profile
    getNodeName
    [ $? -ne 0 ] && return 1
    nodeStatus
    [ $? -ne 0 ] && return 1
    setFloatIp
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?