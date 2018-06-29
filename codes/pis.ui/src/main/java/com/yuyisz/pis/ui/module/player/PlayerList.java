package com.yuyisz.pis.ui.module.player;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyisz.pis.ui.module.approveflow.ApproveFlow;
import com.yuyisz.pis.ui.module.security.User;

/**
 * 播放列表
 * @author mengz
 *
 */
@Entity
@Table(name="t_playlist")
public class PlayerList {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false,unique=true)
	private String playListName;
	private String description;
	private boolean isDefault; //是否是缺省播表
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date startDate;
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date endDate;
	private boolean isApproved;//是否批准
	
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER,optional=false,targetEntity=ApproveFlow.class)
	private ApproveFlow approveFlow;//审批流程单
	
	private boolean isSended; //是否下发
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,optional=false,targetEntity=User.class)
	private User creater;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createDate;
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.EAGER,targetEntity=PlayerRole.class,mappedBy="playList")
	private Set<PlayerRole> playRoles; //播放列表包含的规则
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPlayListName() {
		return playListName;
	}
	public void setPlayListName(String playListName) {
		this.playListName = playListName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public boolean isApproved() {
		return isApproved;
	}
	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	public boolean isSended() {
		return isSended;
	}
	public void setSended(boolean isSended) {
		this.isSended = isSended;
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
	public Set<PlayerRole> getPlayRoles() {
		return playRoles;
	}
	public void setPlayRoles(Set<PlayerRole> playRoles) {
		this.playRoles = playRoles;
	}
	public ApproveFlow getApproveFlow() {
		return approveFlow;
	}
	public void setApproveFlow(ApproveFlow approveFlow) {
		this.approveFlow = approveFlow;
	}
}
