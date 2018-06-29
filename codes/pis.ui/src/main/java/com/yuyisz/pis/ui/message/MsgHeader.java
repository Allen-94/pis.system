package com.yuyisz.pis.ui.message;


public class MsgHeader {
		//消息类型
		private P2PMessageType messageType;
		//是否是最后一个响应
		private boolean end;
		//消息编号 uuid
		private String messageNo;
		//消息接收者，对应一个消息队列名，由服务创建时设置的服务名决定
		private String messageReciver;
		//消息发送者，发送时自动填写发送的消息队列名，对应发送服务的名称
		private String messageSender;
		//超时时间，默认6000毫秒 ，如果到时间没有回复，则消息被删除，不再等待
		private int timeout = 6000;
		
		public MsgHeader() {
			super();
			// TODO Auto-generated constructor stub
		}
		public MsgHeader(P2PMessageType messageType, boolean end, String messageNo, String messageReciver,
				String messageSender, int timeout) {
			super();
			this.messageType = messageType;
			this.end = end;
			this.messageNo = messageNo;
			this.messageReciver = messageReciver;
			this.messageSender = messageSender;
			this.timeout = timeout;
		}
		public P2PMessageType getMessageType() {
			return messageType;
		}
		public void setMessageType(P2PMessageType messageType) {
			this.messageType = messageType;
		}
		public boolean isEnd() {
			return end;
		}
		public void setEnd(boolean end) {
			this.end = end;
		}
		public String getMessageNo() {
			return messageNo;
		}
		public void setMessageNo(String messageNo) {
			this.messageNo = messageNo;
		}
		public String getMessageReciver() {
			return messageReciver;
		}
		public void setMessageReciver(String messageReciver) {
			this.messageReciver = messageReciver;
		}
		public String getMessageSender() {
			return messageSender;
		}
		public void setMessageSender(String messageSender) {
			this.messageSender = messageSender;
		}
		public int getTimeout() {
			return timeout;
		}
		public void setTimeout(int timeout) {
			this.timeout = timeout;
		} 
		
		
}
