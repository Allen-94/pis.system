package com.yuyisz.pis.ui.message;

import java.util.UUID;



public class GeneralMsg {
	private MsgHeader header;
	private MsgBody body;
	
	//创建请求消息。 timeout需要大于0，使用默认值传0
	public static GeneralMsg makeCommandRequestMsg(String msgReciver,String msgCommand,String msgBody,boolean isEnd,int timeout) {
		GeneralMsg msg = new GeneralMsg();
		msg.header = new MsgHeader();
		msg.body = new MsgBody();
		msg.header.setMessageType(P2PMessageType.P2P_command_message);
		msg.header.setMessageNo(UUID.randomUUID().toString());
		msg.header.setEnd(isEnd);
		msg.header.setMessageReciver(msgReciver);
		if(timeout>0) {
			msg.header.setTimeout(timeout);
		}
		
		msg.body.setMessageCommand(P2PMessageCommands.getByCommand(msgCommand));
		msg.body.setMessageBody(msgBody);
		return msg;
	}
	
	//创建请求消息。 timeout需要大于0，使用默认值传0
	public static GeneralMsg makeNotificationCommandRequestMsg(String msgReciver,String msgCommand,String msgBody,boolean isEnd,int timeout) {
		GeneralMsg msg = new GeneralMsg();
		msg.header = new MsgHeader();
		msg.body = new MsgBody();
		msg.header.setMessageType(P2PMessageType.P2P_notification_message);
		msg.header.setMessageNo(UUID.randomUUID().toString());
		msg.header.setEnd(isEnd);
		msg.header.setMessageReciver(msgReciver);
		if(timeout>0) {
			msg.header.setTimeout(timeout);
		}
		
		msg.body.setMessageCommand(P2PMessageCommands.getByCommand(msgCommand));
		msg.body.setMessageBody(msgBody);
		return msg;
	}
	
	public GeneralMsg makeRespMsg(String msgBody,boolean isEnd) {
		GeneralMsg resp = new GeneralMsg();
		resp.header = new MsgHeader();
		resp.body = new MsgBody();
		resp.header.setMessageType(P2PMessageType.P2P_response_message);
		resp.header.setMessageNo(this.header.getMessageNo());
		resp.header.setEnd(isEnd);
		resp.header.setMessageReciver(this.header.getMessageSender());
		resp.body.setMessageCommand(this.body.getMessageCommand());
		resp.body.setMessageBody(msgBody);
		return resp;
	}

	public MsgHeader getHeader() {
		return header;
	}

	public void setHeader(MsgHeader header) {
		this.header = header;
	}

	public MsgBody getBody() {
		return body;
	}

	public void setBody(MsgBody body) {
		this.body = body;
	}
}
