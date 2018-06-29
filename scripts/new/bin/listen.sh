#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
SENDPWD="${RUN_PATH}/sendPwd.exp"
RUNPWD="${RUN_PATH}/runPwd.exp"
CONFIG_FILE="/opt/Core_service_password.csv"
HOST=""
ALL_HOSTS=""

###集群名称,服务Ip,当前主节点IP,主备切换次数,root用户口令,服务用户名,服务用户口令,  ---Core_service_password.csv
###集群名称,节点类型,所在虚拟机名称,虚拟机IP,浮动IP,服务状态,所在服务器, ---${CONFIG_DIR}/status.csv
###HOST="ip1,pwd ip2,pwd ip3,pwd3"
function getHosts()
{

    SERVICE_LIST=$(grep -v "^#" /opt/service/conf.conf |grep "^cluster_pkg"|awk -F '=' '{print $2}')
    SERVICE_LIST=(${SERVICE_LIST})
    for i in ${SERVICE_LIST[@]}; do
        local cluster=$(echo ${i}|awk -F ':' '{print $1}')
        local ip=$(grep "^${cluster}" ${CONFIG_FILE}|awk -F "," '{print $3}'|awk 'gsub(/^ *| *$/,"")')
        local pwd=$(grep "^${cluster}" ${CONFIG_FILE}|awk -F "," '{print $5}'|awk 'gsub(/^ *| *$/,"")')
        HOST="${HOST} "$(grep "^${cluster}" /opt/status.csv|awk -v g=${pwd} -F ',' '{printf $4;printf ",";print g}'|awk 'gsub(/^ *| *$/,"")')
        ALL_HOSTS="${ALL_HOSTS} ${ip},${pwd}"
    done
    return 0
}

function getVmStat()
{
    makeExp ${SENDPWD} ${RUNPWD}
    local ip=""
    local pwd=""
    local hosts=(${HOST})
    
    for i in ${hosts[@]}; do
        {
        ip=$(echo ${i}|awk -F "," '{print $1}')
        pwd=$(echo ${i}|awk -F "," '{print $2}')
        ${RUNPWD} ${pwd} ssh ${ip} "hostname"
        
        cpu=$(${RUNPWD} ${pwd} ssh ${ip} 'awk -v a=100 -v b=$(top -bn 1 -i -c|grep Cpu|awk '\''{print $5}'\''|awk -F '\''%'\'' '\''{print $1}'\'') '\''BEGIN{printf a-b}'\'''|tail -1)
        mem=$(${RUNPWD} ${pwd} ssh ${ip} 'free -m |grep '\''Mem'\''|awk '\''{printf("%0.2f",$3/$2*100)}'\'''|tail -1)
        disk=$(${RUNPWD} ${pwd} ssh ${ip} 'df |sed -n '\''/\/$/p'\''|gawk '\''{printf $5}'\''|sed '\''s/%//'\'''|tail -1)
        echo "${cpu}%,${mem}%,${disk}%,">/tmp/${ip}_status.log
        } &
    done
    wait
    return 0
}

function checkApi()
{
    local num=$(ps -ef |grep "java -jar"|grep "system-api"|wc -l)
    if [ ${num} -eq 0 ];then
        java -jar /opt/service/system-api*.jar >> /opt/service/system-api.out  2>&1 &
    fi
    return 0
}

###服务名称 所在虚拟机名称 虚拟机IP 浮动IP 服务状态 CPU占有率 内存占有率 硬盘占有率 所在服务器
###集群名称,所在虚拟机名称,虚拟机IP,浮动IP,服务状态,所在服务器, ---status.sh
###集群名称,所在虚拟机名称,虚拟机IP,浮动IP,服务状态,所在服务器,CPU占有率,内存占有率,硬盘占有率,,

###new：集群名称,节点类型,所在虚拟机名称,虚拟机IP,浮动IP,服务状态,所在服务器名,所在服务器IP, ---status.sh
###new：集群名称,节点类型,所在虚拟机名称,虚拟机IP,浮动IP,服务状态,所在服务器名,所在服务器IP,CPU占有率,内存占有率,硬盘占有率,,

###/var/spool/cron/username（定时任务的用户名，如root）
###*/1 * * * * /root/c.sh  每分钟会执行一次c.sh脚本
###crontab -l查询当前用户所有定时任务


function sendCore()
{
    local ip=""
    local pwd=""
    local hosts=(${ALL_HOSTS})
    for i in ${hosts[@]}; do
        ip=$(echo ${i}|awk -F "," '{print $1}')
        pwd=$(echo ${i}|awk -F "," '{print $2}')
        ${RUNPWD} ${pwd} ssh ${ip} "hostname"
        ${SENDPWD} ${pwd} ${CONFIG_FILE} ${ip}:/opt/ >/dev/null 2>&1
        ${SENDPWD} ${pwd} /opt/sys_status.csv ${ip}:/opt/ >/dev/null 2>&1
        ${RUNPWD} ${pwd} ssh ${ip} "bash /opt/sendCore.sh /opt/Core_service_password.csv /opt/"
        ${RUNPWD} ${pwd} ssh ${ip} "bash /opt/sendCore.sh /opt/sys_status.csv /opt/"
    done
    return 0
}

function localHost()
{
    cpu=$(awk -v a=100 -v b=$(top -bn 1 -i -c|grep Cpu|awk '{print $5}'|awk -F '%' '{print $1}') 'BEGIN{printf a-b}'|tail -1)
    mem=$(free -m |grep 'Mem'|awk '{printf("%0.2f",$3/$2*100)}'|tail -1)
    disk=$(df |sed -n '/\/$/p'|gawk '{printf $5}'|sed 's/%//'|tail -1)
    
    local str=$(awk -F ',' '{printf $1;printf ",,0,";printf $2;printf ",";printf $3}' /opt/hostList)
    echo "SystemManagement,Master,$(hostname),${str},${cpu}%,${mem}%,${disk}%," >> /opt/sys_status.csv
    return 0
}

function main()
{
    makeExp ${SENDPWD} ${RUNPWD}
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    getHosts
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    bash /tmp/sys_install/bin/status.sh
    ###返回格式需修改，可能会有password： ^M等字样
    getVmStat
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    
    local hosts=(${HOST})
    local ip=""
    local temp="${RUN_PATH}/sys_status.csv"
    [ -f ${temp} ] && rm -rf ${temp}
    touch ${temp}
    for i in ${hosts[@]}; do
        ip=$(echo ${i}|awk -F "," '{print $1}')
        echo "$(grep -w ${ip} /opt/status.csv|awk -F ';' '{print $1}')$(cat /tmp/${ip}_status.log)" >>${temp}
    done
    mv -f ${temp} /opt/sys_status.csv
    localHost
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    sendCore
    [ $? -ne 0 ] && rm -rf ${SENDPWD} ${RUNPWD} && return 1
    return 0
}

main
exit $?