package com.yuyisz.pis.ui.module.subsystem;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.yuyisz.pis.ui.module.security.DevResources;

@Entity
@Table(name = "t_stationserverconfigs")
// 设备拓展配置信息
public class StationServerConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	// 版本号
	@Column(name = "version")
	private String version = "0001";
	
	public StationServerConfig() {
		super();
	}
	public StationServerConfig(String version, StationServer stationServer) {
		super();
		this.version = version;
		this.stationServer = stationServer;
	}
	// 关联的设备资源
	@OneToOne(cascade = {
			CascadeType.REFRESH },  targetEntity = StationServer.class)
	@JoinColumn(name="stationserver_id")
	private StationServer stationServer;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public StationServer getStationServer() {
		return stationServer;
	}
	public void setStationServer(StationServer stationServer) {
		this.stationServer = stationServer;
	}

}
