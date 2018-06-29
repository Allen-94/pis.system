#!/bin/bash

function startFtp()
{
    local hosts=$(cat /opt/hostList|awk -F ',' '{print $1}'|awk 'gsub(/^ *| *$/,"")')
    hosts=(${hosts})
    for i in ${hosts[@]}; do
        ssh ${i} "service vsftpd start"
        ret=$?
        [ ${ret} -ne 0 ] && [ ${ret} -ne 127 ] && return 1
    done
    bash /opt/service/mount.sh -open
    [ $? -ne 0 ] && return 1
    return 0
}

function main()
{
    startFtp
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?