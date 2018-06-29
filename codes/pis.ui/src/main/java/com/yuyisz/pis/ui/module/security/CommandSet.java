package com.yuyisz.pis.ui.module.security;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
/*
 * 命令集
 */
@Entity
@Table(name="t_cmdsets")
public class CommandSet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int cmdsetId;
	@Column(name="CMDSET_NAME",nullable=true)
	private String cmdsetName;
	@Column(name="CMDSET_DESCRIPTION",nullable=true)
	private String cmdset_discription;
	
	@JsonIgnore
	@OneToMany(cascade= {CascadeType.REFRESH,CascadeType.REFRESH,CascadeType.MERGE},mappedBy="cmdSet")
	private Set<Command> cmds;

	public int getCmdsetId() {
		return cmdsetId;
	}

	public void setCmdsetId(int cmdsetId) {
		this.cmdsetId = cmdsetId;
	}

	public String getCmdsetName() {
		return cmdsetName;
	}

	public void setCmdsetName(String cmdsetName) {
		this.cmdsetName = cmdsetName;
	}

	public String getCmdset_discription() {
		return cmdset_discription;
	}

	public void setCmdset_discription(String cmdset_discription) {
		this.cmdset_discription = cmdset_discription;
	}

	public Set<Command> getCmds() {
		return cmds;
	}

	public void setCmds(Set<Command> cmds) {
		this.cmds = cmds;
	}
	
	
}
