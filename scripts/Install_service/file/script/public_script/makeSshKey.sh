#!/bin/bash

##传参：本脚本.sh root密码 ,（逗号分隔符(注意:逗号前后有空格)，分隔符后面为主机ip，可多个）

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
ROOT_PWD=$1
HOST=$(echo $*|awk -F ',' '{print $2}')

function sshKey()
{
    local sendPwd="${RUN_PATH}/sendPwd.exp"
    local runPwd="${RUN_PATH}/runPwd.exp"
    local flag=1
    if [ ! -f ${sendPwd} ] || [ ! -f ${runPwd} ];then
        sendPwd="/tmp/sendPwd.exp"
        runPwd="/tmp/runPwd.exp"
        flag=0
        makeExp ${sendPwd} ${runPwd}
    fi
    chmod +x ${sendPwd} ${runPwd}
    rm -rf ~/.ssh/*
    ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa
    cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
    chmod 600 ~/.ssh/authorized_keys
    ${runPwd} ${ROOT_PWD} ssh $(hostname) "hostname"
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        ${runPwd} ${ROOT_PWD} ssh ${i} "rm -rf ~/.ssh/*"
        ${runPwd} ${ROOT_PWD} ssh ${i} "yes|ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa"
        ${sendPwd} ${ROOT_PWD} ${i}:~/.ssh/id_dsa.pub ~/.ssh/id_dsa.pub.bak >/dev/null 2>&1
        cat ~/.ssh/id_dsa.pub.bak >> ~/.ssh/authorized_keys
        rm -rf ~/.ssh/id_dsa.pub.bak
    done
    for i in ${hosts[@]}; do
        ${sendPwd} ${ROOT_PWD} ~/.ssh/authorized_keys ${i}:~/.ssh/ >/dev/null 2>&1
        ${sendPwd} ${ROOT_PWD} ~/.ssh/known_hosts ${i}:~/.ssh/ >/dev/null 2>&1
    done
    [ ${flag} -eq 0 ] && rm -rf ${sendPwd} ${runPwd}
    return 0
}

function main()
{
    sshKey
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?

