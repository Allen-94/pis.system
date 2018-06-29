package com.yuyisz.pis.ui.module.subsystem;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.yuyisz.pis.ui.module.security.DevResources;
@Entity
@Table(name="stationserver",uniqueConstraints= {@UniqueConstraint(columnNames={"line_id","station_id","station_server_id"})})
public class StationServer implements Serializable{
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
	
	@Column(name="station_server_name",nullable=false)
	private String station_server_name;
	
	@Column(name="station_server_status",nullable=false)
	private Integer station_server_status;
	
	@Column(name="station_server_is_used",nullable=false)
	private Integer station_server_is_used;
	
	@Column(name="station_server_producer",nullable=false)
	private Integer station_server_producer;
	
	@Column(name="station_server_model",nullable=false)
	private Integer station_server_model;
	
	@Column(name="station_server_ip",nullable=false)
	private String station_server_ip;
	
	@Column(name="station_server_account",nullable=false)
	private String station_server_account;
	
	@Column(name="station_server_password",nullable=false)
	private String station_server_password;
	
	@OneToOne(cascade = {
			CascadeType.REFRESH ,CascadeType.REMOVE}, mappedBy = "stationServer", targetEntity = StationServerConfig.class)
	private StationServerConfig stationServerConfig;
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
	public String getStation_server_name() {
		return station_server_name;
	}
	public void setStation_server_name(String station_server_name) {
		this.station_server_name = station_server_name;
	}
	public Integer getStation_server_status() {
		return station_server_status;
	}
	public void setStation_server_status(Integer station_server_status) {
		this.station_server_status = station_server_status;
	}
	public Integer getStation_server_is_used() {
		return station_server_is_used;
	}
	public void setStation_server_is_used(Integer station_server_is_used) {
		this.station_server_is_used = station_server_is_used;
	}
	public Integer getStation_server_producer() {
		return station_server_producer;
	}
	public void setStation_server_producer(Integer station_server_producer) {
		this.station_server_producer = station_server_producer;
	}
	public Integer getStation_server_model() {
		return station_server_model;
	}
	public void setStation_server_model(Integer station_server_model) {
		this.station_server_model = station_server_model;
	}
	public String getStation_server_ip() {
		return station_server_ip;
	}
	public void setStation_server_ip(String station_server_ip) {
		this.station_server_ip = station_server_ip;
	}
	public String getStation_server_account() {
		return station_server_account;
	}
	public void setStation_server_account(String station_server_account) {
		this.station_server_account = station_server_account;
	}
	public String getStation_server_password() {
		return station_server_password;
	}
	public void setStation_server_password(String station_server_password) {
		this.station_server_password = station_server_password;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
