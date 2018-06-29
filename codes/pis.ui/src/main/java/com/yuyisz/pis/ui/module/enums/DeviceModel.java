package com.yuyisz.pis.ui.module.enums;
/**
 * 设备型号 
 * @author 郭芝辰
 *
 */
public enum DeviceModel {
	PC_MODEL_001(1,"默认型号1"),
	PC_MODEL_002(2,"默认型号2"),
	PC_MODEL_003(3,"默认型号3");
	
	int device_model_enum;
	String device_model_state;
	private DeviceModel(int enums,String state) {
		this.device_model_enum = enums;
		this.device_model_state = state;
	}
	public int getDevice_model_enum() {
		return device_model_enum;
	}
	public void setDevice_model_enum(int device_model_enum) {
		this.device_model_enum = device_model_enum;
	}
	public String getDevice_model_state() {
		return device_model_state;
	}
	public void setDevice_model_state(String device_model_state) {
		this.device_model_state = device_model_state;
	}
	
	public static String get_model_state(int device_model_enum) {
		DeviceModel[] values = DeviceModel.values();
		for (DeviceModel deviceModel : values) {
			if(deviceModel.getDevice_model_enum()==device_model_enum) {
				return deviceModel.device_model_state;
			}
		}
		return "";
	}
	
	
}
