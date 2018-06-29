package com.yuyisz.pis.ui.websocket;
import java.io.IOException;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/reverse")
public class ReverseWebSocketEndpoint {

	@OnMessage
	public void handleMessage(Session session, String message) throws IOException {
		session.getBasicRemote()
		.sendText("开始干活。。。");
		int flag = 0;
		while(true) {
			if(flag==100)break;
			flag++;
			try {
				Thread.sleep(100);
				session.getBasicRemote()
				.sendText(flag+"%");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		session.getBasicRemote()
		.sendText("完工。。。");
	}
}
