#!/bin/bash

function stopHadoop()
{
    local hosts=$(cat /opt/hostList|awk -F ',' '{print $1}'|awk 'gsub(/^ *| *$/,"")')
    hosts=(${hosts})
    su - hadoop -c "/usr/hadoop/sbin/stop-all.sh"
    for i in ${hosts[@]}; do
        ssh ${i} 'su - hadoop -c "/home/zookeeper/bin/zkServer.sh stop"'
        ssh ${i} '/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs stop portmap'
        ssh ${i} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs stop nfs3"'
    done
    return 0
}

function main()
{
    stopHadoop
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?