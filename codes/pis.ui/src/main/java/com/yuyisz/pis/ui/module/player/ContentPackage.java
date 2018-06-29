package com.yuyisz.pis.ui.module.player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import com.yuyisz.pis.ui.module.infosources.SystemVariable;
import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.module.security.User;

/**
 * 内容包
 * @author mengz
 *
 */
@Entity
@Table(name="t_contentpackage")
public class ContentPackage {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false,unique=true)
	private String contentName;
	private String description;
	private boolean isReferenced; //是否被引用
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,optional=false,targetEntity=User.class)
	private User creater;
	private Date createDate;
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY,targetEntity=ContentObject.class)
	private List<ContentObject> contents;
	
	public long getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public boolean isReferenced() {
		return isReferenced;
	}
	public void setReferenced(boolean isReferenced) {
		this.isReferenced = isReferenced;
	}
	public void setId(long id) {
		this.id = id;
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
	public List<ContentObject> getContents() {
		return contents;
	}
	public void setContents(List<ContentObject> contents) {
		this.contents = contents;
	}
	/**
	 * 获取内容包中涉及到的资源
	 * 内容包中的每一项内容都涉及到可能的资源，取出来
	 * @return
	 */
	public List<FileObject> getMediaResources() {
		List<FileObject> fos = new ArrayList<>();
		for(ContentObject co : contents) {
			SystemVariable main = co.getMainContent();
			SystemVariable backup = co.getBackupContent();
			//
			
		}
		return fos;
	}
}
