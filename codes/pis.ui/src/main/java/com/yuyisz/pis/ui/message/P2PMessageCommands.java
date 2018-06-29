package com.yuyisz.pis.ui.message;


/**
 * 系统消息命令  
 * 格式： 服务_模块_命令名
 * @author zm
 * 2017-11-20
 */
public enum P2PMessageCommands{
	DEVICE_MONITOR_GET("devicemonitorget"),
	DEVICE_MONITOR_PUT("devicemonitorput"),
	DEVICE_SCREENONOFFSET_ON("devicescreenonoffseron"),
	DEVICE_SCREENONOFFSET_OFF("devicescreenonoffseroff"),
	SECURITY_USERMANAGE_LIST("securityusermanagelist"),
	
	ADD_REAL_TIME_ALARM("addrealtimealarm"),	//接收告警
	CONFIRM_ALARM("confirmalarm"),				//确认告警
	CLEAN_ALARM("cleanalarm");					//清除告警
	
	
	String msgCommand;

	private P2PMessageCommands(String msgCommand) {
		this.msgCommand = msgCommand;
	}
	
	//根据命令名获取命令枚举
	public static P2PMessageCommands getByCommand(String command) {
		for(P2PMessageCommands msgCmd:P2PMessageCommands.values()) {
			if(msgCmd.getMsgCommand().equals(command)) {
				return msgCmd;
			}
		}
		return null;
	}
	
	public String getMsgCommand() {
		return msgCommand;
	}

	public void setMsgCommand(String msgCommand) {
		this.msgCommand = msgCommand;
	}
}
