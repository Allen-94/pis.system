package com.yuyisz.pis.ui.service.alarm;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.springframework.data.domain.Page;

import com.yuyisz.pis.ui.module.alarm.Alarm;
import com.yuyisz.pis.ui.module.alarm.AlarmForwardConfig;
import com.yuyisz.pis.ui.vo.AlarmParam;
import com.yuyisz.pis.ui.vo.HistoryAlarmCondition;

public interface AlarmService{
	
	/**
	 * 保存告警
	 * @return 
	 */
	
	public boolean saveAlarm(Alarm alarm);
	/**
	 * 当前告警分页查询
	 */
	public List<Alarm> findCurrentAlarmCriteria(AlarmParam param);
	public List<Alarm> findAllRealtimeAlarms(AlarmParam param);
	/**
	 * 当前告警 确认|取消确认
	 */
	List<Alarm> confirmAlarm(List<String> alarmId,boolean isConfirm);
	/**
	 * 当前告警清除|取消清除
	 * @param isClean 
	 */
	List<Alarm> cleanAlarm(List<String> alarmId, boolean isClean);
	/**
	 * 历史告警分页查询
	 */
	List<Alarm> findHistoryAlarmCriteria(AlarmParam param);
	public List<Alarm> findAllHistoryAlarms(HistoryAlarmCondition condition);
	public boolean remarkAlarm(String alarmId, String remark);
	public InputStream getRealTimeExcel(AlarmParam param,String type);
	public AlarmForwardConfig saveAlarmConfig(AlarmForwardConfig config);
	public List<AlarmForwardConfig> findForwardConfig();
	public AlarmForwardConfig findForwardConfigById(long id,int type);
	public void deleteConfig(long id, int type);
	public Alarm findAlarmById(String alarmId);
	
}
