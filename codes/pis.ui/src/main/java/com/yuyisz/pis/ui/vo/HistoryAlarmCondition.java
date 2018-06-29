package com.yuyisz.pis.ui.vo;

/**
 * 查询历史告警的条件
 * @author mengz
 *
 */
public class HistoryAlarmCondition {

	private int grade;			//告警级别
	private int timeType;		//告警时间类型    0：全部   ，1：最近一天，2：最近三天，3：最近一周，4：最近一月
	private int sourceId;		//告警源的ID
	
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getTimeType() {
		return timeType;
	}
	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
}
