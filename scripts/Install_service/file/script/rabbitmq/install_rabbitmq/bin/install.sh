#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/installRabbitmq_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
CONFIG_FILE="${RUN_PATH}/../config/config.conf"
FILE_PATH="${RUN_PATH}/../file"
CONF_DIR="${RUN_PATH}/../config/"
CLUSTER_STATUS="none"

function getConfig()
{
    USER_NAME=$(getValue "rabbitmq_user_name")
    USER_PWD=$(getValue "rabbitmq_user_password")
    ROOT_PWD=$(getValue "root_password")
    HOST=$(getValue "other_host" |awk '{print $2}')
    ls /opt/cluster_status.conf >/dev/null 2>&1
    if [ $? -eq 0 ];then
        CLUSTER_STATUS=$(grep "^in_the_cluster" /opt/cluster_status.conf |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")')
    fi
    setHosts
    [ $? -ne 0 ] && return 1
    let nu=0
    local hosts=(${HOST})
    getClusterStatus
    return 0
}

function installErlang()
{
    tar -zxf ${FILE_PATH}/otp_src*.tar.gz -C /tmp
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        {
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r /tmp/otp_src* ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} "cd /tmp/otp_src*;./configure --prefix=/opt/erlang >/dev/null 2>&1"
            [ $? -ne 0 ] && rm -rf /tmp/otp_src* && ssh ${i} rm -rf /tmp/otp_src* && return 1
            ssh ${i} "cd /tmp/otp_src*;make >/dev/null 2>&1 && make install >/dev/null 2>&1"
            [ $? -ne 0 ] && rm -rf /tmp/otp_src* && ssh ${i} rm -rf /tmp/otp_src* && return 1
            ssh ${i} rm -rf /tmp/otp_src*
            scp /etc/profile ${i}:/etc/ >/dev/null 2>&1
            ssh ${i} source /etc/profile
        fi
        } &
    done
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        cd /tmp/otp_src*
        ./configure --prefix=/opt/erlang >/dev/null 2>&1
        [ $? -ne 0 ] && rm -rf /tmp/otp_src* && return 1
        make >/dev/null 2>&1 && make install >/dev/null 2>&1
        [ $? -ne 0 ] && rm -rf /tmp/otp_src* && return 1
        cd - >/dev/null 2>&1
        echo '#set erlang environment
export ERL_HOME=/opt/erlang
export PATH=$PATH:${ERL_HOME}/bin' >> /etc/profile
        source /etc/profile
    fi
    wait
    rm -rf /tmp/otp_src*
    return 0
}

function installSimplejson()
{
    tar -zxf ${FILE_PATH}/simplejson*.tar.gz -C /tmp
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        {
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r /tmp/simplejson* ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} "cd /tmp/simplejson*;python setup.py install >/dev/null 2>&1"
            [ $? -ne 0 ] && rm -rf /tmp/simplejson* && ssh ${i} rm -rf /tmp/simplejson* && return 1
            ssh ${i} rm -rf /tmp/simplejson*
        fi
        } &
    done
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        cd /tmp/simplejson*
        python setup.py install >/dev/null 2>&1
        [ $? -ne 0 ] && rm -rf /tmp/simplejson* && return 1
        cd - >/dev/null 2>&1
    fi
    wait
    rm -rf /tmp/simplejson*
    return 0
}

function installRabbitmq()
{
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        {
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r ${FILE_PATH}/rabbitmq-server-*.noarch.rpm ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} rpm -i --nodeps /tmp/rabbitmq-server-*.noarch.rpm
            ssh ${i} rm -rf /tmp/rabbitmq-server-*.noarch.rpm
        fi
        } &
    done
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        rpm -i --nodeps ${FILE_PATH}/rabbitmq-server-*.noarch.rpm
    fi
    wait
    mv -f ${RUN_PATH}/../config/hostList /opt/
    for i in ${hosts[@]}; do
        scp -r /opt/hostList ${i}:/opt
    done
    return 0
}

function setRabbitmq()
{
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        {
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            ssh ${i} "source /etc/profile;rabbitmq-plugins enable rabbitmq_management"
            [ $? -ne 0 ] && return 1
            ssh ${i} "source /etc/profile;rabbitmq-plugins enable rabbitmq_stomp"
            [ $? -ne 0 ] && return 1
            ssh ${i} "source /etc/profile;rabbitmq-server -detached"
            [ $? -ne 0 ] && return 1
            sleep 6s
            ssh ${i} "source /etc/profile;rabbitmqctl add_user ${USER_NAME} ${USER_PWD}"
            [ $? -ne 0 ] && return 1
            ssh ${i} "source /etc/profile;rabbitmqctl set_user_tags ${USER_NAME} administrator"
            [ $? -ne 0 ] && return 1
            ssh ${i} "source /etc/profile;rabbitmqctl delete_user guest >/dev/null 2>&1"
            [ $? -ne 0 ] && return 1
            ssh ${i} "source /etc/profile;rabbitmqctl set_permissions -p / ${USER_NAME} '.*' '.*' '.*'"
            [ $? -ne 0 ] && return 1
            ssh ${i} 'echo "in_the_cluster=no" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="no"
        fi
        }
    done
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        rabbitmq-plugins enable rabbitmq_management
        [ $? -ne 0 ] && return 1
        rabbitmq-plugins enable rabbitmq_stomp
        rabbitmq-server -detached
        [ $? -ne 0 ] && return 1
        sleep 6s
        rabbitmqctl add_user ${USER_NAME} ${USER_PWD}
        [ $? -ne 0 ] && return 1
        rabbitmqctl set_user_tags ${USER_NAME} administrator
        [ $? -ne 0 ] && return 1
        rabbitmqctl delete_user guest >/dev/null 2>&1
        [ $? -ne 0 ] && return 1
        rabbitmqctl set_permissions -p / ${USER_NAME} '.*' '.*' '.*'
        [ $? -ne 0 ] && return 1
        echo "in_the_cluster=no" >/dev/null 2>&1 > /opt/cluster_status.conf
        CLUSTER_STATUS[0]="no"
    fi
    wait
    return 0
}

function setCluster()
{
    local cluster=$(getValue "local_host" |awk '{print $2}')
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xno" ];then
            ssh ${i} "source /etc/profile;rabbitmqctl stop"
            [ $? -ne 0 ] && return 1
            sleep 6s
            scp -r ~/.erlang.cookie ${i}:~/
            scp -r /var/lib/rabbitmq/.erlang.cookie ${i}:/var/lib/rabbitmq/
            ssh ${i} "source /etc/profile;rabbitmq-server -detached"
            [ $? -ne 0 ] && return 1
            sleep 6s
            ssh ${i} "source /etc/profile;rabbitmqctl stop_app"
            [ $? -ne 0 ] && return 1
            ssh ${i} "source /etc/profile;rabbitmqctl join_cluster rabbit@${cluster}"
            [ $? -ne 0 ] && return 1
            ssh ${i} "source /etc/profile;rabbitmqctl start_app"
            [ $? -ne 0 ] && return 1
            ssh ${i} 'echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="yes"
        fi
    done
    [ "X${CLUSTER_STATUS[0]}" == "Xno" ] && echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf
    CLUSTER_STATUS[0]="yes"
    return 0
}

function setKeepalived()
{
    local confFile="${CONF_DIR}/keepalived.conf"
    local tempFile="${RUN_PATH}/keepalived.conf"
    
    local server_ip=$(getValue "server_ip")
    local ethernet_port=$(getValue "ethernet_port")

    sed -i "s|<SERVER_IP>|${server_ip}|g" "${RUN_PATH}/status.sh"
    mkdir -p /opt/service
    yes|cp ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh /opt/service/

    yes|cp ${confFile} ${tempFile}
    sed -i "s|<SERVER_IP>|${server_ip}|g" ${tempFile}
    sed -i "s|<ETH>|${ethernet_port}|g" ${tempFile}
    sed -i "s|<PRIOR>|100|g" ${tempFile}
    sed -i "s|<STATE>|MASTER|g" ${tempFile}
    yes|mv ${tempFile} /etc/keepalived/keepalived.conf
    
    local script_path="/etc/keepalived/scripts/"
    local script_file="${script_path}rabbitmq_check.sh"
    mkdir -p ${script_path}
    echo -n '#!/bin/bash
RET=$(/usr/bin/nmap -sS 127.0.0.1 -p 5672 | grep 5672 | awk '>${script_file}
    
    echo -n "'{printf ">>${script_file}
    echo -n '$2}'>>${script_file}
    echo "')">>${script_file}
    
    echo -n 'if [ "X${RET}" != "Xopen" ]; then
    source /etc/profile;
    /usr/sbin/rabbitmq-server -detached
    sleep 10s
    RET=$(/usr/bin/nmap -sS 127.0.0.1 -p 5672 | grep 5672 | awk '>>${script_file}
    
    echo -n "'{printf ">>${script_file}
    echo -n '$2}'>>${script_file}
    echo "')">>${script_file}
    
    echo '    if [ "X${RET}" != "Xopen" ]; then
        /etc/init.d/keepalived stop
    fi
fi'>>${script_file}
    chmod +x -R ${script_path}
    chkconfig keepalived on
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        ssh ${i} "mkdir -p /opt/service"
        scp -r ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${i}:/opt/service/

        yes|cp ${confFile} ${tempFile}
        sed -i "s|<SERVER_IP>|${server_ip}|g" ${tempFile}
        sed -i "s|<ETH>|${ethernet_port}|g" ${tempFile}
        sed -i "s|<PRIOR>|70|g" ${tempFile}
        sed -i "s|<STATE>|SLAVE|g" ${tempFile}
        scp -r ${tempFile} ${i}:/etc/keepalived/keepalived.conf
        rm -rf ${tempFile}
        
        scp -r ${script_path} ${i}:/etc/keepalived/
        ssh ${i} "chmod +x -R ${script_path}"
        ssh ${i} "chkconfig keepalived on"
    done
    return 0
}

function startKeepalived()
{
    /etc/init.d/keepalived start
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        ssh ${i} "/etc/init.d/keepalived start"
    done
    return 0
}

function main()
{
    getConfig
    [ $? -ne 0 ] && return 1
    installErlang
    [ $? -ne 0 ] && return 1
    installSimplejson
    [ $? -ne 0 ] && return 1
    installRabbitmq
    [ $? -ne 0 ] && return 1
    setRabbitmq
    [ $? -ne 0 ] && return 1
    setCluster
    [ $? -ne 0 ] && return 1
    setKeepalived
    [ $? -ne 0 ] && return 1
    startKeepalived
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?

