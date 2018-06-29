package com.yuyisz.pis.ui.vo;

/**
 * 性能采集信息模板前端参数VO
 * @author mengz
 *
 */
public class CollectionTemplateVO {

	private long id;
	private String templateName;
	private String description;
	private int deviceType;
	private String indexIds;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	public String getIndexIds() {
		return indexIds;
	}
	public void setIndexIds(String indexIds) {
		this.indexIds = indexIds;
	}
}
