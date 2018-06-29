package com.yuyisz.pis.ui.message;

public class MsgBody {
	//消息命令
	private P2PMessageCommands messageCommand;
	//消息体
	private String messageBody;
	
	public MsgBody() {
	}
	
	public MsgBody(P2PMessageCommands messageCommand, String messageBody) {
		super();
		this.messageCommand = messageCommand;
		this.messageBody = messageBody;
	}
	public P2PMessageCommands getMessageCommand() {
		return messageCommand;
	}
	public void setMessageCommand(P2PMessageCommands messageCommand) {
		this.messageCommand = messageCommand;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	
}
