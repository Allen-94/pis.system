package com.yuyisz.pis.ui.module.subsystem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="line",uniqueConstraints= {@UniqueConstraint(columnNames= {"line_id"})})
/*
 * 线路
 */
public class Line implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="line_id",nullable=false)
	private Integer line_id;
	@Column(name="line_name",nullable=false)
	private String line_name;
	@Column(name="line_status",nullable=false)
	private Integer line_status;
	@Column(name="line_inf",nullable=false)
	private String line_inf;
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
	public String getLine_name() {
		return line_name;
	}
	public void setLine_name(String line_name) {
		this.line_name = line_name;
	}
	public Integer getLine_status() {
		return line_status;
	}
	public void setLine_status(Integer line_status) {
		this.line_status = line_status;
	}
	public String getLine_inf() {
		return line_inf;
	}
	public void setLine_inf(String line_inf) {
		this.line_inf = line_inf;
	}
	
}
