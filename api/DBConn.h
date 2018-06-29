#ifndef _DBCONN_HPP_
#define _DBCONN_HPP_

#include <iostream>
#include <string>
#include <libpq-fe.h>
#include <map>
using namespace std;

typedef map<int,map<string,string> > map_result;

class Conn
{
public:
    Conn(char *connstring);
    ~Conn();
    map_result Fetch(char *SQL);
    int Exec(char *SQL);
private:
    void FinishConnection(PGconn *conn);
    void Reset();
private:
    PGconn *_conn;
    const char *_conninfo;
    PGresult *_res;
};


#endif // _DBCONN_HPP_