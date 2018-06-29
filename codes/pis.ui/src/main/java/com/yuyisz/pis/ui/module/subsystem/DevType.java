package com.yuyisz.pis.ui.module.subsystem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 设备类型
 */
@Entity
@Table(name="dev_type")
public class DevType implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="dev_type",nullable=false)
	private Integer Dev_type;
	@Column(name="dev_description",nullable=false)
	private String Dev_description;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDev_type() {
		return Dev_type;
	}
	public void setDev_type(Integer dev_type) {
		Dev_type = dev_type;
	}
	public String getDev_description() {
		return Dev_description;
	}
	public void setDev_description(String dev_description) {
		Dev_description = dev_description;
	}
	
}
