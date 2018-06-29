#!/bin/bash
IP1=<IP1>
IP2=<IP2>
RUN_PATH="$(cd $(dirname $0);pwd)"

function dfExp()
{
    echo '#!/usr/bin/expect
set timeout 20
spawn /bin/df -h
expect {
 "(yes/no)?" {
   send "yes\n"
  }
}
exit 0' > ${RUN_PATH}/temp.exp
    chmod +x ${RUN_PATH}/temp.exp
    return 0
}

function mountNow()
{
    local ip=$1
    umount -f /var/ftp/
    mount -t nfs -o vers=3,proto=tcp,nolock,noacl,sync ${ip}:/ /var/ftp/
    [ $? -ne 0 ] && return 1
    return 0
}

function mountHdfs()
{
    dfExp
    local flag=$(${RUN_PATH}/temp.exp|grep /var/ftp |grep ${IP1} |wc -l)
    if [ ${flag} -eq 0 ];then
        mountNow ${IP1}
        if [ $? -ne 0 ];then
            flag=$(${RUN_PATH}/temp.exp|grep /var/ftp |grep ${IP2} |wc -l)
            if [ ${flag} -eq 0 ];then
                mountNow ${IP2}
                [ $? -ne 0 ] && return 1
            else
                ls -l /var/ftp
                if [ $? -ne 0 ];then
                    mountNow ${IP2}
                    [ $? -ne 0 ] && return 1
                fi
            fi
        fi
    else
        ls -l /var/ftp
        if [ $? -ne 0 ];then
            umount -f /var/ftp/
            mountHdfs
        fi
    fi
    rm -rf ${RUN_PATH}/temp.exp
    return 0
}

function main()
{
    source /etc/profile
    local stat=$(cat /opt/service/mountStat.txt)
    [ "X${stat}" == "Xclose" ] && return 0
    mountHdfs
    [ $? -ne 0 ] && return 1
    return 0
}

main
exit $?