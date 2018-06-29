package com.yuyisz.pis.ui.module.alarm;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;

/**
 * 告警类
 * @author mengz
 *
 */

@Entity
@Table(name="t_alarm")
public class Alarm{
	@Id
	private String id;
	
	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, optional = true)  
	@JoinColumn(name="SOURCE_ID")
	private DevResources source;	//告警源
	private String content;			//具体定位：告警详情，从告警信息和告警配置表中解析
	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH }, optional = true)   
    @JoinColumn(name="ALARM_CONFIG_ID")
	private AlarmConfig config;		//告警详情
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@Column(name="receipt_time")
	private Date receiptTime;		//告警时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@Column(name="confirm_time")
	private Date confirmTime; 		//确认时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@Column(name="clean_time")
	private Date cleanTime;					//清除时间
	@Column(name="confirm_status")
	private boolean confirmStatus=false;	//确认状态
	@Column(name="clean_status")
	private boolean cleanStatus=false;		//清除状态
	@ManyToOne(cascade = {CascadeType.REFRESH}, optional = true,targetEntity=User.class)   
    @JoinColumn(name="CONFIRM_USER_ID",nullable=true)
	private User confirmUser;		//确认人
	@ManyToOne(cascade = {CascadeType.REFRESH}, optional = true,targetEntity=User.class)   
    @JoinColumn(name="CLEAN_USER_ID",nullable=true)
	private User cleanUser;			//清除人
	private String remark;			//备注
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@Column(name="create_time")
	private Date createTime;	//创建时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@Column(name="update_time")
	private Date updateTime;	//修改时间
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DevResources getSource() {
		return source;
	}
	public void setSource(DevResources source) {
		this.source = source;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getReceiptTime() {
		return receiptTime;
	}
	public void setReceiptTime(Date receiptTime) {
		this.receiptTime = receiptTime;
	}
	public Date getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}
	public Date getCleanTime() {
		return cleanTime;
	}
	public void setCleanTime(Date cleanTime) {
		this.cleanTime = cleanTime;
	}
	public boolean isConfirmStatus() {
		return confirmStatus;
	}
	public void setConfirmStatus(boolean confirmStatus) {
		this.confirmStatus = confirmStatus;
	}
	
	public AlarmConfig getConfig() {
		return config;
	}
	public void setConfig(AlarmConfig config) {
		this.config = config;
	}
	public boolean isCleanStatus() {
		return cleanStatus;
	}
	public void setCleanStatus(boolean cleanStatus) {
		this.cleanStatus = cleanStatus;
	}
	public User getConfirmUser() {
		return confirmUser;
	}
	public void setConfirmUser(User confirmUser) {
		this.confirmUser = confirmUser;
	}
	public User getCleanUser() {
		return cleanUser;
	}
	public void setCleanUser(User cleanUser) {
		this.cleanUser = cleanUser;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
