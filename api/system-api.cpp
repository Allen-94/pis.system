#include <cstdlib>
#include <string>
#include <cstdio>
#include <cstring>
#include <iostream>
#include <algorithm>

using namespace std;

const int N = 300;

// 在Linux操作系统中执行命令，返回执行结果
string exec(string cmd){
    char line[N];
    string res="";
    FILE *fp;

    const char *sysCommand = cmd.data();
    if ((fp = popen(sysCommand, "r")) == NULL) {
        return "error";
    }
    while (fgets(line, sizeof(line)-1, fp) != NULL){
        res=res+line;
    }
    
    pclose(fp);
    return res;
}

// 获取指定集群服务信息
string findServiceInfo(string clusterName){
    string cmd="grep '^"+clusterName+"' /opt/Core_service_password.csv|awk -F ',' '{if($2==\"\") printf $3\",\"$6\",\"$7;else printf $2\",\"$6\",\"$7}'";
    return exec(cmd);
}

// 获取系统服务服务信息
string findSystemServiceInfo(){
    string cmd="grep '^SystemManagement' /opt/Core_service_password.csv|awk -F ',' '{if($2==\"\") printf $3;else printf $2}'";
    return exec(cmd);
}

// 获取数据库服务信息
string findDBInfo(){
    string cmd="grep 'gp' /opt/Core_service_password.csv|awk -F ',' '{if($8==\"gp\") {if($2==\"\") printf $3\",\"$6\",\"$7;else printf $2\",\"$6\",\"$7}}'";
    return exec(cmd);
}

// 获取消息中心服务信息
string findMQInfo(){
    string cmd="grep 'rabbitmq' /opt/Core_service_password.csv|awk -F ',' '{if($8==\"rabbitmq\") {if($2==\"\") printf $3\",\"$6\",\"$7;else printf $2\",\"$6\",\"$7}}'";
    return exec(cmd);
}

// 获取HDFS服务信息
string findHDFSInfo(){
    string cmd="grep 'hadoop' /opt/Core_service_password.csv|awk -F ',' '{if($8==\"hadoop\") {if($2==\"\") printf $3\",\"$6\",\"$7;else printf $2\",\"$6\",\"$7}}'";
    return exec(cmd);
}

// 获取FTP服务信息
string findFTPInfo(){
    string cmd="a=($(grep 'ftp' /opt/Core_service_password.csv|awk -F ',' '{if($8==\"ftp\") {if($2==\"\") print $1\",\"$3\",\"$6\",\"$7;else print $1\",\"$2\",\"$6\",\"$7}}'));for((i=0;i<${#a[@]};i++));do echo -n [${a[$i]}];((b=${#a[@]}-1));[ ${i} -ne ${b} ] && echo -n ',';done";
    return exec(cmd);
}

// 获取NTP服务信息
string findNTPInfo(){
    string cmd="grep 'ntp' /opt/Core_service_password.csv|awk -F ',' '{if($8==\"ntp\") {if($2==\"\") printf $3;else printf $2}}'";
    return exec(cmd);
}

// 获取流媒体服务信息
string findStreamInfo(){
    string cmd="a=($(grep 'stream' /opt/Core_service_password.csv|awk -F ',' '{if($8==\"stream\") {if($2==\"\") print $1\",\"$3\",\"$6\",\"$7;else print $1\",\"$2\",\"$6\",\"$7}}'));for((i=0;i<${#a[@]};i++));do echo -n [${a[$i]}];((b=${#a[@]}-1));[ ${i} -ne ${b} ] && echo -n ',';done";
    return exec(cmd);
}

//流媒体服务获取本身标识；仅在流媒体服务上调接口才能使用;在流媒体服务虚拟机中的/opt/service中有一个flag文件，里面有当前流媒体服务的标识信息
string findStreamFlag(){
    string cmd="cat /opt/service/flag";
    return exec(cmd);
}

// 获取所有服务的信息
string findServiceInfo(){
    //string cmd="a=($(cat /opt/Core_service_password.csv|awk -F ',' '{if($2==\"\") printf $1\",\"$3\",\"$6\",\"$7;else printf $1\",\"$2\",\"$6\",\"$7}'));for((i=0;i<${#a[@]};i++));do echo -n [${a[$i]}];((b=${#a[@]}-1));[ ${i} -ne ${b} ] && echo -n ',';done";
    //return exec(cmd);
    string str="{greenplum:"+findDBInfo()+"},{RabbitMQ:"+findMQInfo()+"},{NTP:"+findNTPInfo()+"},{FTP:"+findFTPInfo()+"},{Stream:"+findStreamInfo()+"}";
    return str;
}

// 加密方法 ,使用base64加密3次
string encoder(string pwd) {
    for (int i = 0; i < 3; i++) {
        pwd=exec("echo '" + pwd + "'|base64|awk '{printf $1\",\"}'");
        int temp=pwd.find(",");
        pwd=pwd.substr(0,temp);
    }
    return pwd;
}

// 解密方法 ,使用base64解密3次
string decoder(string pwd) {
    for (int i = 0; i < 3; i++) {
        pwd=exec("echo '" + pwd + "'|base64 -d|awk '{printf $1\",\"}'");
        int temp=pwd.find(",");
        pwd=pwd.substr(0,temp);
    }
    return pwd;
}

