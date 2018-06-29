package com.yuyisz.pis.ui.module.alarm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 告警配置表，开发中输入内容，固定不变，版本向后兼容。
 * @author mengz
 *
 */
@Entity
@Table(name="t_alarm_config")
public class AlarmConfig {

	@Id
	private String id;
	
	@Column(name="ALARM_NAME")
	private String alarmName; //告警名称
	@Column(name="GRADE")
	private AlarmGrade grade; //告警级别
	
	@Column(name="P1")        //补充说明
	private String P1;
	@Column(name="P2")
	private String P2;
	@Column(name="P3")
	private String P3;
	@Column(name="P4")
	private String P4;
	@Column(name="P5")
	private String P5;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlarmName() {
		return alarmName;
	}
	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}
	
	public AlarmGrade getGrade() {
		return grade;
	}
	public void setGrade(AlarmGrade grade) {
		this.grade = grade;
	}
	public String getP1() {
		return P1;
	}
	public void setP1(String p1) {
		P1 = p1;
	}
	public String getP2() {
		return P2;
	}
	public void setP2(String p2) {
		P2 = p2;
	}
	public String getP3() {
		return P3;
	}
	public void setP3(String p3) {
		P3 = p3;
	}
	public String getP4() {
		return P4;
	}
	public void setP4(String p4) {
		P4 = p4;
	}
	public String getP5() {
		return P5;
	}
	public void setP5(String p5) {
		P5 = p5;
	}
}
