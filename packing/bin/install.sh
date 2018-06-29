#!/bin/bash

RUN_PATH="$(cd $(dirname $0);pwd)"
LOG_FILE="${RUN_PATH}/../logs"
[ -d ${LOG_FILE} ] || mkdir -p ${LOG_FILE}
LOG_FILE="$(cd ${LOG_FILE};pwd)/install_$(date +'%Y%m%d%H%M%S').log"
rm -rf ${LOG_FILE}
touch ${LOG_FILE}
FILE_PATH="${RUN_PATH}/../file"
TEMP_PATH="${RUN_PATH}/temp"

function install_maven(){
    tar -zxvf ${FILE_PATH}/apache-maven-*-bin.tar.gz -C /opt
    ln -s /opt/apache-maven-* /opt/apache-maven
    echo 'export M2_HOME=/opt/apache-maven
export PATH=$PATH:$M2_HOME/bin' >> /etc/profile
    source /etc/profile
    tar -zxvf ${FILE_PATH}/m2lib.tar.gz -C ~/
    return 0
}

function make_apr()
{
    tar -zxvf ${FILE_PATH}/apr-1*tar.gz -C ${TEMP_PATH}
    cd ${TEMP_PATH}/apr-1*
    ./configure --prefix=/opt/svn/apr
    make && make test && make install
    [ $? -ne 0 ] && return 1
    cd -
    return 0
}

function make_apr_util()
{
    rpm -ivh ${FILE_PATH}/expat-devel*.rpm
    tar -zxvf ${FILE_PATH}/apr-util*tar.gz -C ${TEMP_PATH}
    cd ${TEMP_PATH}/apr-util*
    ./configure --prefix=/opt/svn/apr-util --with-apr=/opt/svn/apr
    make && make test && make install
    [ $? -ne 0 ] && return 1
    cd -
    return 0
}

function make_neon()
{
    tar -zxvf ${FILE_PATH}/neon*tar.gz -C ${TEMP_PATH}
    cd ${TEMP_PATH}/neon*
    ./configure --prefix=/opt/svn/neon
    make && make install
    [ $? -ne 0 ] && return 1
    cd -
    return 0
}

function make_subversion()
{
    tar -zxvf ${FILE_PATH}/subversion*.tar.gz -C ${TEMP_PATH}
    tar -zxvf ${FILE_PATH}/sqlite-autoconf*.tar.gz -C ${TEMP_PATH}/subversion*
    cd ${TEMP_PATH}/subversion*
    mv sqlite-autoconf* sqlite-amalgamation
    ./configure --prefix=/opt/svn/subversion/ --with-apr=/opt/svn/apr --with-apr-util=/opt/svn/apr-util/ --with-neon=/opt/svn/neon/
    make && make install
    [ $? -ne 0 ] && return 1
    cd -
    echo 'export PATH=$PATH:/opt/svn/subversion/bin' >> /etc/profile
    source /etc/profile
    return 0
}

function install_svn()
{
    make_apr
    [ $? -ne 0 ] && return 1
    make_apr_util
    [ $? -ne 0 ] && return 1
    make_neon
    [ $? -ne 0 ] && return 1
    make_subversion
    [ $? -ne 0 ] && return 1
    return 0
}

function main()
{
    mkdir -p ${TEMP_PATH}
    install_maven
    [ $? -ne 0 ] && rm -rf ${TEMP_PATH} && return 1
    install_svn
    [ $? -ne 0 ] && rm -rf ${TEMP_PATH} && return 1
    rm -rf ${TEMP_PATH}
    return 0
}

set -x
main 2>${LOG_FILE}
exit $?