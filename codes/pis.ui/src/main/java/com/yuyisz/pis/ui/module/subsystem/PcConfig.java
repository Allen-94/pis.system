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
@Table(name = "t_pcconfigs")
// 设备拓展配置信息
public class PcConfig implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id ;
	// 版本号
	@Column(name = "version")
	private String version = "001";
	// 是否定时开关
	@Column(name = "is_auto_switch")
	private String isAutoSwitch = "是";
	// 开关时间
	@Column(name = "pc_switch_time")
	private String pcSwitchTime = "8:00-22:00";
	// 是否控制屏开关
	@Column(name = "is_screen_auto_switch")
	private String isScreenAutoSwitch = "是";
	// 控制屏开关时间
	@Column(name = "screen_switch_time")
	private String screenSwitchTime = "8:00-22:00";
	// 关联的设备资源
	@OneToOne(cascade = {
			CascadeType.REFRESH,CascadeType.REMOVE },  targetEntity = PlaybackController.class)
	@JoinColumn(name="pc_id")
	private PlaybackController pc;
	
	public PcConfig() {
		super();
	}
	
	public PcConfig(String version, String isAutoSwitch, String pcSwitchTime, String isScreenAutoSwitch,
			String screenSwitchTime, PlaybackController pc) {
		super();
		this.version = version;
		this.isAutoSwitch = isAutoSwitch;
		this.pcSwitchTime = pcSwitchTime;
		this.isScreenAutoSwitch = isScreenAutoSwitch;
		this.screenSwitchTime = screenSwitchTime;
		this.pc = pc;
	}


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
	public String getIsAutoSwitch() {
		return isAutoSwitch;
	}
	public void setIsAutoSwitch(String isAutoSwitch) {
		this.isAutoSwitch = isAutoSwitch;
	}
	public String getPcSwitchTime() {
		return pcSwitchTime;
	}
	public void setPcSwitchTime(String pcSwitchTime) {
		this.pcSwitchTime = pcSwitchTime;
	}
	public String getIsScreenAutoSwitch() {
		return isScreenAutoSwitch;
	}
	public void setIsScreenAutoSwitch(String isScreenAutoSwitch) {
		this.isScreenAutoSwitch = isScreenAutoSwitch;
	}
	public String getScreenSwitchTime() {
		return screenSwitchTime;
	}
	public void setScreenSwitchTime(String screenSwitchTime) {
		this.screenSwitchTime = screenSwitchTime;
	}
	public PlaybackController getPc() {
		return pc;
	}
	public void setPc(PlaybackController pc) {
		this.pc = pc;
	}

}
