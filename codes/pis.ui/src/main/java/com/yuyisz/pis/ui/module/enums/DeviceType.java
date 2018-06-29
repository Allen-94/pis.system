package com.yuyisz.pis.ui.module.enums;
/**
 * 设备类型枚举值
 * @author 郭芝辰
 *
 */
public enum DeviceType {
	ROOT(1,"全部线路"),
	LINE(2,"线路"),
	STATION(3,"站点"),
	STATION_SERVER(4,"车站服务器"),
	STATION_WORKSTATION(5,"车站工作站"),
	PLAYBACK_CONTROLLER(6,"播放控制器"),
	SCREEN(7,"显示屏")
	;
	int deviceType_enum;
	String deviceType_state;
	
	private DeviceType(int deviceType_enum,String deviceType_state) {
		this.deviceType_enum = deviceType_enum;
		this.deviceType_state = deviceType_state;
	}

	public int getDeviceType_enum() {
		return deviceType_enum;
	}

	public void setDeviceType_enum(int deviceType_enum) {
		this.deviceType_enum = deviceType_enum;
	}

	public String getDeviceType_state() {
		return deviceType_state;
	}

	public void setDeviceType_state(String deviceType_state) {
		this.deviceType_state = deviceType_state;
	}
	
	
}
