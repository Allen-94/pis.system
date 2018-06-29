#!/bin/bash

su - gpadmin -c "gpstart -a"
[ $? -ne 0 ] && exit 1
exit 0