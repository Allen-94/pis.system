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
 * 工作站
 */
@Entity
@Table(name="station_workstation",uniqueConstraints= {@UniqueConstraint(columnNames= {"line_id","station_id","station_server_id","ws_id"})})
public class StationWorkStation implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Column(name="line_id",nullable=false)
	private Integer line_id;
	@Column(name="station_id",nullable=false)
	private Integer station_id;
	@Column(name="station_server_id",nullable=false)
	private Integer station_server_id;
	@Column(name="ws_id",nullable=false)
	private Integer station_workstation_id;
	@Column(name="station_ws_name",nullable=false)
	private String station_workstation_name;
	@Column(name="station_ws_account",nullable=false)
	private String station_workstation_account;
	@Column(name="station_ws_password",nullable=false)
	private String station_workstation_password;
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
	public Integer getStation_server_id() {
		return station_server_id;
	}
	public void setStation_server_id(Integer station_server_id) {
		this.station_server_id = station_server_id;
	}
	public Integer getStation_workstation_id() {
		return station_workstation_id;
	}
	public void setStation_workstation_id(Integer station_workstation_id) {
		this.station_workstation_id = station_workstation_id;
	}
	public String getStation_workstation_name() {
		return station_workstation_name;
	}
	public void setStation_workstation_name(String station_workstation_name) {
		this.station_workstation_name = station_workstation_name;
	}
	public String getStation_workstation_account() {
		return station_workstation_account;
	}
	public void setStation_workstation_account(String station_workstation_account) {
		this.station_workstation_account = station_workstation_account;
	}
	public String getStation_workstation_password() {
		return station_workstation_password;
	}
	public void setStation_workstation_password(String station_workstation_password) {
		this.station_workstation_password = station_workstation_password;
	}
	
}
