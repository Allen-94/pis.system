#!/bin/bash

su - gpadmin -c "gpstop -a"
[ $? -ne 0 ] && exit 1
sleep 2s
su - gpadmin -c "gpstart -a"
[ $? -ne 0 ] && exit 1

exit 0