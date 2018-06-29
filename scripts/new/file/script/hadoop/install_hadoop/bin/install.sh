#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
. ${RUN_PATH}/common.sh
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/installHadoop_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
CONFIG_FILE="${RUN_PATH}/../config/config.conf"
HD_CONF="${RUN_PATH}/../config/hadoop"
HD_FILE="${RUN_PATH}/../file/hadoop*.tar.gz"
ZP_FILE="${RUN_PATH}/../file/zookeeper*.tar.gz"
CLUSTER_STATUS="none"

function getConfig()
{
    USER_PWD=$(getValue "hd_user_password")
    ROOT_PWD=$(getValue "root_password")
    HOST=$(getValue "data_host" |awk '{print $2}')
    HOST_IP=$(grep "_host" ${CONFIG_FILE} |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")'|awk '{print $1}')
    HOST_IP=(${HOST_IP})
    ls /opt/cluster_status.conf >/dev/null 2>&1
    if [ $? -eq 0 ];then
        CLUSTER_STATUS=$(grep "^in_the_cluster" /opt/cluster_status.conf |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")')
    fi
    setHosts
    [ $? -ne 0 ] && return 1
    let nu=0
    local hosts=(${HOST})
    getClusterStatus
    
    NFS_IP=""
    local flag=$(grep -v "^#" ${RUN_PATH}/../config/permit_nfs_ip.txt)
    flag=(${flag})
    let len=${#flag[@]}-1
    if [ ${len} -ne -1 ];then
        for((i=0;i<=${len};i++));do
            if [ ${i} -ne ${len} ];then
                NFS_IP="${NFS_IP}${flag[${i}]} rw;"
            else
                NFS_IP="${NFS_IP}${flag[${i}]} rw"
            fi
        done
    fi
    
    return 0
}

function typeFlag()
{
    local server_ip=$(getValue "server_ip")
    sed -i "s|<SERVER_IP>|${server_ip}|g" "${RUN_PATH}/status.sh"
    mkdir -p /opt/service
    yes|cp ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh /opt/service/
    
    echo "Master" > /opt/typeFlag
    ssh ${HOST_IP[1]} "echo 'Slave' > /opt/typeFlag"
    ssh ${HOST_IP[1]} "mkdir -p /opt/service"
    scp -r ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${HOST_IP[1]}:/opt/service/

    for((i=2;i<${#HOST_IP[@]};i++));do
        ssh ${HOST_IP[${i}]} "echo 'Data' > /opt/typeFlag"
        ssh ${HOST_IP[${i}]} "mkdir -p /opt/service"
        scp -r ${RUN_PATH}/status.sh ${RUN_PATH}/start.sh ${RUN_PATH}/stop.sh ${RUN_PATH}/restart.sh ${HOST_IP[${i}]}:/opt/service/
    done
    return 0
}

function makeHadoopUser()
{
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        bash ${RUN_PATH}/addUser.sh ${USER_PWD} hadoop 530
        [ $? -ne 0 ] && return 1
    fi
    
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp ${RUN_PATH}/addUser.sh ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} bash /tmp/addUser.sh ${USER_PWD} hadoop 530
            ssh ${i} rm -rf /tmp/addUser.sh
        fi
    done
    return 0
}

function installJdk()
{
    local file_path="${RUN_PATH}/../file"
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        bash ${RUN_PATH}/installJdk.sh ${file_path}
        [ $? -ne 0 ] && return 1
    fi
    
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp ${RUN_PATH}/installJdk.sh ${file_path}/jdk*.tar.gz ${i}:/tmp/ >/dev/null 2>&1
            ssh ${i} bash /tmp/installJdk.sh /tmp
            ssh ${i} rm -rf /tmp/installJdk.sh /tmp/jdk*.tar.gz
        fi
    done
    return 0
}

function makeHadoopSshKey()
{
    cp ${RUN_PATH}/makeSshKey.sh /home/hadoop/
    cp ${RUN_PATH}/common.sh /home/hadoop/
    chown hadoop:hadoop /home/hadoop/makeSshKey.sh /home/hadoop/common.sh
    local hosts=(${HOST})
    hosts=${hosts[@]}
    su - hadoop -c "bash /home/hadoop/makeSshKey.sh ${USER_PWD} , ${hosts}"
    [ $? -ne 0 ] && rm -rf /home/hadoop/makeSshKey.sh && return 1
    rm -rf /home/hadoop/makeSshKey.sh /home/hadoop/common.sh
    return 0
}

function setHdConf()
{
    local master_ip=$(getValue "master_host" |awk '{print $1}')
    local num=$(getValue "dfs_replication_number")
    mkdir -p ${RUN_PATH}/hdtmp
    yes|cp -rf ${HD_CONF}/* ${RUN_PATH}/hdtmp/
    HD_CONF="${RUN_PATH}/hdtmp/"
    echo ${master_ip} >/dev/null 2>&1 > ${HD_CONF}/masters
    grep "^data_host" ${CONFIG_FILE} |awk -F "=" '{print $2}' |awk 'gsub(/^ *| *$/,"")' |awk '{print $1}' > ${HD_CONF}/slaves
    echo "${master_ip} nn1" >/dev/null 2>&1 > ${HD_CONF}/node
    echo "${HOST_IP[1]} nn2" >/dev/null 2>&1 >> ${HD_CONF}/node
    sed -i "s|<MASTER_IP>|${master_ip}|g" ${HD_CONF}/mapred-site.xml
    sed -i "s|<MASTER_IP>|${master_ip}|g" ${HD_CONF}/site/core-site.xml
    sed -i "s|<DATA_1>|${HOST_IP[1]}|g" ${HD_CONF}/site/core-site.xml
    sed -i "s|<DATA_2>|${HOST_IP[2]}|g" ${HD_CONF}/site/core-site.xml
    sed -i "s|<MASTER_IP>|${master_ip}|g" ${HD_CONF}/site/hdfs-site.xml
    if [ "X${NFS_IP}" != "X" ];then
        sed -i "s|<NFS_IP>|${NFS_IP}|g" ${HD_CONF}/site/hdfs-site.xml
    else
        sed -i "s|<NFS_IP>|*|g" ${HD_CONF}/site/hdfs-site.xml
    fi
    local qjournal="${HOST_IP[1]}:8485"
    for((i=2;i<${#HOST_IP[@]};i++));do
        qjournal="${qjournal};${HOST_IP[${i}]}:8485"
    done
    sed -i "s|<DATA_1>|${HOST_IP[1]}|g" ${HD_CONF}/site/hdfs-site.xml
    sed -i "s|<QJOURNAL>|${qjournal}|g" ${HD_CONF}/site/hdfs-site.xml
    sed -i "s|<MASTER_IP>|${master_ip}|g" ${HD_CONF}/yarn-site.xml
    
    yes|cp ${HD_CONF}/site/hdfs-site.xml ${HD_CONF}/site/core-site.xml ${HD_CONF}/site/nn1
    yes|cp ${HD_CONF}/site/hdfs-site.xml ${HD_CONF}/site/core-site.xml ${HD_CONF}/site/nn2
    sed -i "s|<IP>|${master_ip}|g" ${HD_CONF}/site/nn1/hdfs-site.xml
    sed -i "s|<IP>|${HOST_IP[1]}|g" ${HD_CONF}/site/nn2/hdfs-site.xml
    sed -i "s|<IP>|${master_ip}|g" ${HD_CONF}/site/nn1/core-site.xml
    sed -i "s|<IP>|${HOST_IP[1]}|g" ${HD_CONF}/site/nn2/core-site.xml
    yes|cp ${HD_CONF}/site/nn1/hdfs-site.xml ${HD_CONF}/site/nn1/core-site.xml ${HD_CONF}
    
    return 0
}

function uncompressHd()
{
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        [ ! -f ${HD_FILE} ] && return 1
        tar -zxvf ${HD_FILE} -C /usr >/dev/null 2>&1
        mv /usr/hadoop* /usr/hadoop/
        yes|cp -rf ${HD_CONF}/* /usr/hadoop/etc/hadoop/
        echo "log4j.logger.org.apache.hadoop.hdfs.nfs=DEBUG
log4j.logger.org.apache.hadoop.oncrpc=DEBUG" >> /usr/hadoop/etc/hadoop/log4j.properties
        echo "export JAVA_HOME=/usr/java/default/" >> /usr/hadoop/etc/hadoop/hadoop-env.sh
        echo "export JAVA_HOME=/usr/java/default/" >> /usr/hadoop/etc/hadoop/yarn-env.sh
        mkdir -p /usr/hadoop/dfs/name
        mkdir -p /usr/hadoop/dfs/data
        mkdir -p /usr/hadoop/tmp
        rm -rf ${RUN_PATH}/hdtmp
    fi
    return 0
}

function installZookeeper()
{
    if [ "X${CLUSTER_STATUS[0]}" == "Xno" ];then
        [ ! -f ${ZP_FILE} ] && return 1
        mkdir -p /opt/zookeeper /opt/journal/data
        tar -zxvf ${ZP_FILE} -C /home/ >/dev/null 2>&1
        mv /home/zookeeper* /home/zookeeper
        cp -a /home/zookeeper/conf/zoo_sample.cfg /home/zookeeper/conf/zoo.cfg
        sed -i "s|^dataDir|#dataDir|g" /home/zookeeper/conf/zoo.cfg
        echo "dataDir=/opt/zookeeper
server.1=${HOST_IP[0]}:2888:3888
server.2=${HOST_IP[1]}:2888:3888
server.3=${HOST_IP[2]}:2888:3888" >> /home/zookeeper/conf/zoo.cfg
        touch /opt/zookeeper/myid
        echo "1" > /opt/zookeeper/myid
        echo 'export PATH=$PATH:/home/zookeeper/bin' >> /etc/profile
        source /etc/profile
    fi
    for((i=1;i<3;i++));do
        if [ "X${CLUSTER_STATUS[${i}]}" == "Xno" ];then
            scp -r /home/zookeeper ${HOST_IP[${i}]}:/home/
            scp -r /opt/zookeeper ${HOST_IP[${i}]}:/opt/
            ssh ${HOST_IP[${i}]} "echo $((${i}+1)) > /opt/zookeeper/myid"
            ssh ${HOST_IP[${i}]} "chown -R hadoop:hadoop /home/zookeeper /opt/zookeeper"
            scp -r /etc/profile ${HOST_IP[${i}]}:/etc/
            ssh ${HOST_IP[${i}]} source /etc/profile
        fi
    done
    
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xno" ];then
            ssh ${i} mkdir -p /opt/journal/data
            ssh ${i} "chown -R hadoop:hadoop /opt/journal"
            ssh ${i} 'echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="yes"
        fi
    done
    
    chown -R hadoop:hadoop /home/zookeeper /opt/zookeeper /opt/journal
    return 0
}

function installHd()
{
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        echo '# set hadoop path
export HADOOP_HOME=/usr/hadoop
export PATH=$PATH:$HADOOP_HOME/bin'>>/etc/profile
        source /etc/profile
        chown -R hadoop:hadoop /usr/hadoop/
        mv -f ${RUN_PATH}/../config/hostList /opt/
        chown -R hadoop:hadoop /opt/hostList
        echo "in_the_cluster=no" >/dev/null 2>&1 > /opt/cluster_status.conf
        CLUSTER_STATUS[0]="no"
    fi
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            scp -r /usr/hadoop ${i}:/usr/ >/dev/null 2>&1
            scp -r /etc/profile ${i}:/etc/ >/dev/null 2>&1
            scp -r /opt/hostList ${i}:/opt/ >/dev/null 2>&1
            ssh ${i} source /etc/profile
            ssh ${i} chown -R hadoop:hadoop /usr/hadoop/
            ssh ${i} chown -R hadoop:hadoop /opt/hostList
            ssh ${i} 'echo "in_the_cluster=no" >/dev/null 2>&1 > /opt/cluster_status.conf'
            CLUSTER_STATUS[${nu}]="no"
        fi
    done
    return 0
}

function upgradeGlibc()
{
    let nu=0
    local hosts=(${HOST})
    for i in ${hosts[@]}; do
        let nu=nu+1
        {
        if [ "X${CLUSTER_STATUS[${nu}]}" == "Xnone" ];then
            ssh ${i} mkdir -p /tmp/glibc/
            scp -r ${RUN_PATH}/../file/glibc*.tar.gz ${i}:/tmp/glibc/ >/dev/null 2>&1
            scp -r ${RUN_PATH}/upgradeGlibc.sh ${i}:/tmp/glibc/ >/dev/null 2>&1
            ssh ${i} bash /tmp/glibc/upgradeGlibc.sh >/dev/null 2>&1
            ssh ${i} rm -rf /tmp/glibc/
        fi
        } &
    done
    if [ "X${CLUSTER_STATUS[0]}" == "Xnone" ];then
        bash ${RUN_PATH}/upgradeGlibc.sh ${RUN_PATH}/../file/
        [ $? -ne 0 ] && return 1
    fi
    wait
    return 0
}

function startHd()
{
    if [ "X${CLUSTER_STATUS[0]}" == "Xno" ];then
        local hosts=(${HOST})
        su - hadoop -c "/home/zookeeper/bin/zkServer.sh start"
        for((i=1;i<3;i++));do
            ssh ${HOST_IP[${i}]} 'su - hadoop -c "/home/zookeeper/bin/zkServer.sh start"'
        done
        mkdir -p /tmp/.hdfs-nfs
        chown hadoop:hadoop /tmp/.hdfs-nfs/
        mkdir -p /usr/hadoop/logs
        chown -R hadoop:hadoop /usr/hadoop
        /usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs start portmap
        su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs start nfs3"
        
        for i in ${hosts[@]}; do
            ssh ${i} 'mkdir -p /tmp/.hdfs-nfs;chown hadoop:hadoop /tmp/.hdfs-nfs/'
            ssh ${i} 'mkdir -p /usr/hadoop/logs;chown -R hadoop:hadoop /usr/hadoop'
            ssh ${i} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh start journalnode"'
        done
        ##ssh ${HOST_IP[1]} '/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs start portmap'
        ##ssh ${HOST_IP[1]} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh --script /usr/hadoop/bin/hdfs start nfs3"'
       
        su - hadoop -c "/usr/hadoop/sbin/start-dfs.sh"
        sleep 2s
        su - hadoop -c "/usr/hadoop/bin/hdfs namenode -format"
        [ $? -ne 0 ] && return 1
        scp -r /usr/hadoop/dfs ${HOST_IP[1]}:/usr/hadoop
        ssh ${HOST_IP[1]} "chown -R hadoop:hadoop /usr/hadoop"
        su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh start namenode"
        ssh ${HOST_IP[1]} 'su - hadoop -c "yes|/usr/hadoop/bin/hdfs namenode -bootstrapStandby"'
        su - hadoop -c "/usr/hadoop/sbin/stop-dfs.sh"
        sleep 2s
        su - hadoop -c "/usr/hadoop/sbin/stop-all.sh"
        sleep 2s
        su - hadoop -c "yes|/usr/hadoop/bin/hdfs zkfc -formatZK"
        su - hadoop -c "/usr/hadoop/sbin/start-all.sh"
        sleep 3s
        local flag=$(/usr/hadoop/bin/hdfs haadmin -getServiceState nn1)
        if [ "X${flag}" == "Xstandby" ];then
            ssh ${HOST_IP[1]} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh stop datanode"'
            ssh ${HOST_IP[1]} 'su - hadoop -c "/usr/hadoop/sbin/yarn-daemon.sh stop nodemanager"'
            ssh ${HOST_IP[1]} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh stop namenode"'
            sleep 6s
            ssh ${HOST_IP[1]} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh start datanode"'
            ssh ${HOST_IP[1]} 'su - hadoop -c "/usr/hadoop/sbin/yarn-daemon.sh start nodemanager"'
            ssh ${HOST_IP[1]} 'su - hadoop -c "/usr/hadoop/sbin/hadoop-daemon.sh start namenode"'
        
        fi
        echo "in_the_cluster=yes" >/dev/null 2>&1 > /opt/cluster_status.conf
        CLUSTER_STATUS[0]="yes"
        
        local nu=$(grep -n "source /etc/profile;bash /opt/service/start.sh" /etc/rc.d/rc.local |awk -F ":" '{print $1}')
        nu=(${nu})
        let len=${#nu[@]}-1
        for((i=${len};i>=0;i--)) do
            sed -i "${nu[${i}]}d" /etc/rc.d/rc.local
        done
        echo "source /etc/profile;bash /opt/service/start.sh" >> /etc/rc.d/rc.local
        nu=$(ssh ${HOST_IP[1]} "grep -n \"source /etc/profile;bash /opt/service/start.sh\" /etc/rc.d/rc.local"|awk -F ':' '{print $1}')
        nu=(${nu})
        let len=${#nu[@]}-1
        for((i=${len};i>=0;i--)) do
            ssh ${HOST_IP[1]} "sed -i '${nu[${i}]}d' /etc/rc.d/rc.local"
        done
        ssh ${HOST_IP[1]} "echo \"source /etc/profile;bash /opt/service/start.sh\" >> /etc/rc.d/rc.local"

    fi
    return 0
}

function main()
{
    getConfig
    [ $? -ne 0 ] && return 1
    makeHadoopUser
    [ $? -ne 0 ] && return 1
    installJdk
    [ $? -ne 0 ] && return 1
    makeHadoopSshKey
    [ $? -ne 0 ] && return 1
    setHdConf
    [ $? -ne 0 ] && return 1
    upgradeGlibc
    [ $? -ne 0 ] && return 1
    uncompressHd
    [ $? -ne 0 ] && return 1
    installHd
    [ $? -ne 0 ] && return 1
    installZookeeper
    [ $? -ne 0 ] && return 1
    startHd
    [ $? -ne 0 ] && return 1
    setServerIp ${HOST_IP[1]}
    [ $? -ne 0 ] && return 1
    typeFlag
    [ $? -ne 0 ] && return 1
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?

