package com.yuyisz.pis.ui.module.subsystem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
/*
 * 显示屏
 */
@Entity
@Table(name="screen",uniqueConstraints= {@UniqueConstraint(columnNames= {"line_id","station_id","pc_id","screen_id"})})
public class Screen implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="line_id",nullable=false)
	private Integer line_id;
	@Column(name="station_id",nullable=false)
	private Integer station_id;
	@Column(name="pc_id",nullable=false)
	private Integer pc_id;
	@Column(name="screen_id",nullable=false)
	private Integer screen_id;
	@Column(name="screen_name",nullable=false)
	private String screen_name;
	@Column(name="screen_status",nullable=false)
	private Integer screen_status;
	@Column(name="screen_producer",nullable=false)
	private Integer screen_producer;
	@Column(name="screen_model",nullable=false)
	private Integer screen_model;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getLine_id() {
		return line_id;
	}
	public void setLine_id(Integer line_id) {
		this.line_id = line_id;
	}
	public Integer getStation_id() {
		return station_id;
	}
	public void setStation_id(Integer station_id) {
		this.station_id = station_id;
	}
	public Integer getPc_id() {
		return pc_id;
	}
	public void setPc_id(Integer pc_id) {
		this.pc_id = pc_id;
	}
	public Integer getScreen_id() {
		return screen_id;
	}
	public void setScreen_id(Integer screen_id) {
		this.screen_id = screen_id;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public Integer getScreen_status() {
		return screen_status;
	}
	public void setScreen_status(Integer screen_status) {
		this.screen_status = screen_status;
	}
	public Integer getScreen_producer() {
		return screen_producer;
	}
	public void setScreen_producer(Integer screen_producer) {
		this.screen_producer = screen_producer;
	}
	public Integer getScreen_model() {
		return screen_model;
	}
	public void setScreen_model(Integer screen_model) {
		this.screen_model = screen_model;
	}
	
}
