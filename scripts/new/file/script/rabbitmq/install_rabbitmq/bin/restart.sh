#!/bin/bash

function restartRabbit()
{
    local hosts=$(cat /opt/hostList|awk -F ',' '{print $1}'|awk 'gsub(/^ *| *$/,"")')
    hosts=(${hosts})
    for i in ${hosts[@]}; do
        ssh ${i} "source /etc/profile;/etc/init.d/keepalived stop;rabbitmqctl stop"
        ret=$?
        [ ${ret} -ne 0 ] && [ ${ret} -ne 127 ] && return 1
        sleep 2s
        ssh ${i} "source /etc/profile;/etc/init.d/keepalived start;rabbitmq-server -detached"
        ret=$?
        [ ${ret} -ne 0 ] && [ ${ret} -ne 127 ] && return 1
    done
    return 0
}

function main()
{
    restartRabbit
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?