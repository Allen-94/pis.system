package com.yuyisz.pis.ui.vo;

import com.yuyisz.pis.ui.module.alarm.AlarmGrade;

public class AlarmForwardConfigVO {
	private long id;						//告警转发配置ID
	private int type;              	      	//配置类型，1：邮件发送类型，2：短信转发 ，3：向上级转发
	private String configName;     	      	//配置名称
	private AlarmGrade grade;	  	      	//转发告警级别
	private String sourceIds;   			//要转发的告警源，支持全部或单个
	private String configContent;	    	//配置内容，每种配置不相同，保存为字符串格式，各类型自己解析
	private String acceptors;		    	//接收者，可表示多个接收者，保存接收者ID，逗号分隔
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public AlarmGrade getGrade() {
		return grade;
	}
	public void setGrade(AlarmGrade grade) {
		this.grade = grade;
	}
	public String getSourceIds() {
		return sourceIds;
	}
	public void setSourceIds(String sourceIds) {
		this.sourceIds = sourceIds;
	}
	public String getConfigContent() {
		return configContent;
	}
	public void setConfigContent(String configContent) {
		this.configContent = configContent;
	}
	public String getAcceptors() {
		return acceptors;
	}
	public void setAcceptors(String acceptors) {
		this.acceptors = acceptors;
	}
}
