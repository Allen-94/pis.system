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
@Table(name = "pc", uniqueConstraints = { @UniqueConstraint(
		columnNames = { "line_id", "station_id", "pc_id" }) })
// 播放控制器
public class PlaybackController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	//线路id
	@Column(name="line_id",nullable=false)
	private Integer line_id;
	//站点id
	@Column(name="station_id",nullable=false)
	private Integer station_id;
	//播控器id
	@Column(name="pc_id",nullable=false)
	private Integer pc_id;
	//播控器名称
	@Column(name="pc_name",nullable=false)
	private String pc_name;
	//播控器状态
	@Column(name="pc_status",nullable=false)
	private Integer pc_status;
	//播控器位置
	@Column(name="pc_place",nullable=false)
	private Integer pc_place;
	//播控器厂家
	@Column(name="pc_producer",nullable=false)
	private Integer pc_producer;
	//播控器型号
	@Column(name="pc_model",nullable=false)
	private Integer pc_model;
	//播控器IP
	@Column(name="pc_ip",nullable=false)
	private String pc_ip;
	//播控器访问账号
	@Column(name="pc_account",nullable=false)
	private String pc_account;
	//播控器访问口令
	@Column(name="pc_password",nullable=false)
	private String pc_password;
	@OneToOne(cascade = {
			CascadeType.REFRESH,CascadeType.REMOVE}, mappedBy = "pc", targetEntity = PcConfig.class)
	private PcConfig pcConfig;
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

	public String getPc_name() {
		return pc_name;
	}

	public void setPc_name(String pc_name) {
		this.pc_name = pc_name;
	}

	public Integer getPc_status() {
		return pc_status;
	}

	public void setPc_status(Integer pc_status) {
		this.pc_status = pc_status;
	}

	public Integer getPc_place() {
		return pc_place;
	}

	public void setPc_place(Integer pc_place) {
		this.pc_place = pc_place;
	}

	public Integer getPc_producer() {
		return pc_producer;
	}

	public void setPc_producer(Integer pc_producer) {
		this.pc_producer = pc_producer;
	}

	public Integer getPc_model() {
		return pc_model;
	}

	public void setPc_model(Integer pc_model) {
		this.pc_model = pc_model;
	}

	public String getPc_ip() {
		return pc_ip;
	}

	public void setPc_ip(String pc_ip) {
		this.pc_ip = pc_ip;
	}

	public String getPc_account() {
		return pc_account;
	}

	public void setPc_account(String pc_account) {
		this.pc_account = pc_account;
	}

	public String getPc_password() {
		return pc_password;
	}

	public void setPc_password(String pc_password) {
		this.pc_password = pc_password;
	}

}
