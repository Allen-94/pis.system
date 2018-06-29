package com.yuyisz.pis.ui.module.enums;
/**
 * 设备厂家
 * @author 郭芝辰
 *
 */
public enum DeviceProducer {
	HUAWEI(0,"华为"),
	ZHONGXING(1,"中兴"),
	HTC(2,"HTC");
	
	String producer_state;//说明
	int producer_enum;//枚举值
	
	private DeviceProducer(int enums,String state) {
		this.producer_enum = enums;
		this.producer_state = state;
	}

	public String getProducer_state() {
		return producer_state;
	}

	public void setProducer_state(String producer_state) {
		this.producer_state = producer_state;
	}

	public int getProducer_enum() {
		return producer_enum;
	}

	public void setProducer_enum(int producer_enum) {
		this.producer_enum = producer_enum;
	}
	
	
}
