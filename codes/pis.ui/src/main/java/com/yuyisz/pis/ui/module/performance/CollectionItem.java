package com.yuyisz.pis.ui.module.performance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 性能采集项，描述系统支持的采集数据类型
 * @author mengz
 *
 */
@Entity
@Table(name="t_collection_item")
public class CollectionItem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;			//采集项ID
	@Column(name="ITEM_NAME",nullable=false)
	private String itemName;	//采集项名称
	@Column(name="ITEM_CODE",nullable=false)
	private String itemCode;	//采集项代号
	@Column(name="DESCRIPTION")
	private String description; //采集项描述
	@Column(name="LOWER_ALARM")
	private boolean lowerAlarm;	//下限告警
	@Column(name="LOWER_THRESHOLD")
	private int lowerThreshold;	//下限阈值
	@Column(name="UPPER_ALARM")
	private boolean upperAlarm;	//下限告警
	@Column(name="UPPER_THRESHOLD")
	private int upperThreshold;	//下限阈值
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isLowerAlarm() {
		return lowerAlarm;
	}
	public void setLowerAlarm(boolean lowerAlarm) {
		this.lowerAlarm = lowerAlarm;
	}
	public int getLowerThreshold() {
		return lowerThreshold;
	}
	public void setLowerThreshold(int lowerThreshold) {
		this.lowerThreshold = lowerThreshold;
	}
	public boolean isUpperAlarm() {
		return upperAlarm;
	}
	public void setUpperAlarm(boolean upperAlarm) {
		this.upperAlarm = upperAlarm;
	}
	public int getUpperThreshold() {
		return upperThreshold;
	}
	public void setUpperThreshold(int upperThreshold) {
		this.upperThreshold = upperThreshold;
	}
}
