package com.yuyisz.pis.ui.vo;

public class CollectionTaskVO {
	private long id;
	private String taskName;						//任务名称
	private String effectiveStartDate;			//有效起始日期
	private String effectiveEndDate;				//有效结束日期
	private String collectStartTime;				//采集开始时间
	private String collectEndTime;				//采集结束时间
	private int collectDelay;						//采集间隔，分钟	
	private int status;								//状态，0：未启用，1：启用		
	private long templateId;						//性能统计模板ID
	private String items;				//要统计的指标项目
	private String devices;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getEffectiveStartDate() {
		return effectiveStartDate;
	}
	public void setEffectiveStartDate(String effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}
	public String getEffectiveEndDate() {
		return effectiveEndDate;
	}
	public void setEffectiveEndDate(String effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
	public String getCollectStartTime() {
		return collectStartTime;
	}
	public void setCollectStartTime(String collectStartTime) {
		this.collectStartTime = collectStartTime;
	}
	public String getCollectEndTime() {
		return collectEndTime;
	}
	public void setCollectEndTime(String collectEndTime) {
		this.collectEndTime = collectEndTime;
	}
	public int getCollectDelay() {
		return collectDelay;
	}
	public void setCollectDelay(int collectDelay) {
		this.collectDelay = collectDelay;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	public String getItems() {
		return items;
	}
	public void setItems(String items) {
		this.items = items;
	}
	public String getDevices() {
		return devices;
	}
	public void setDevices(String devices) {
		this.devices = devices;
	}	
}
