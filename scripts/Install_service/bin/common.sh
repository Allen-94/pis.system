#!/bin/bash

##引用时需自定义日志文件环境变量${LOG_FILE}和配置文件环境变量${CONFIG_FILE}

function log_echo()
{
    if [ "X$4" == "X" ];then
        echo "$(date '+%Y-%m-%d %H:%M:%S') $(whoami):$3-[$1]:-$2"
    else
        echo "$(date '+%Y-%m-%d %H:%M:%S') [$4] $(whoami):$3-[$1]:-$2"
    fi
    return 0
}

function log_info()
{
    log_echo "INFO" "$1" "${FUNCNAME[1]}" "$2" |tee -a ${LOG_FILE}
    return 0
}

function log_warning()
{
    log_echo "WARNING" "$1" "${FUNCNAME[1]}" "$2" |tee -a ${LOG_FILE}
    return 0
}

function log_error()
{
    log_echo "ERROR" "$1" "${FUNCNAME[1]}" "$2" |tee -a ${LOG_FILE}
    return 1
}

function getValue()
{
    [ "X$1" == "X" ] &&  return 1
    grep "^$1" ${CONFIG_FILE} |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")'
    return 0
}

function setHosts()
{
    grep "_host" ${CONFIG_FILE} |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")' >> /etc/hosts
    bash ${RUN_PATH}/makeSshKey.sh ${ROOT_PWD} , ${HOST}
    [ $? -ne 0 ] && return 1
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        scp /etc/hosts ${i}:/etc/ >/dev/null 2>&1
    done
    return 0
}

function setAllHosts()
{
    HOST=${ALL_HOST}
    rm -rf ~/.ssh
    setHosts
    [ $? -ne 0 ] && return 1
    return 0
}

function setServerIp()
{
    local other_ip=$1
    local server_ip=$(getValue "server_ip")
    local ethernet_port=$(getValue "ethernet_port")
    ifconfig ${ethernet_port} ${server_ip} up
    [ $? -ne 0 ] && return 1
    
    cp ${RUN_PATH}/autoFloatIP.sh /root
    sed -i "s|<SERVER_IP>|${server_ip}|g" /root/autoFloatIP.sh
    sed -i "s|<ETH>|${ethernet_port}|g" /root/autoFloatIP.sh
    
    echo "* * * * * bash -x /root/autoFloatIP.sh
* * * * * sleep 15; bash -x /root/autoFloatIP.sh
* * * * * sleep 30; bash -x /root/autoFloatIP.sh
* * * * * sleep 45; bash -x /root/autoFloatIP.sh">>/var/spool/cron/root

    scp /root/autoFloatIP.sh ${other_ip}:/root
    scp /var/spool/cron/root ${other_ip}:/var/spool/cron/
    
    return 0
}

function getClusterStatus()
{
    for i in ${hosts[@]}; do
        let nu=nu+1
        ssh ${i} ls /opt/cluster_status.conf >/dev/null 2>&1
        if [ $? -eq 0 ];then
            local temp=$(ssh ${i} grep "^in_the_cluster" /opt/cluster_status.conf |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")')
            if [ "X${temp}" == "Xyes" ] || [ "X${temp}" == "Xno" ];then
                CLUSTER_STATUS[${nu}]="${temp}"
            else
                CLUSTER_STATUS[${nu}]="none"
            fi
        else
            CLUSTER_STATUS[${nu}]="none"
        fi
    done
}

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

function getHostList()
{
    local hostList=$1
    local all_ip=$(echo $*|awk -F 'x' '{print $2}')
    [ -f ${hostList} ] && rm -rf ${hostList}
    touch ${hostList}
    local hosts=(${all_ip})
    for i in ${hosts[@]}; do
        echo "${i}" >>${hostList}
    done
    return 0
}

#加密方法  //使用base64加密3次
function encoder()
{
    local pwd=$1
    for((i=0;i<3;i++));do
        pwd=$(echo ${pwd}|base64)
    done
    echo ${pwd}
    return 0
}

#解密方法  //使用base64解密3次
function decoder()
{
    local pwd=$1
    for((i=0;i<3;i++));do
        pwd=$(echo ${pwd}|base64 -d)
    done
    echo ${pwd}
    return 0
}

#系统管理api放置
function systemApi()
{
    local path=$1
    local hosts=$2
    for i in ${hosts[@]}; do
        scp ${path}/system-api.cpp ${i}:/usr/include/c++/4.4.4/
        scp ${path}/runtimeLogs.sh  ${i}:/opt/
        ssh ${i} "chmod +x /usr/include/c++/4.4.4/system-api.cpp"
        ssh ${i} "chmod +x /opt/runtimeLogs.sh"
    done
    return 0
}
