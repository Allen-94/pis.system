package com.yuyisz.pis.ui.config;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuyisz.pis.ui.module.reserveplan.ReserveCmd;
import com.yuyisz.pis.ui.module.reserveplan.ReservePlan;
import com.yuyisz.pis.ui.module.reserveplan.ReserverPlanTask;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.service.reserveplan.ReserveCmdService;
import com.yuyisz.pis.ui.service.reserveplan.ReservePlanService;
import com.yuyisz.pis.ui.service.reserveplan.ReserverPlanTaskService;
import com.yuyisz.pis.ui.util.SpringUtil;
/**
 * 启动预案的websocket
 * @author mengz
 *
 */
@ServerEndpoint(value = "/reserve_plan_start")
@Component
public class ReservePlanStartWebSocketServer {
	private static Logger logger = LoggerFactory.getLogger(ReservePlanStartWebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    public static CopyOnWriteArraySet<ReservePlanStartWebSocketServer> webSocketSet = new CopyOnWriteArraySet<ReservePlanStartWebSocketServer>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //是否停止预案执行的标记
    private boolean flag;	
    private ReservePlanService reservePlanService;
    private ReserverPlanTaskService reserverPlanTaskService;
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
    	logger.info("启动预案...");
        this.session = session;
        webSocketSet.add(this);     //加入set中
        reservePlanService = SpringUtil.getBean(ReservePlanService.class);
        reserverPlanTaskService = SpringUtil.getBean(ReserverPlanTaskService.class);
        addOnlineCount();           //在线数加1
        logger.info("有新连接加入！当前在线人数为" + getOnlineCount());
        try {
            sendMessage("Start-ReservePlan");
        } catch (IOException e) {
        	logger.info("IO异常");
        }
    }
 
	
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        logger.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    ObjectMapper mapper=new ObjectMapper();
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
    	logger.info("来自客户端的消息:" + message);
    	try {
    		if(message.equals("Stop-ReservePlan")) {
        		this.flag = false;
        	}else{
        		//启动预案，判断是否已经启动，如果启动，就直接推出
        		if(this.flag) {
        			//预案已启动
        			sendMessage("Start-ReservePlan_Running");
        		}else {
        			int planId = Integer.valueOf(message);
        			ReservePlan plan = reservePlanService.findById(planId);
        			Set<DevResources> players = plan.getPlayers();
        			for(DevResources dev:players) {
        				if(this.flag) {
        					sendMessage("Stop-ReservePlan");
    						break;
    					}
        				//向每一个设备发送执行指令，
        				List<ReserverPlanTask> tasks = dev.getSortedReserverTasks();
        				processDevReserveTask(tasks).run();;
        				
        			}
        			sendMessage("Stop-ReservePlan");
					reservePlanService.Start(planId,true);//设置预案启动
	    			sendMessage("Run-ReservePlan-Success");
        		}
        	}
    	} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private Runnable processDevReserveTask(List<ReserverPlanTask> tasks) {
    	Runnable thread = new Runnable() {
			@Override
			public void run() {
				for(ReserverPlanTask task:tasks) {
					try {
						if(flag) {
	    					sendMessage("Stop-ReservePlan");
							break;
						}
						//执行指令。。。
						logger.info("执行指令》》》"+task.getPlayer().getResourceName()
						+":指令名称："+task.getActionName()
						+":指令内容："+task.getActionContent()
						+":播放内容："+task.getContent());
						task.setState(1);
						reserverPlanTaskService.savePlanTask(task);
						sendMessage(mapper.writeValueAsString(task));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		    	}
			}
		};
		return thread;
    }
    
    /**
     * 发生错误时调用
     * */
    @OnError
    public void onError(Session session, Throwable error) {
    	logger.info("发生错误");
        error.printStackTrace();
    }


    
    public void sendMessage(String message) throws IOException {
        if(this.session != null) {
        	this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 群发自定义消息
     * */
    public static void sendInfo(String message) throws IOException {
        for (ReservePlanStartWebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        ReservePlanStartWebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ReservePlanStartWebSocketServer.onlineCount--;
    }
}