#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/changeRabbitmq_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
MODE="$@"
OPTION_NU="$#"
CONFIG_FILE="${RUN_PATH}/../config/config.conf"
NODES_CONF="${RUN_PATH}/../config/nodes.conf"
FILE_PATH="${RUN_PATH}/../file"
CLUSTER_STATUS="yes"


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

function getConfig()
{
    USER_NAME=$(getValue "rabbitmq_user_name")
    USER_PWD=$(getValue "rabbitmq_user_password")
    ROOT_PWD=$(getValue "root_password")
    OLD_HOST=$(getValue "other_host" |awk '{print $2}')
    LOCAL_NAME=$(getValue "local_host" |awk '{print $2}')
    CONFIG_FILE=${NODES_CONF}
    ADD_HOST=$(getValue "nodes_host" |awk '{print $2}')
    ALL_HOST="${OLD_HOST} ${ADD_HOST}"
    if [ "X${MODE}" == "X-add" ];then
        setAllHosts
        [ $? -ne 0 ] && return 1
    fi
    let nu=0
    local hosts=(${ADD_HOST})
    getClusterStatus
    return 0
}

function installErlang()
{
    tar -zxf ${FILE_PATH}/otp_src*.tar.gz -C /tmp
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
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
    done
    rm -rf /tmp/otp_src*
    return 0
}

function installSimplejson()
{
    tar -zxf ${FILE_PATH}/simplejson*.tar.gz -C /tmp
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r /tmp/simplejson* ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} "cd /tmp/simplejson*;python setup.py install >/dev/null 2>&1"
            [ $? -ne 0 ] && rm -rf /tmp/simplejson* && ssh ${i} rm -rf /tmp/simplejson* && return 1
            ssh ${i} rm -rf /tmp/simplejson*
        fi
    done
    rm -rf /tmp/simplejson*
    return 0
}

function installRabbitmq()
{
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r ${FILE_PATH}/rabbitmq-server-*.noarch.rpm ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} rpm -i --nodeps /tmp/rabbitmq-server-*.noarch.rpm
            ssh ${i} rm -rf /tmp/rabbitmq-server-*.noarch.rpm
        fi
    done
    return 0
}

function setRabbitmq()
{
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" != "Xyes" ];then
            ssh ${i} "source /etc/profile;rabbitmqctl stop >/dev/null 2>&1"
            sleep 3s
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
    done
    return 0
}

function setCluster()
{
    let nu=0
    local hosts=(${ADD_HOST})
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
            ssh ${i} "source /etc/profile;rabbitmqctl join_cluster rabbit@${LOCAL_NAME}"
            [ $? -ne 0 ] && return 1
            ssh ${i} "source /etc/profile;rabbitmqctl start_app"
            [ $? -ne 0 ] && return 1
            ssh ${i} 'echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="yes"
        fi
    done
    return 0
}

function removeCluster()
{
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xyes" ];then
            ssh ${i} "source /etc/profile;rabbitmqctl stop_app"
            [ $? -ne 0 ] && return 1
            ssh ${i} "source /etc/profile;rabbitmqctl reset"
            [ $? -ne 0 ] && return 1
            ssh ${i} "source /etc/profile;rabbitmqctl start_app"
            [ $? -ne 0 ] && return 1
            ssh ${i} 'echo "in_the_cluster=no" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="no"
        fi
    done
    return 0
}

function clearCluster()
{
    let nu=0
    local hosts=(${ADD_HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xyes" ];then
            rabbitmqctl forget_cluster_node rabbit@${i}
            [ $? -ne 0 ] && return 1
        fi
    done
    return 0
}

function main()
{
    getConfig
    [ $? -ne 0 ] && return 1
    if [ ${OPTION_NU} -eq 1 ];then
        if [ "X${MODE}" == "X-add" ];then
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
        elif [ "X${MODE}" == "X-del" ];then
            removeCluster
            [ $? -ne 0 ] && return 1
        elif [ "X${MODE}" == "X-clear" ];then
            clearCluster
            [ $? -ne 0 ] && return 1
        elif [ "X${MODE}" == "X--help" ];then
            echo "Usage: setnodes [OPTION]
Shell options:
       -add
       -del
       -clear"
        else
            echo "setnodes: unrecognized option '${MODE}'
Usage: setnodes [OPTION]
Try \`setnodes --help' for more information."
            return 2
        fi
    elif [ ${OPTION_NU} -eq 0 ];then
        echo "Usage: setnodes [OPTION]
Try \`setnodes --help' for more information."
        return 2
    else
        echo "setnodes: unrecognized option '${MODE}'
Usage: setnodes [OPTION]
Try \`setnodes --help' for more information."
        return 2
    fi
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?

