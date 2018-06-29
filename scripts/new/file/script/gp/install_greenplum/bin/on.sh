#!/bin/bash
function main()
{
    source /etc/profile
    
    local name=$(cat /dev/urandom |head -c 5 |md5sum |head -c 8)
    echo "${name}" > /opt/service/onName.txt
    
    su - gpadmin -c '/opt/greenplum/greenplum-db/bin/gpstart -a'
    
    sleep 6s
    
    local num=$(grep -n "^ON_NAME=" /root/autoFloatIP.sh|awk -F ':' '{print $1}')
    sed -i "${num}d" /root/autoFloatIP.sh
    let num=num-1
    sed -i "${num}a ON_NAME=${name}" /root/autoFloatIP.sh
    
    return 0
}

main
exit $?