#!/bin/bash

su - gpadmin -c "gpstop -a"
[ $? -ne 0 ] && exit 1
exit 0