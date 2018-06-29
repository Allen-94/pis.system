#!/bin/bash

function restartHadoop()
{
    local hosts=$(cat /opt/hostList|awk -F ',' '{print $1}'|awk 'gsub(/^ *| *$/,"")')
    hosts=(${hosts})
    su - hadoop -c "/usr/hadoop/sbin/stop-all.sh"
    [ $? -ne 0 ] && return 1

    for i in ${hosts[@]}; do
        ssh ${i} 'su - hadoop -c "/home/zookeeper/bin/zkServer.sh stop"'
        ssh ${i} '/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs stop portmap'
        ssh ${i} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs stop nfs3"'
    done
    sleep 5s
    for i in ${hosts[@]}; do
        ssh ${i} 'su - hadoop -c "/home/zookeeper/bin/zkServer.sh start"'
        ssh ${i} '/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs start portmap'
        ssh ${i} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs start nfs3"'
    done
    
    su - hadoop -c "yes|/usr/hadoop/bin/hdfs zkfc -formatZK"
    [ $? -ne 0 ] && return 1
    su - hadoop -c "/usr/hadoop/sbin/start-all.sh"
    [ $? -ne 0 ] && return 1
    return 0
}

function main()
{
    restartHadoop
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?