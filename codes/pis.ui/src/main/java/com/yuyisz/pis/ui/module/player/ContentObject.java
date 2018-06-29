package com.yuyisz.pis.ui.module.player;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuyisz.pis.ui.module.infosources.SystemVariable;
/**
 * 内容包的描述
 * 可以是媒资库资源，也可以是预定义系统变量
 * @author mengz
 *
 */
@Entity
@Table(name="t_content_object")
public class ContentObject {

	@Id
	private String id;
	private String contentName;
	
	@JsonIgnore
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,targetEntity=ContentPackage.class,optional=true)
	private ContentPackage cp;
	
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,targetEntity=SystemVariable.class)
	private SystemVariable mainContent;
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,targetEntity=SystemVariable.class)
	private SystemVariable backupContent;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public ContentPackage getCp() {
		return cp;
	}
	public void setCp(ContentPackage cp) {
		this.cp = cp;
	}
	
	public SystemVariable getMainContent() {
		return mainContent;
	}
	public void setMainContent(SystemVariable mainContent) {
		this.mainContent = mainContent;
	}
	public SystemVariable getBackupContent() {
		return backupContent;
	}
	public void setBackupContent(SystemVariable backupContent) {
		this.backupContent = backupContent;
	}
}
