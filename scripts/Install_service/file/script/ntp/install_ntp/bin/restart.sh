#!/bin/bash

function restartNtp()
{
    local hosts=$(cat /opt/hostList|awk -F ',' '{print $1}'|awk 'gsub(/^ *| *$/,"")')
    hosts=(${hosts})
    for i in ${hosts[@]}; do
        ssh ${i} '/etc/init.d/ntpd restart'
    done
}

function main()
{
    restartNtp
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?