package com.yuyisz.pis.ui.module.subsystem;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
/*
 * 车站升级任务
 */
@Entity
@Table(name="t_station_upgrade_task")
public class StationUpgradeTask implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer task_id;//任务ID
	
	@Column(name="task_name")
	private String task_name;//任务名
	
	@Column(name="task_type")
	private String task_type;//任务类型
	
	@Column(name="task_description")
	private String task_description;//任务描述
	
	@Column(name="exec_date")
	@JsonFormat(pattern="yyyy/MM/dd",timezone = "GMT+8")
	private Date exec_date;//执行日期
	
	@Column(name="period")
	private String period;//起止时间
	
	@Column(name="pc_group")
	private String pc_group;//播控组名称
	
	@Column(name="pc_name")
	private String pc_name;//播控名称
	
	@Column(name="original_version")
	private String original_version;//原版本号
	
	@Column(name="target_version")
	private String target_version;//目标版本号
	
	@Column(name="creater")
	private String creater;//创建人
	
	@Column(name="upgrade_status")
	private String upgrade_status;//状态

	public Integer getTask_id() {
		return task_id;
	}

	public void setTask_id(Integer task_id) {
		this.task_id = task_id;
	}

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}

	public String getTask_type() {
		return task_type;
	}

	public void setTask_type(String task_type) {
		this.task_type = task_type;
	}

	public String getTask_description() {
		return task_description;
	}

	public void setTask_description(String task_description) {
		this.task_description = task_description;
	}

	public Date getExec_date() {
		return exec_date;
	}

	public void setExec_date(Date exec_date) {
		this.exec_date = exec_date;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getPc_group() {
		return pc_group;
	}

	public void setPc_group(String pc_group) {
		this.pc_group = pc_group;
	}

	public String getOriginal_version() {
		return original_version;
	}

	public void setOriginal_version(String original_version) {
		this.original_version = original_version;
	}

	public String getTarget_version() {
		return target_version;
	}

	public void setTarget_version(String target_version) {
		this.target_version = target_version;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getUpgrade_status() {
		return upgrade_status;
	}

	public void setUpgrade_status(String upgrade_status) {
		this.upgrade_status = upgrade_status;
	}

	public String getPc_name() {
		return pc_name;
	}

	public void setPc_name(String pc_name) {
		this.pc_name = pc_name;
	}
	
}
