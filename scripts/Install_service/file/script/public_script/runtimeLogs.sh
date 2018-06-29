#!/bin/bash
RUN_PATH="$(cd $(dirname $0);pwd)"
RUN_EXP="${RUN_PATH}/runtimeLogs.exp"
GP_INFO=$(grep 'gp' /opt/Core_service_password.csv|awk -F ',' '{if($8=="gp") {if($2=="") printf $3","$6","$7;else printf $2" "$6" "$7}}')
GP_INFO=(${GP_INFO})
LEVEL=$1
SERVICE=$2
MESSAGE=$3

#####插入运行日志接口
#####调用方式:bash runtimeLogs.sh "level" "service" "message"
#####参数说明：
#####         level:日志级别:
#####                        INFO 提示，普通信息
#####                        WARN 警告，表明会出现潜在错误的情形，有些信息不是错误信息，但是也要给程序员的一些提示
#####                        ERROR 错误，指出虽然发生错误事件，但仍然不影响系统的继续运行
#####                        FATAL 执行失败及异常，指出每个严重的错误事件将会导致应用程序的退出

function make_exp(){
    echo "#!/usr/bin/expect
set timeout 3600
set passwd [lindex \$argv 0]
spawn ssh [lindex \$argv 1]@[lindex \$argv 2] \"hostname\"
expect {
    \"yes/no\" {send \"yes\r\"}
    \"*assword:\" {send \"\$passwd\\r\"}
}
expect {
    \"*assword:\" {send \"\$passwd\\r\"}
}
exit 0" > ${RUN_EXP}
    chmod +x ${RUN_EXP}
    ${RUN_EXP} ${GP_INFO[2]} ${GP_INFO[1]} ${GP_INFO[0]}
    echo "#!/usr/bin/expect
set timeout 3600
set passwd [lindex \$argv 0]
spawn ssh [lindex \$argv 1]@[lindex \$argv 2] \"export LD_LIBRARY_PATH=/opt/greenplum/greenplum-db-yuyi/lib;/opt/greenplum/greenplum-db/bin/psql -U gpadmin -h localhost -d pis100 << EOF 
[lindex \$argv 3];
EOF\"
expect {
    \"yes/no\" {send \"yes\r\"}
    \"*assword:\" {send \"\$passwd\\r\"}
}
expect {
    \"*assword:\" {send \"\$passwd\\r\"}
}
exit 0" > ${RUN_EXP}
    return 0
}

function check_base_exist(){
    local temp=$(${RUN_EXP} ${GP_INFO[2]} ${GP_INFO[1]} ${GP_INFO[0]} "select * from t_runtimelogs;" |grep "does not exist" |wc -l)
    if [ ${temp} -ne 0 ];then
        ${RUN_EXP} ${GP_INFO[2]} ${GP_INFO[1]} ${GP_INFO[0]} "create sequence t_runtimelogs_seq"
        ${RUN_EXP} ${GP_INFO[2]} ${GP_INFO[1]} ${GP_INFO[0]} "create table t_runtimelogs(id integer NOT NULL,time timestamp without time zone,level character varying(255),service character varying(255),message text,CONSTRAINT t_runtimelogs_pkey PRIMARY KEY (id))"
    fi
}

function main()
{
    make_exp
    [ $? -ne 0 ] && rm -rf ${RUN_EXP} && return 1
    ${RUN_EXP} ${GP_INFO[2]} ${GP_INFO[1]} ${GP_INFO[0]} "insert into t_runtimelogs values(nextval('t_runtimelogs_seq'),now(),'${LEVEL}','${SERVICE}','${MESSAGE}')"
    check_base_exist
    rm -rf ${RUN_EXP}
}

main
exit $?