package com.yuyisz.pis.ui.module.reserveplan;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;

/**
 * 预案执行任务类
 * @author mengz
 *
 */
@Entity
@Table(name="t_reserver_plan_task")
public class ReserverPlanTask {

	@Id
	private String id;
	private String actionName; 			//指令名称
	private String actionContent;		//指令内容
	private int step;					//步骤
	private String content;				//播放信息
	private int reservePlanId;			//关联的预案ID
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER)
	private DevResources player;		//关联的播控/设备ID
	private int state; 					//指令执行状态 0：未执行，1：正在执行，2:完成 ，3：失败
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createDate; 			//执行日期
	@JsonIgnore
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,optional=true,targetEntity=User.class)
	private User runner;				//执行人
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getReservePlanId() {
		return reservePlanId;
	}
	public void setReservePlanId(int reservePlanId) {
		this.reservePlanId = reservePlanId;
	}
	public DevResources getPlayer() {
		return player;
	}
	public void setPlayer(DevResources player) {
		this.player = player;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getActionContent() {
		return actionContent;
	}
	public void setActionContent(String actionContent) {
		this.actionContent = actionContent;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public User getRunner() {
		return runner;
	}
	public void setRunner(User runner) {
		this.runner = runner;
	}
}
