#!/bin/bash

######################################################################################
##input:                                                                            ##
##      null                                                                        ##
##out/return:                                                                       ##
##           零为成功,非零为失败(通常为1)                                           ##
##variable:                                                                         ##
##         RUN_PATH:当前脚本所在路径                                                ##
##         CONFIG_FILE:配置文件                                                     ##
##         LOG_FILE:日志文件                                                        ##
##         ROOT_PWD=root用户密码                                                    ##
##         GP_PWD:数据库用户gpadmin的密码                                           ##
##         MASTER:主节点主机名                                                      ##
##         STANDBY:备节点主机名                                                     ##
##         HOST:数据节点主机名                                                      ##
##         PGPORT:数据库访问端口号                                                  ##
##         PGDATABASE:数据库实例名                                                  ##
##function:                                                                         ##
##         getConfig:读取配置文件,并设置一些环境变量                                ##
##         envSet:配置内核参数hosts等配置信息                                       ##
##         setConfig:gp数据库配置文件设置                                           ##
##         installGp:安装数据库                                                     ##
##         openClient:客户端访问设置                                                ##
##         mian:程序入口                                                            ##
##import:                                                                           ##
##      addGpUser.sh:创建gpadmin用户及用户组脚本                                    ##
##      gpDbConfig.sh:完成其他节点的数据库安装及数据库配置(必须使用gpadmin用户执行) ##
##      common.sh:公共函数库                                                        ##
##               注：需自定义日志文件变量${LOG_FILE}和配置文件变量${CONFIG_FILE}    ##
##               function:                                                          ##
##                       log_echo:输出日志                                          ##
##                               $1:日志等级,INFO:一般,WARNING:关注,ERROR:错误      ##
##                               $2:日志具体内容                                    ##
##                               $3:产生日志所在函数                                ##
##                               $4:产生日志所在行号(可有可无),系统自带当前行号环境 ##
##                                  变量:${LINENO}                                  ##
##                          日志格式:年-月-日 时:分:秒 [行号] 用户:函数-[级别]:-内容##
##                       log_info:输出普通日志                                      ##
##                               $1:日志具体内容                                    ##
##                               $2:产生日志所在行号(可有可无)                      ##
##                       注:log_warning和log_error的传参与log_info一样              ##
##                       log_warning:输出注意日志                                   ##
##                       log_error:输出错误日志                                     ##
##                       getValue:获取配置文件内容,参数$1为条件                     ##
######################################################################################

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
CONFIG_FILE="${RUN_PATH}/../config/config.conf"
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/installGreenplum_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
CLUSTER_STATUS="none"

function getConfig()
{
    log_info "getConfig begin" ${LINENO}
    [ -f ${CONFIG_FILE} ] || log_error "File ${CONFIG_FILE} not found" ${LINENO}
    [ $? -ne 0 ] && return 1
    ROOT_PWD=$(getValue "root_password")
    GP_PWD=$(getValue "gp_user_password")
    STANDBY=$(getValue "standby_host" |awk '{print $2}')
    STANDBY_IP=$(getValue "standby_host" |awk '{print $1}')
    HOST=$(getValue "data_host" |awk '{print $2}')
    
    HOST=$(echo ${STANDBY} ${HOST})
    
    ls /opt/cluster_status.conf >/dev/null 2>&1
    if [ $? -eq 0 ];then
        CLUSTER_STATUS=$(grep "^in_the_cluster" /opt/cluster_status.conf |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")')
    fi
    setHosts
    [ $? -ne 0 ] && return 1
    let nu=0
    local hosts=(${HOST})
    getClusterStatus
    
    PGPORT=$(getValue "PGPORT")
    PGDATABASE=$(getValue "PGDATABASE")
    log_info "getConfig end" ${LINENO}
    return 0
}

function typeFlag()
{

    local name=$(cat /dev/urandom |head -c 5 |md5sum |head -c 8)
    echo "${name}" > ${RUN_PATH}/onName.txt
    local num=$(grep -n "^ON_NAME=" ${RUN_PATH}/autoFloatIP.sh|awk -F ':' '{print $1}')
    sed -i "${num}d" ${RUN_PATH}/autoFloatIP.sh
    let num=num-1
    sed -i "${num}a ON_NAME=${name}" ${RUN_PATH}/autoFloatIP.sh

    local server_ip=$(getValue "server_ip")
    sed -i "s|<SERVER_IP>|${server_ip}|g" "${RUN_PATH}/status.sh"
    mkdir -p /opt/service
    yes|cp ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${RUN_PATH}/onName.txt ${RUN_PATH}/on.sh /opt/service/
    
    echo "Master" > /opt/typeFlag
    local nu=$(grep -n "su - gpadmin -c 'bash /opt/service/on.sh" /etc/rc.d/rc.local |awk -F ":" '{print $1}')
    nu=(${nu})
    let len=${#nu[@]}-1
    for((i=${len};i>=0;i--)) do
        sed -i "${nu[${i}]}d" /etc/rc.d/rc.local
    done
    echo "bash /opt/service/on.sh" >> /etc/rc.d/rc.local
    local tempIp=$(getValue "standby_host" |awk '{print $1}')
    ssh ${tempIp} "echo 'Slave' > /opt/typeFlag"
    nu=$(ssh ${tempIp} "grep -n \"bash /opt/service/on.sh\" /etc/rc.d/rc.local"|awk -F ':' '{print $1}')
    nu=(${nu})
    let len=${#nu[@]}-1
    for((i=${len};i>=0;i--)) do
        ssh ${tempIp} "sed -i '${nu[${i}]}d' /etc/rc.d/rc.local"
    done
    ssh ${tempIp} "echo \"bash /opt/service/on.sh\" >> /etc/rc.d/rc.local"
    ssh ${tempIp} "mkdir -p /opt/service"
    scp -r ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${RUN_PATH}/onName.txt ${RUN_PATH}/on.sh ${tempIp}:/opt/service/
    
    echo "export PGOPTIONS='-c gp_session_role=utility'" >> /opt/greenplum/greenplum-db/greenplum_path.sh
    
    tempIp=$(getValue "data_host" |awk '{print $1}')
    tempIp=(${tempIp})
    for i in ${tempIp[@]}; do
        ssh ${i} "echo \"export PGOPTIONS='-c gp_session_role=utility'\" >> /opt/greenplum/greenplum-db/greenplum_path.sh"
        ssh ${i} "echo 'Data' > /opt/typeFlag"
        ssh ${i} "mkdir -p /opt/service"
        scp -r ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${RUN_PATH}/onName.txt ${RUN_PATH}/on.sh ${i}:/opt/service/
    done
    
    
    return 0
}

function makeGpUser()
{
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        bash ${RUN_PATH}/addUser.sh ${GP_PWD} gpadmin 530
        mkdir /opt/greenplum
        chown -R gpadmin:gpadmin /opt/greenplum
        [ $? -ne 0 ] && return 1
    fi
    
    
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp ${RUN_PATH}/addUser.sh ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} bash /tmp/addUser.sh ${GP_PWD} gpadmin 530
            ssh ${i} rm -rf /tmp/addUser.sh
            ssh ${i} mkdir /opt/greenplum
            ssh ${i} chown -R gpadmin:gpadmin /opt/greenplum
        fi
    done
    return 0
}


function envSet()
{
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        log_info "set /etc/security/limits.conf" ${LINENO}
        local file="${RUN_PATH}/../config/limits.conf"
        [ -f ${file} ] || log_error "File ${file} not found" ${LINENO}
        [ $? -ne 0 ] && return 1
        cat ${file} >/dev/null 2>&1 >> /etc/security/limits.conf

        log_info "set /etc/sysctl.conf" ${LINENO}
        file="${RUN_PATH}/../config/sysctl.conf"
        [ -f ${file} ] || log_error "File ${file} not found" ${LINENO}
        [ $? -ne 0 ] && return 1
        cat ${file} >/dev/null 2>&1 >> /etc/sysctl.conf
        sysctl -p
    fi
    
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp /etc/security/limits.conf ${i}:/etc/security/ >/dev/null 2>&1
            scp /etc/sysctl.conf ${i}:/etc/ >/dev/null 2>&1
            ssh ${i} sysctl -p >/dev/null 2>&1
        fi
    done
    makeGpUser
    [ $? -ne 0 ] && return 1
    return 0
}
function setConfig()
{
    local file="/home/gpadmin/conf/hostlist"
    mkdir -p /home/gpadmin/conf
    touch ${file}
    grep "_host" ${CONFIG_FILE} |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")'|awk '{print $2}'  >/dev/null 2>&1 >> ${file}
    file="/home/gpadmin/conf/seg_hosts"
    [ -f ${file} ] && rm -rf ${file}
    touch ${file}
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        echo ${i} >/dev/null 2>&1 >> ${file}
    done
    chown -R gpadmin:gpadmin /home/gpadmin
    return 0
}

function installGp()
{
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        local file="${RUN_PATH}/../file/greenplum-db.tar.gz"
        [ -f ${file} ] || log_error "File ${file} not found" ${LINENO}
        [ $? -ne 0 ] && return 1
        tar -zxf ${file} -C /opt/greenplum
        chown -R gpadmin:gpadmin /opt/greenplum
        mv -f ${RUN_PATH}/../config/hostList /opt/
        chown -R gpadmin:gpadmin /opt/hostList
        echo "in_the_cluster=none" >/dev/null 2>&1 > /opt/cluster_status.conf
        chown -R gpadmin:gpadmin /opt/cluster_status.conf
    fi
    
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r /opt/hostList ${i}:/opt
            ssh ${i} chown -R gpadmin:gpadmin /opt/hostList
            ssh ${i} 'echo "in_the_cluster=none" >/dev/null 2>&1 > /opt/cluster_status.conf'
            ssh ${i} chown -R gpadmin:gpadmin /opt/cluster_status.conf
        fi
    done
    chown -R gpadmin:gpadmin /home/gpadmin
    return 0
}

function openClient()
{
    echo "host     all         gpadmin         0.0.0.0/0         md5">>/home/gpadmin/gpdata/gpmaster/gpseg-1/pg_hba.conf
    echo "hot_standby=on">>/home/gpadmin/gpdata/gpmaster/gpseg-1/postgresql.conf
    scp -r /home/gpadmin/gpdata/gpmaster/gpseg-1/pg_hba.conf ${STANDBY}:/home/gpadmin/gpdata/gpmaster/gpseg-1/
    scp -r /home/gpadmin/gpdata/gpmaster/gpseg-1/postgresql.conf ${STANDBY}:/home/gpadmin/gpdata/gpmaster/gpseg-1/
    su - gpadmin -c "gpstop -a"
    su - gpadmin -c "gpstart -a"
    return 0
}

function main()
{
    getConfig
    [ $? -ne 0 ] && return 1
    envSet
    [ $? -ne 0 ] && return 1
    setConfig
    [ $? -ne 0 ] && return 1
    installGp
    [ $? -ne 0 ] && return 1
    chown gpadmin:gpadmin ${RUN_PATH}/gpDbConfig.sh
    chown gpadmin:gpadmin ${RUN_PATH}/common.sh
    chmod +x ${RUN_PATH}/gpDbConfig.sh
    local hosts=(${HOST})
    hosts=${hosts[@]}
    su - gpadmin -c "bash ${RUN_PATH}/gpDbConfig.sh ${PGPORT} ${PGDATABASE} ${STANDBY} ${GP_PWD} , ${hosts}"
    [ $? -ne 0 ] && return 1
    openClient
    [ $? -ne 0 ] && return 1
    log_info "greenplum install successfully!"
    typeFlag
    [ $? -ne 0 ] && return 1
    setServerIp ${STANDBY_IP}
    [ $? -ne 0 ] && return 1
    
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?

