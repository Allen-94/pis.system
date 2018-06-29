package com.yuyisz.pis.ui.vo;

/**
 * 查询实时告警的条件
 * @author mengz
 *
 */
public class RealTimeAlarmCondition {

	private int grade;			//告警级别
	private int confirm;		//是否确认
	private int clean;			//是否清楚   //确认和清除不可以都为真
	private int timeType;		//告警时间类型    0：全部   ，1：最近一天，2：最近三天，3：最近一周，4：最近一月
	private int sourceId;		//告警源的ID
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public int getConfirm() {
		return confirm;
	}
	public void setConfirm(int confirm) {
		this.confirm = confirm;
	}
	public int getClean() {
		return clean;
	}
	public void setClean(int clean) {
		this.clean = clean;
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
