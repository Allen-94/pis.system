#!/bin/bash

##传参：本脚本.sh jdk包所在路径

RUN_PATH="$(cd $(dirname $0);pwd)"
GLIBC_PATH=$1
GLIBC_VERSION="2.14"
[ "X${GLIBC_PATH}" == "X" ] && GLIBC_PATH=${RUN_PATH}

function uncompressGlibc()
{
    [ ! -f ${GLIBC_PATH}/glibc-${GLIBC_VERSION}.tar.gz ] && return 1
    mkdir ${RUN_PATH}/temp/
    tar -zxvf ${GLIBC_PATH}/glibc-${GLIBC_VERSION}.tar.gz -C ${RUN_PATH}/temp/ >/dev/null 2>&1
    return 0
}

function makeGlibc()
{
    mkdir ${RUN_PATH}/temp/glibc-${GLIBC_VERSION}/build
    cd ${RUN_PATH}/temp/glibc-${GLIBC_VERSION}/build/
    ../configure --prefix=/opt/glibc-${GLIBC_VERSION}
    make && make install
    cd - >/dev/null 2>&1
    return 0
}

function linkGlibc()
{
    rm -rf /lib64/libc.so.6
    LD_PRELOAD=/opt/glibc-${GLIBC_VERSION}/lib/libc-${GLIBC_VERSION}.so ln -s /opt/glibc-${GLIBC_VERSION}/lib/libc-${GLIBC_VERSION}.so /lib64/libc.so.6
    return 0
}

function main()
{
    uncompressGlibc
    [ $? -ne 0 ] && return 1 && rm -rf ${RUN_PATH}/temp/
    makeGlibc
    [ $? -ne 0 ] && return 1 && rm -rf ${RUN_PATH}/temp/
    linkGlibc
    [ $? -ne 0 ] && return 1 && rm -rf ${RUN_PATH}/temp/
    rm -rf ${RUN_PATH}/temp/
    return 0
}

main
exit $?

