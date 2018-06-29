#include "DBConn.h"

Conn::Conn(char *connstring)
{
    _conninfo = connstring;
    _conn = PQconnectdb(_conninfo);
    if (PQstatus(_conn) != CONNECTION_OK)
    {
        fprintf(stderr, "Could not connect to db/n%s", PQerrorMessage(_conn));
        FinishConnection(_conn);
    }
}
Conn::~Conn()
{
    _conninfo = NULL;
    FinishConnection(_conn);
}
void Conn::Reset()
{  
    PQfinish(_conn);
    _conn = PQconnectdb(_conninfo);
}

void Conn::FinishConnection(PGconn *conn)
{  
    PQfinish(_conn);
}
 
map_result Conn::Fetch(char *SQL)
{  
    int row, col;
    map_result results;
    map<string,string> pairs;
    // 检查连接
    if (PQstatus(_conn) != CONNECTION_OK)
    {
        this->Reset();
    }
    // 开始一个事物 
    _res = PQexec(_conn, "BEGIN");
    if (PQresultStatus(_res) != PGRES_COMMAND_OK)
    {
        fprintf(stderr, "Failed to BEGIN transaction /n%s", PQerrorMessage(_conn));
        PQclear(_res);
        FinishConnection(_conn);
    }
    // 建立游标 
    string FinalSQL = string("DECLARE myportal CURSOR FOR ") +  
                      string(SQL);
    _res = PQexec(_conn,FinalSQL.c_str());
    if (PQresultStatus(_res) != PGRES_COMMAND_OK)
    {  
        fprintf(stderr, "QUERY FAILED/n%s/n", PQerrorMessage(_conn));
        PQclear(_res);
        FinishConnection(_conn);
    }
    PQclear(_res);
 
    _res = PQexec(_conn, "FETCH ALL in myportal");
    if (PQresultStatus(_res) != PGRES_TUPLES_OK)
    {
        fprintf(stderr, "FETCH ALL failed/n%s/n", PQerrorMessage(_conn));
        PQclear(_res);
        FinishConnection(_conn);
    }
    else  
    {
        for (row=0;row<PQntuples(_res);row++)
        {
            for(col=0;col<PQnfields(_res);col++)
            {
                pairs[PQfname(_res,col)] = PQgetvalue(_res, row, col);
            }
            results[row] = pairs;
        }
    }
    // 结束一个事物 
    _res = PQexec(_conn, "END");
    if (PQresultStatus(_res) != PGRES_COMMAND_OK)
    {
        fprintf(stderr, "Failed to END transaction /n%s", PQerrorMessage(_conn));
        PQclear(_res);
        FinishConnection(_conn);
    }
    return results;
}
int Conn::Exec(char *sql)
{
    // 检查连接
    if (PQstatus(_conn) != CONNECTION_OK)
    {
        this->Reset();
    }
    // 开始一个事物
    _res = PQexec(_conn, "BEGIN");
    if (PQresultStatus(_res) != PGRES_COMMAND_OK)
    {
        fprintf(stderr, "Failed to BEGIN transaction /n%s/n", PQerrorMessage(_conn));
        PQclear(_res);
        FinishConnection(_conn);
    }
    // 执行插入
    _res = PQexec(_conn, sql);
    if (PQresultStatus(_res) != PGRES_COMMAND_OK)
    {
        fprintf(stderr, "Failed to execute INSERT /n%s/n", PQerrorMessage(_conn));
        PQclear(_res);
        FinishConnection(_conn);
    }
    // 提交事物
    _res = PQexec(_conn, "COMMIT");
    if (PQresultStatus(_res) != PGRES_COMMAND_OK) 
    {
        fprintf(stderr, "Failed to COMMIT transaction/n%s/n", PQerrorMessage(_conn));
        PQclear(_res);
        FinishConnection(_conn);
    }
    // 结束一个事物 
    _res = PQexec(_conn, "END");
    if (PQresultStatus(_res) != PGRES_COMMAND_OK)
    {
        fprintf(stderr, "Failed to END transaction /n%s", PQerrorMessage(_conn));
        PQclear(_res);
        FinishConnection(_conn);
    }
    return 0;
}