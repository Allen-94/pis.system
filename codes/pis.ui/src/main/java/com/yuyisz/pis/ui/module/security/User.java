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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yuyisz.pis.ui.module.alarm.Alarm;
import com.yuyisz.pis.ui.service.security.UserService;
/**
 * 
 * 用户
 *
 */
@Entity
@Table(name="t_users")
//@JsonIgnoreProperties(value= {"cleanAlarms","cleanAlarms"})
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer userId;
	@Column(name="ACCOUNT",nullable=false)
	private String account;
	@Column(name="PASSWORD",nullable=false)
	private String password;
	@Column(name="REAL_NAME",nullable=false)
	private String realname;
	@Column(name="PHONE",nullable=false)
	private String phone;
	@Column(name="GENDER",nullable=false)
	private String gender;
	@Column(name="EMAIL",nullable=false)
	private String email;
	
	/*@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE},mappedBy ="confirmUser")
	private Set<Alarm> confirmAlarms;
	@OneToMany(cascade = { CascadeType.REFRESH, CascadeType.PERSIST,CascadeType.MERGE},mappedBy ="cleanUser")
	private Set<Alarm> cleanAlarms;*/
	
	@ManyToOne(cascade= {CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST}, optional = true,targetEntity=UserGroup.class)
	@JoinColumn(name="GROUP_ID")
	@JsonIgnore
	private UserGroup userGroup;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

	/*public Set<Alarm> getConfirmAlarms() {
		return confirmAlarms;
	}

	public void setConfirmAlarms(Set<Alarm> confirmAlarms) {
		this.confirmAlarms = confirmAlarms;
	}

	public Set<Alarm> getCleanAlarms() {
		return cleanAlarms;
	}

	public void setCleanAlarms(Set<Alarm> cleanAlarms) {
		this.cleanAlarms = cleanAlarms;
	}
	*/
	/*public Integer getGroupId() {
		if(this.userGroup!=null) {
			return this.userGroup.getGroupId();
		}
		return -1;
	}*/
	
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

	public byte[] getCredentialsSalt() {
		// TODO Auto-generated method stub
		return null;
	}
	
}