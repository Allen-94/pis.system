package com.yuyisz.pis.ui.dao.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.security.OperationLog;

@Repository
public interface OperationLogRepository extends JpaSpecificationExecutor<OperationLog>, JpaRepository<OperationLog, Integer> {

	List<OperationLog> findByOperator(String name);
	/**
	 * 条件过滤
	 * @param time
	 * @param operator
	 * @return
	 */
	@Query(value="select * from t_operationlogs where operator like %?1% "
			+ "and client_ip like %?2% "
			+ "and to_char(create_time,'yyyy-MM-dd HH:mm:ss') like %?3% "
			+ "and command like %?4% and result like %?5%",nativeQuery=true)
	List<OperationLog> findByOperatorLike(String operator,String clientIP,String createTime,
			String command,String result);
	
	@Query(value="select * from t_operationlogs where to_char(create_time,'yyyy-MM-dd HH:mm:ss') like %?1%",nativeQuery=true)
	List<OperationLog> findbyDate(String createTime);
}
