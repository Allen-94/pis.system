#!/bin/bash

#加密方法  //使用base64加密3次
function encoder()
{
    local pwd=$1
    for((i=0;i<3;i++));do
        pwd=$(echo ${pwd}|base64)
    done
    echo ${pwd}
    return 0
}

#解密方法  //使用base64解密3次
function decoder()
{
    local pwd=$1
    for((i=0;i<3;i++));do
        pwd=$(echo ${pwd}|base64 -d)
    done
    echo ${pwd}
    return 0
}

function main()
{
    local mode=$1
    local pwd=$2
    local str=""
    [ ${mode} -eq 1 ] && str=$(encoder ${pwd})
    [ ${mode} -eq 2 ] && str=$(decoder ${pwd})
    echo ${str}
    return 0
}

main $@
exit $?