package com.yuyisz.pis.ui.module.reserveplan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;


/**
 * 紧急预案
 * @author mengz
 *
 */
@Entity
@Table(name="t_reservePlan")
public class ReservePlan {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String planName;
	private String planDesc;
	private String priority; //优先级
	private int level;	//预案级别    ， 设计了5级
	private boolean isApproved;//是否批准
	private boolean state;//预案状态，启动，未启动
	
	@Column(nullable=true)
	private String approveFlowId;//审批流程单id
	
	@ManyToMany(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,targetEntity=DevResources.class)	
	private Set<DevResources> players; //包含的播控
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER,targetEntity=ReserveCmd.class)	
	private Set<ReserveCmd> cmds;//包含的动作
	
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)	
	private Set<ReserverPlanTask> tasks;//包含的动作
	
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,optional=false,targetEntity=User.class)
	private User creater;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getPlanDesc() {
		return planDesc;
	}
	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public boolean isApproved() {
		return isApproved;
	}
	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	public Set<ReserverPlanTask> getTasks() {
		return tasks;
	}
	public void setTasks(Set<ReserverPlanTask> tasks) {
		this.tasks = tasks;
	}
	public Set<DevResources> getPlayers() {
		return players;
	}
	public String getApproveFlowId() {
		return approveFlowId;
	}
	public void setApproveFlowId(String approveFlowId) {
		this.approveFlowId = approveFlowId;
	}
	public void setPlayers(Set<DevResources> players) {
		this.players = players;
	}
	public Set<ReserveCmd> getCmds() {
		return cmds;
	}
	public void setCmds(Set<ReserveCmd> cmds) {
		this.cmds = cmds;
	}
	public User getCreater() {
		return creater;
	}
	public void setCreater(User creater) {
		this.creater = creater;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取按照步骤排序后的执行动作
	 * @return
	 */
	public List<ReserveCmd> getSortedCmds() {
		List<ReserveCmd> cmds = new ArrayList<>(this.cmds);
		Collections.sort(cmds, new Comparator<ReserveCmd>() {
			@Override
			public int compare(ReserveCmd o1, ReserveCmd o2) {
				return o1.getStep()<o2.getStep() ? -1 :1;
			}
		});
		return cmds;
	}
}
