#!/bin/bash

HDFS_ROOT_PWD=<HDFS_ROOT_PWD>
HDFS_IP=<HDFS_IP>
HOST="$(cat /opt/hostList|awk -F ',' '{print $1}')"
RUN_PATH="$(cd $(dirname $0);pwd)"
##-open 
##-close
OPTION=$1

function makeExp()
{
    local sendPwd=$1
    local runPwd=$2
    rm -rf ${sendPwd} ${runPwd}
    touch ${sendPwd} ${runPwd}
    echo '#!/usr/bin/expect
set timeout 3600
set password [lindex $argv 0]
spawn scp -P 22 -r [lindex $argv 1] [lindex $argv 2]
expect {
 "(yes/no)?" {
   send "yes\n"
   expect "*assword:" { send "$password\n"}
  }
  "*assword:" {
   send "$password\n"
  }
}
expect "100%"
expect eof
exit 0
'>>${sendPwd}
    echo '#!/usr/bin/expect
set timeout 3600
set passwd [lindex $argv 0]
spawn [lindex $argv 1] [lindex $argv 2] [lindex $argv 3] [lindex $argv 4] [lindex $argv 5] [lindex $argv 6] [lindex $argv 7] [lindex $argv 8] [lindex $argv 9]
expect {
    "yes/no" {send "yes\r"}
    "*assword:" {send "$passwd\r"}
}
expect {
    "*assword:" {send "$passwd\r"}
}
exit 0'>>${runPwd}
    chmod +x ${sendPwd} ${runPwd}
    return 0
}

function getAllHostIp()
{
    local runPwd=$1
    local pwd=$2
    local ip=$3

    local cmd="cat /opt/hostList|awk -F ',' '{print "'$1}'"'"
    local all_ip=$(${runPwd} ${pwd} ssh ${ip} "${cmd}")

    [ $? -ne 0 ] && return 1
    local temp="${RUN_PATH}/temp.temp"
    [ -f ${temp} ] && rm -rf ${temp}
    touch ${temp}
    echo ${all_ip}|awk -F 'assword:' '{print $NF}' > ${temp}
    all_ip=$(cat -v ${temp}|tr -d '^M'|awk 'gsub(/^ *| *$/,"")')
    rm -rf ${temp}
    echo ${all_ip}
    return 0
}

function open()
{

    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        ssh ${i} "bash /opt/service/autoMount.sh"
        [ $? -ne 0 ] && return 1
    done
}

function close()
{
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        local nu=$(ssh ${i} "mount -l -t nfs |grep /var/ftp |wc -l")
        for((j=0;j<${nu};j++));do
            ssh ${i} "umount /var/ftp"
            if [ $? -ne 0 ];then
                sleep 3s
                fuser -km /var/ftp/
                ssh ${i} "umount -f /var/ftp"
            fi
        done
    done
}

function main()
{
    local hosts=(${HOST})
    if [ "X${OPTION}" == "X-open" ];then
        for i in ${hosts[@]}; do
            ssh ${i} "echo 'open' > /opt/service/mountStat.txt"
        done
        open
        [ $? -ne 0 ] && return 1
    elif [ "X${OPTION}" == "X-close" ];then
        for i in ${hosts[@]}; do
            ssh ${i} "echo 'close' > /opt/service/mountStat.txt"
        done
        close
        [ $? -ne 0 ] && return 1
    else
        echo "Usage: setnodes [OPTION]
Shell options:
       -open
       -close"
       return 1
    fi
    return 0
}

main
exit $?