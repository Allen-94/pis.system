package com.yuyisz.pis.ui.module.security;

import java.io.Serializable;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuyisz.pis.ui.util.ResourcesUtil;
/*
 * 命令
 */
@Entity
@Table(name="t_cmds")
public class Command implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CMD_ID")
	private int cmdId;
	@Column(name="CMD_NAME",nullable=false)
	private String cmdName;
	@Column(name="CMD_DESCRIPTION",nullable=false)
	private String cmd_description;
	
	@JsonIgnore
	@ManyToOne(cascade= {CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE}, optional = true)
	@JoinColumn(name="CMDSET_ID")
	private CommandSet cmdSet;
	
	@JsonIgnore
	@ManyToMany(fetch=FetchType.EAGER,cascade= {CascadeType.MERGE,CascadeType.REFRESH},targetEntity=Roles.class)
	@JoinTable(name="cmds_roles",
			joinColumns  = {@JoinColumn(name="cmd_id",referencedColumnName="CMD_ID")},
			inverseJoinColumns = {@JoinColumn(name="role_id",referencedColumnName="ROLE_ID")})
	private Set<Roles> roles;
	
	public int getCmdId() {
		return cmdId;
	}

	public void setCmdId(int cmdId) {
		this.cmdId = cmdId;
	}

	public String getCmdName() {
		return cmdName;
	}

	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}

	public String getCmd_description() {
		return cmd_description;
	}

	public void setCmd_description(String cmd_description) {
		this.cmd_description = cmd_description;
	}

	public CommandSet getCmdSet() {
		return cmdSet;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	public void setCmdSet(CommandSet cmdSet) {
		this.cmdSet = cmdSet;
	}
	
	
}
