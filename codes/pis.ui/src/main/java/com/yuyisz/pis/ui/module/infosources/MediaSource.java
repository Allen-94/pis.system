package com.yuyisz.pis.ui.module.infosources;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyisz.pis.ui.module.approveflow.ApproveFlow;
import com.yuyisz.pis.ui.module.security.User;
/**
 * 资源对象
 * @author mengz
 *
 */
@Entity
@Table(name="t_media_source",uniqueConstraints= {@UniqueConstraint(
		columnNames= {"mediaName","parent_source_id"})})
public class MediaSource {
	@Id
	private String id;	//id, uuid
	private String mediaName;
	private String describe;
	@ManyToOne(cascade= {CascadeType.ALL},fetch=FetchType.LAZY,targetEntity=MediaSource.class)
	@NotFound(action=NotFoundAction.IGNORE) //关联可为空， 表示没有上级，是根目录下的
	private MediaSource parentSource;
	private int mediaType;
	@ManyToOne(cascade= {CascadeType.REFRESH},fetch=FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)//为空表示还没开始授权
	private User manager;
	@ManyToOne(cascade= {CascadeType.REFRESH},fetch=FetchType.EAGER)
	private User creater;
	private String mediaFilePath;
	private int referenced;
	private int size; //KB，文件大小
	private String flow_id;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date effectiveDate;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMediaName() {
		return mediaName;
	}
	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public MediaSource getParentSource() {
		return parentSource;
	}
	public void setParentSource(MediaSource parentSource) {
		this.parentSource = parentSource;
	}
	public int getMediaType() {
		return mediaType;
	}
	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}
	public User getManager() {
		return manager;
	}
	public void setManager(User manager) {
		this.manager = manager;
	}
	public User getCreater() {
		return creater;
	}
	public void setCreater(User creater) {
		this.creater = creater;
	}
	public String getMediaFilePath() {
		return mediaFilePath;
	}
	public void setMediaFilePath(String mediaFilePath) {
		this.mediaFilePath = mediaFilePath;
	}
	public int getReferenced() {
		return referenced;
	}
	public void setReferenced(int referenced) {
		this.referenced = referenced;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getFlow_id() {
		return flow_id;
	}
	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
