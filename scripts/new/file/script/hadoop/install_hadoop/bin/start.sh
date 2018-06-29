#!/bin/bash

function startHadoop()
{
    local hosts=$(cat /opt/hostList|awk -F ',' '{print $1}'|awk 'gsub(/^ *| *$/,"")')
    hosts=(${hosts})
    for i in ${hosts[@]}; do
        ssh ${i} 'su - hadoop -c "/home/zookeeper/bin/zkServer.sh start"'
        ssh ${i} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh start journalnode"'
    done
    local flag=$(cat /opt/typeFlag)
    if [ "X${flag}" == "XMaster" ];then
        /usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs start portmap
        su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs start nfs3"
    fi
    
    ##su - hadoop -c "yes|/usr/hadoop/bin/hdfs zkfc -formatZK"
    ##[ $? -ne 0 ] && return 1
    su - hadoop -c "/usr/hadoop/sbin/start-all.sh"
    [ $? -ne 0 ] && return 1
    return 0
}

function main()
{
    startHadoop
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?