package com.yuyisz.pis.ui.module.security;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * 角色
 * @author 郭芝辰
 *
 */
@Entity
@Table(name="t_roles")
public class Roles implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ROLE_ID")
	private int roleId;
	@Column(name="ROLE_NAME",nullable=false)
	private String roleName;
	@Column(name="ROLE_DESCRIPTION",nullable=false)
	private String role_description;
	@JsonIgnore
	@ManyToMany(fetch=FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.REFRESH},targetEntity=UserGroup.class)
	@JoinTable(name="group_roles",
			inverseJoinColumns= {@javax.persistence.JoinColumn(name = "user_group_id", referencedColumnName="GROUP_ID")},
			joinColumns= {@javax.persistence.JoinColumn(name = "role_id", referencedColumnName="ROLE_ID")})
	private Set<UserGroup> userGroups;
	
	@JsonIgnore
	@ManyToMany(cascade= {CascadeType.REFRESH,CascadeType.MERGE},targetEntity=Command.class,fetch=FetchType.EAGER)
	@JoinTable(name="cmds_roles",
			inverseJoinColumns  = {@JoinColumn(name="cmd_id",referencedColumnName="CMD_ID")},
			joinColumns = {@JoinColumn(name="role_id",referencedColumnName="ROLE_ID")})
	private Set<Command> cmds;
	
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	public Set<UserGroup> getUserGroups() {
		return userGroups;
	}
	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRole_description() {
		return role_description;
	}
	public void setRole_description(String role_description) {
		this.role_description = role_description;
	}
	public Set<Command> getCmds() {
		return cmds;
	}
	public void setCmds(Set<Command> cmds) {
		this.cmds = cmds;
	}


	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	protected Date createTime;	//创建时间
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	protected Date updateTime;	//修改时间
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
