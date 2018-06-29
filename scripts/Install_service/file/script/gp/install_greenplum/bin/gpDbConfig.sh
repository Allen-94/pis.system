#!/bin/bash

######################################################################################
##input:                                                                            ##
##      $1:数据库访问端口号                                                         ##
##      $2:数据库实例名                                                             ##
##      $3:备节点主机名                                                             ##
##      $4:gpadmin用户密码                                                          ##
##out/return:                                                                       ##
##           零为成功,非零为失败(通常为1)                                           ##
##variable:                                                                         ##
##         RUN_PATH:当前脚本所在路径                                                ##
##         PGPORT:数据库访问端口号                                                  ##
##         PGDATABASE:数据库实例名                                                  ##
##         STANDBY:备节点主机名                                                     ##
##         GP_PWD:数据库用户gpadmin的密码                                           ##
##         GP_HOME:gpadmin用户主目录                                                ##
##         EXP_FILE:临时expect脚本文件(用于ssh远程登陆,自动输入等待确认信息)        ##
##function:                                                                         ##
##         setKey:设置gpadmin用户下，各节点间的信任关系，实现免密码登录             ##
##         copyGp:从主节点出，将gp数据库安装文件复制至其他节点                      ##
##         setBashrc:设置gpadmin用户环境变量                                        ##
##         createDir:创建数据库共享目录                                             ##
##         gpinitsystem_config:数据库初始化配置                                     ##
##         mian:程序入口                                                            ##
######################################################################################
set -x

RUN_PATH=$(cd $(dirname $0);pwd)
. ${RUN_PATH}/common.sh
PGPORT=$1
PGDATABASE=$2
STANDBY=$3
GP_PWD=$4
HOST=$(echo $*|awk -F ',' '{print $2}')
GP_HOME="/home/gpadmin"
EXP_FILE="${GP_HOME}/temp.exp"
[ "X$(echo ~)" == "X${GP_HOME}" ] || exit 1
cd ${GP_HOME}
CLUSTER_STATUS="none"

function setKey()
{
    source /opt/greenplum/greenplum-db/greenplum_path.sh
    [ -f ${EXP_FILE} ] && rm -rf ${EXP_FILE}
    echo '#!/usr/bin/expect
set timeout 5
spawn gpssh-exkeys -f /home/gpadmin/conf/hostlist
expect "Enter password"
send "[lindex $argv 0]\n"
expect "Enter password"
exit 0'>${EXP_FILE}
    chmod +x ${EXP_FILE}
    ${EXP_FILE} ${GP_PWD} >/dev/null 2>&1
    local ret=$?
    rm -rf ${EXP_FILE}
    return ${ret}
}

function copyGp()
{
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r /opt/greenplum/greenplum-db-* ${i}:/opt/greenplum/
        fi
    done
    [ -f ${EXP_FILE} ] && rm -rf ${EXP_FILE}
    echo '#!/usr/bin/expect
set timeout 5
spawn gpssh -f /home/gpadmin/conf/hostlist
sleep 6
expect "=>"
send "pwd\n"
expect "=>"
send "cd /opt/greenplum;ln -s ./greenplum-db-* greenplum-db\n"
expect "=>"
send "exit\n"
expect "=>"
exit 0'>${EXP_FILE}
    chmod +x ${EXP_FILE}
    ${EXP_FILE} >/dev/null 2>&1
    local ret=$?
    rm -rf ${EXP_FILE}
    return ${ret}
}

function setBashrc()
{
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        local file="${GP_HOME}/.bash_profile"
        echo "source /opt/greenplum/greenplum-db/greenplum_path.sh" >/dev/null 2>&1 >> ${file}
        echo "export MASTER_DATA_DIRECTORY=/home/gpadmin/gpdata/gpmaster/gpseg-1" >/dev/null 2>&1 >> ${file}
        echo "export PGPORT=${PGPORT}" >/dev/null 2>&1 >> ${file}
        echo "export PGDATABASE=${PGDATABASE}" >/dev/null 2>&1 >> ${file}
        source ${file}
        echo "in_the_cluster=no" >/dev/null 2>&1 > /opt/cluster_status.conf
        CLUSTER_STATUS[0]="no"
    fi
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp ${file} ${i}:${file}
            ssh ${i} source ${file}
            ssh ${i} 'echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="yes"
        fi
    done
    return 0
}

function createDir()
{
    [ -f ${EXP_FILE} ] && rm -rf ${EXP_FILE}
    echo '#!/usr/bin/expect
set timeout 5
spawn gpssh -f /home/gpadmin/conf/hostlist
sleep 6
expect "=>"
send "pwd\n"
expect "=>"
send "cd /home/gpadmin;mkdir gpdata;cd gpdata;mkdir gpmaster gpdatap1 gpdatap2 gpdatam1 gpdatam2\n"
expect "=>"
send "exit\n"
expect "=>"
exit 0'>${EXP_FILE}
    chmod +x ${EXP_FILE}
    ${EXP_FILE} >/dev/null 2>&1
    local ret=$?
    rm -rf ${EXP_FILE}
    return ${ret}
}

function gpinitsystem_config()
{
    local file="/home/gpadmin/conf/gpinitsystem_config"
    touch ${file}
    echo 'ARRAY_NAME="Greenplum"
SEG_PREFIX=gpseg
PORT_BASE=33000
declare -a DATA_DIRECTORY=(/home/gpadmin/gpdata/gpdatap1  /home/gpadmin/gpdata/gpdatap2)' >${file}
    echo "MASTER_HOSTNAME=$(hostname)
MASTER_DIRECTORY=/home/gpadmin/gpdata/gpmaster
MASTER_PORT=${PGPORT}
TRUSTED_SHELL=/usr/bin/ssh
MIRROR_PORT_BASE=43000
REPLICATION_PORT_BASE=34000
MIRROR_REPLICATION_PORT_BASE=44000
declare -a MIRROR_DATA_DIRECTORY=(/home/gpadmin/gpdata/gpdatam1 /home/gpadmin/gpdata/gpdatam2)
MACHINE_LIST_FILE=/home/gpadmin/conf/seg_hosts" >>${file}
    echo "hot_standby=on">>/home/gpadmin/gpdata/gpmaster/gpseg-1/postgresql.conf
    [ -f ${EXP_FILE} ] && rm -rf ${EXP_FILE}
    echo -n '#!/usr/bin/expect
set timeout 3600
spawn gpinitsystem -c /home/gpadmin/conf/gpinitsystem_config -s '>${EXP_FILE}
echo "${STANDBY}">>${EXP_FILE}
echo 'expect {
    "Continue with Greenplum creation Yy/Nn>" {send "Y\n"}
}
expect {
    "Yy/Nn>" {send "Y\n"}
}
exit 0'>>${EXP_FILE}
    chmod +x ${EXP_FILE}
    rm -rf /opt/greenplum/greenplum-db-yuyi/greenplum-db-yuyi
    ${EXP_FILE}
    [ -f ${EXP_FILE} ] && rm -rf ${EXP_FILE}
    gpstart -a
    echo -n '#!/usr/bin/expect
set timeout 5000
spawn psql -d postgres
expect "=#"
send "\n"
expect "=#"
send "alter role gpadmin with password '>${EXP_FILE}
    echo -n "'[lindex ">>${EXP_FILE}
    echo -n '$argv 0]'>>${EXP_FILE}
    echo -n "'">>${EXP_FILE}
    echo ';\n"
expect "=#"
send "\\q\n"
expect "=#"
exit 0'>>${EXP_FILE}
    chmod +x ${EXP_FILE}
    ${EXP_FILE} ${GP_PWD} >/dev/null 2>&1
    local ret=$?
    rm -rf ${EXP_FILE}
    return ${ret}
    return 0
}

function createSql()
{
    [ -f ${EXP_FILE} ] && rm -rf ${EXP_FILE}
    echo -n '#!/usr/bin/expect
set timeout 5000
spawn psql -d postgres
expect "=#"
send "\n"
expect "=#"
send "create database '>${EXP_FILE}
    echo -n "${PGDATABASE}">>${EXP_FILE}
    echo -n ';\n"
expect "=#"
send "\\c '>>${EXP_FILE}
    echo -n "${PGDATABASE}">>${EXP_FILE}
    echo ';\n"
expect "=#"
send "\\q\n"
expect "=#"
exit 0
'>>${EXP_FILE}
    chmod +x ${EXP_FILE}
    ${EXP_FILE} >/dev/null 2>&1
    local ret=$?
    rm -rf ${EXP_FILE}
    return ${ret}
}

function main()
{
    setKey
    [ $? -ne 0 ] && return 1
    
    let nu=0
    local hosts=(${HOST})
    getClusterStatus
    
    copyGp
    [ $? -ne 0 ] && return 1
    setBashrc
    [ $? -ne 0 ] && return 1
    if [ "X${CLUSTER_STATUS[0]}" == "Xno" ];then
        createDir
        [ $? -ne 0 ] && return 1
        gpinitsystem_config
        [ $? -ne 0 ] && return 1
        createSql
        [ $? -ne 0 ] && return 1
        echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf
        CLUSTER_STATUS[0]="yes"
    fi
    return 0
}

main
exit $?
