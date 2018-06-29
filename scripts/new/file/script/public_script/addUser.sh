#!/bin/bash

######################################################################################
##input:                                                                            ##
##      $1:用户密码                                                                 ##
##      $1:用户名称                                                                 ##
##      $1:用户id                                                                   ##
##out/return:                                                                       ##
##           零为成功,非零为失败(通常为1)                                           ##
##variable:                                                                         ##
##         DIR_PATH:当前脚本所在路径                                                ##
##         EXP_FILE:临时expect脚本文件(用于ssh远程登陆,自动输入等待确认信息)        ##
##         USER_PWD:用户密码                                                        ##
##         USER_NAME:用户名称                                                       ##
##         USER_ID:用户id                                                           ##
##function:                                                                         ##
##         addUser:创建用户以及用户组                                               ##
##         setPwd:设置用户密码                                                      ##
##                $1:用户密码                                                       ##
##         mian:程序入口                                                            ##
######################################################################################

DIR_PATH=$(cd $(dirname $0);pwd)
EXP_FILE="/tmp/temp.exp"
USER_PWD=$1
USER_NAME=$2
USER_ID=$3
[ "X${USER_ID}" == "X" ] && USER_ID=530

function addUser()
{
    groupadd -g ${USER_ID} ${USER_NAME}
    useradd -g ${USER_ID} -u${USER_ID} -m -d /home/${USER_NAME} -s /bin/bash ${USER_NAME}
    return 0
}

function setPwd()
{
    [ -f ${EXP_FILE} ] && rm -rf ${EXP_FILE}
    echo -n '#!/usr/bin/expect
set timeout 5
spawn passwd '>${EXP_FILE}
    echo "${USER_NAME}">>${EXP_FILE}
    echo 'expect "*assword:"
send "[lindex $argv 0]\n"
expect "*assword:"
send "[lindex $argv 0]\n"
expect "*assword:"
exit 0'>>${EXP_FILE}
    chmod +x ${EXP_FILE}
    ${EXP_FILE} ${USER_PWD} >/dev/null 2>&1
    local ret=$?
    rm -rf ${EXP_FILE}
    return ${ret}
}

function main()
{
    addUser
    [ $? -ne 0 ] && return 1
    setPwd ${USER_PWD}
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?
