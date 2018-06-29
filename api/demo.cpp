#include "system-api.cpp"

int main(){
    cout << "===========1、获取数据库服务信息===========" << endl;
    cout << "===========e.g:192.168.2.16,gpadmin,gpadmin===========" << endl;
    string str=findDBInfo();
    cout << str << endl << endl;
    
    cout << "===========2、获取消息中心信息===========" << endl;
    cout << "===========e.g:192.168.2.32,rabbitmqadmin,rabbitmqadmin===========" << endl;
    str=findMQInfo();
    cout << str << endl << endl;
    
    cout << "===========3、获取FTP服务信息===========" << endl;
    cout << "===========e.g:[ftp1,192.168.2.35,ftpuser,ftpuser],[ftp2,192.168.2.37,ftpuser,ftpuser],[ftp3,192.168.2.68,ftpuser,ftpuser]===========" << endl;
    str=findFTPInfo();
    cout << str << endl << endl;
    
    cout << "===========4、获取NTP服务信息===========" << endl;
    cout << "===========e.g:192.168.2.36===========" << endl;
    str=findNTPInfo();
    cout << str << endl << endl;
    
    cout << "===========5、获取流媒体服务信息===========" << endl;
    cout << "===========e.g:[stream1,192.168.2.95,streamuser,streamuser],[stream2,192.168.2.99,ftpuser,ftpuser],[stream3,192.168.2.90,streamuser,streamuser]===========" << endl;
    str=findStreamInfo();
    cout << str << endl << endl;
    
    cout << "===========6、流媒体服务获取本身标识===========" << endl;
    cout << "===========e.g:stream1===========" << endl;
    str=findStreamFlag();
    cout << str << endl << endl;
    
    
    cout << "===========7、获取所有服务信息===========" << endl;
    str=findServiceInfo();
    cout << str << endl << endl;
    
    cout << "===========test===========" << endl;
    str=findServiceInfo("hadoop");
    cout << str << endl << endl;
    return 0;
}
