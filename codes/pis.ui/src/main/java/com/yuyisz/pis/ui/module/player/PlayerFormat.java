package com.yuyisz.pis.ui.module.player;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.module.security.User;


/**
 * 播表版式
 * @author mengz
 *
 */
@Entity
@Table(name="t_playformat")
public class PlayerFormat {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false,unique=true)
	private String playFormatName;
	private String description;
	private boolean isReferenced; //是否被引用
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,optional=true,targetEntity=User.class)
	private User creater;
	private Date createDate;
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER,targetEntity=FileObject.class,optional=true)
	private FileObject fileObject;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPlayFormatName() {
		return playFormatName;
	}
	public void setPlayFormatName(String playFormatName) {
		this.playFormatName = playFormatName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isReferenced() {
		return isReferenced;
	}
	public void setReferenced(boolean isReferenced) {
		this.isReferenced = isReferenced;
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
	public FileObject getFileObject() {
		return fileObject;
	}
	public void setFileObject(FileObject fileObject) {
		this.fileObject = fileObject;
	}
}
