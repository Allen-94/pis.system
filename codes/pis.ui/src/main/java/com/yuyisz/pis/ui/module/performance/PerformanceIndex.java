package com.yuyisz.pis.ui.module.performance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 性能指标类，设备要统计的性能指标项目，
 * 产生模板的统计项
 * @author mengz
 *
 */
@Entity
@Table(name="t_performance_index")
public class PerformanceIndex {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;			//采集项ID
	@Column(name="ITEM_NAME")
	private String itemName;	//采集项名称
	@Column(name="ITEM_CODE")
	private String itemCode;	//采集项代号
	@Column(name="DESCRIPTION")
	private String description; //采集项描述
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
}
