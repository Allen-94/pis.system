#!/bin/bash

##传参：本脚本.sh jdk包所在路径

RUN_PATH="$(cd $(dirname $0);pwd)"
JDK_PATH=$1
[ "X${JDK_PATH}" == "X" ] && JDK_PATH=${RUN_PATH}

function uncompressJdk()
{
    [ ! -f ${JDK_PATH}/jdk*.tar.gz ] && return 1
    tar -zxvf ${JDK_PATH}/jdk*.tar.gz -C /usr >/dev/null 2>&1 
    return 0
}

function setProfile()
{
    echo '# set java environment
export JAVA_HOME=/usr/java/default/
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=.:$CLASSPATH:$JAVA_HOME/lib:$JRE_HOME/lib
export PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin'>>/etc/profile
    source /etc/profile
    return 0
}

function main()
{
    uncompressJdk
    [ $? -ne 0 ] && return 1
    setProfile
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?

