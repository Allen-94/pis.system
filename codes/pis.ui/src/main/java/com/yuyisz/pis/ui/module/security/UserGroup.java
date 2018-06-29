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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 用户组类型
 * @author 42254
 *	groupName：用户组名称
 *	description：组描述，可选
 */
@Entity
@Table(name="t_usergroups")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroup implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="GROUP_ID")
	private int groupId;
	@Column(name="GROUP_NAME",nullable=false)
	private String groupName;
	@Column(name="GROUP_DESCRIPTION",nullable=true)
	private String group_description;
	@JsonIgnore
	@SuppressWarnings("rawtypes")
	@ManyToMany(fetch=FetchType.EAGER,cascade = {CascadeType.MERGE,CascadeType.PERSIST},targetEntity=Roles.class)
	@JoinTable(name="group_roles",
			joinColumns= {@javax.persistence.JoinColumn(name = "user_group_id", referencedColumnName="GROUP_ID")},
			inverseJoinColumns= {@javax.persistence.JoinColumn(name = "role_id", referencedColumnName="ROLE_ID")})
	private Set<Roles> roles;
	
	//@JsonIgnore
	@OneToMany(cascade= {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE},mappedBy="userGroup",targetEntity=User.class)
	private Set<User> users;
	
	@JsonIgnore
	@OneToMany(mappedBy="userGroup",fetch=FetchType.LAZY,cascade= {CascadeType.REFRESH,CascadeType.MERGE,CascadeType.REMOVE,CascadeType.PERSIST},targetEntity=GroupField.class)
	private Set<GroupField> groupfields;
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public Set<Roles> getRoles() {
		return roles;
	}
	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroup_description() {
		return group_description;
	}
	public void setGroup_description(String group_description) {
		this.group_description = group_description;
	}
	
	public Set<GroupField> getGroupfields() {
		return groupfields;
	}
	public void setGroupfields(Set<GroupField> groupfields) {
		this.groupfields = groupfields;
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
