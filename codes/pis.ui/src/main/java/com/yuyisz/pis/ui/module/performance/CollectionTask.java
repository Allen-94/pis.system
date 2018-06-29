package com.yuyisz.pis.ui.module.performance;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;

/**
 * 设备监控任务
 * 有任务执行器调度
 * 系统启动或创建新任务时，获取任务对象，交给任务执行器，启动适当线程
 * 任务设置为不启用时，关闭已经启动的任务
 * @author mengz
 *
 */
@Entity
@Table(name="t_collection_task")
public class CollectionTask {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String taskName;						//任务名称
	private String effectiveStartDate="";			//有效起始日期
	private String effectiveEndDate="";				//有效结束日期
	private String collectStartTime="";				//采集开始时间
	private String collectEndTime="";				//采集结束时间
	private int collectDelay;						//采集间隔，分钟	
	@ManyToOne(cascade=CascadeType.REFRESH)
	private User createrUser;						//创建人
	private int status;								//状态，0：未启用，1：启用		
	private Date createDate;						//创建日期
	private Date updateDate;						//更新日期
	
	@ManyToOne(cascade=CascadeType.REFRESH)
	private User updateUser;						//更新人
	
	private long templateId;						//性能统计模板ID
	
	@OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY)
	private Set<CollectionItem> items;				//要统计的指标项目
	@OneToMany(cascade=CascadeType.MERGE,fetch=FetchType.LAZY)
	private Set<DevResources> devices;				//任务要统计的设备
	
	private boolean isAlive = true;					//是否删除 ，使用软删除
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
	public User getCreaterUser() {
		return createrUser;
	}
	public void setCreaterUser(User createrUser) {
		this.createrUser = createrUser;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public User getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}
	public boolean isAlive() {
		return isAlive;
	}
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	public Set<DevResources> getDevices() {
		return devices;
	}
	public void setDevices(Set<DevResources> devices) {
		this.devices = devices;
	}
	public long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}
	public Set<CollectionItem> getItems() {
		return items;
	}
	public void setItems(Set<CollectionItem> items) {
		this.items = items;
	}
	
	//判断当前性能采集任务是否还可以使用
	public boolean isCanUse() {
		
		
		return true;
	}
}
