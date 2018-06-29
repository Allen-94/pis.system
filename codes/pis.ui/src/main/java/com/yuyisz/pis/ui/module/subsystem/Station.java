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
 * 站点
 */
@Entity
@Table(name="station",uniqueConstraints= {@UniqueConstraint(columnNames= {"line_id","station_id"})})
public class Station implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;//主键
	
	@Column(name="line_id",nullable=false)
	private Integer line_id;//线路id
	
	@Column(name="station_id",nullable=false)
	private Integer station_id;//站点id
	
	@Column(name="site_number",nullable=false)
	private Integer site_number;//站点上下行
	
	@Column(name="x_pos",nullable=false)
	private Integer x_pos;//x轴位置
	
	@Column(name="y_pos",nullable=false)
	private Integer y_pos;//y轴位置
	
	@Column(name="station_name",nullable=false)
	private String station_name;//名称
	
	@Column(name="station_status",nullable=false)
	private Integer station_status;//状态
	
	@Column(name="station_inf",nullable=false)
	private String station_inf;//说明
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
	public Integer getSite_number() {
		return site_number;
	}
	public void setSite_number(Integer site_number) {
		this.site_number = site_number;
	}
	public Integer getX_pos() {
		return x_pos;
	}
	public void setX_pos(Integer x_pos) {
		this.x_pos = x_pos;
	}
	public Integer getY_pos() {
		return y_pos;
	}
	public void setY_pos(Integer y_pos) {
		this.y_pos = y_pos;
	}
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}
	public Integer getStation_status() {
		return station_status;
	}
	public void setStation_status(Integer station_status) {
		this.station_status = station_status;
	}
	public String getStation_inf() {
		return station_inf;
	}
	public void setStation_inf(String station_inf) {
		this.station_inf = station_inf;
	}
	
}
