#!/bin/bash

##传参：本脚本.sh rpm包所在路径

RUN_PATH="$(cd $(dirname $0);pwd)"
RPM_PATH=$1
[ "X${RPM_PATH}" == "X" ] && RPM_PATH=${RUN_PATH}

function installRpm()
{
    rpm -ivh ${RPM_PATH}/keyutils-*.rpm
    rpm -ivh ${RPM_PATH}/lib*.rpm
    rpm -ivh ${RPM_PATH}/python-argparse-*.rpm
    rpm -ivh ${RPM_PATH}/rpcbind-*.rpm
    rpm -ivh ${RPM_PATH}/nfs-utils-*.rpm
    return 0
}

function main()
{
    installRpm
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?

