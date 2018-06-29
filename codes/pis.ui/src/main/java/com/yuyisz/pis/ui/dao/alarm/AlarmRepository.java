package com.yuyisz.pis.ui.dao.alarm;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.alarm.Alarm;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, String>, JpaSpecificationExecutor<Alarm> {

	/**
	 * 查询实时告警信息
	 * @param param
	 * @return
	 */
/*	@Query("select * from \r\n" + 
			"t_alarm alarm0_ where alarm0_.confirm_status=false and alarm0_.clean_status=false limit 10 offset \r\n" + 
			"10 ")
	List<Alarm> findRealtimeAlarms(AlarmParam param);
	@Query("select count(alarm0_.alarm_id) as col_0_0_ from t_alarm alarm0_ where alarm0_.confirm_status=false \r\n" + 
			"or alarm0_.clean_status=false")
	long getRealtimeAlarmCount(AlarmParam param);*/

}
