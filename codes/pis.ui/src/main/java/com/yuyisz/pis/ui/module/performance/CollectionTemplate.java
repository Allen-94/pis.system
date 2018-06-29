package com.yuyisz.pis.ui.module.performance;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 性能采集模板，针对某种设备
 * @author mengz
 *
 */
@Entity
@Table(name="t_collection_template")
public class CollectionTemplate {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;	
	@Column(name="TEMPLATE_NAME")
	private String templateName; 	//模板名称
	@Column(name="DESCRIPTION")
	private String description;		//模板描述
	@Column(name="DEVICE_TYPE")
	private int deviceType;			//采集设备类型
	
	@OneToMany(cascade=CascadeType.ALL)
	private Set<CollectionItem> items;//模板包含的指标
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<CollectionItem> getItems() {
		return items;
	}
	public void setItems(Set<CollectionItem> items) {
		this.items = items;
	}
}
