#include <iostream>
#include "DBConn.h"
using namespace std;

int main()
{
    map_result res;
    Conn *postgres = new Conn("host=192.168.2.16 dbname=pis100 user=gpadmin password=gpadmin port=5432 connect_timeout=5");
    postgres->Exec("insert into t_runtimelogs values(nextval('t_runtimelogs_seq'),now(),'DBtest','DBtest','DBtest')");
    delete postgres;
    return 0;
}