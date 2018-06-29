#!/bin/bash
FILE=$1
DIR=$2
RUN_PATH="$(cd $(dirname $0);pwd)"

function main()
{
    local hosts=$(cat /opt/hostList|awk -F ',' '{print $1}'|awk 'gsub(/^ *| *$/,"")')
    hosts=(${hosts})
    local sendFile="${RUN_PATH}/temp.exp"
    echo '#!/usr/bin/expect
set timeout 3600
spawn scp -P 22 -r [lindex $argv 0] [lindex $argv 1]
expect {
 "(yes/no)?" {
   send "no\n"
  }
}
exit 0
'>>${sendFile}
    chmod +x ${sendFile}
    for i in ${hosts[@]}; do
        ${sendFile} ${FILE} ${i}:${DIR}
    done
    rm -rf ${sendFile}
    return 0
}
main
exit $?