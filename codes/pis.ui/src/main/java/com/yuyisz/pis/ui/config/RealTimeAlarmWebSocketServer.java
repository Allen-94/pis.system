package com.yuyisz.pis.ui.config;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeoutException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.yuyisz.pis.ui.module.alarm.Alarm;
import com.yuyisz.pis.ui.service.alarm.AlarmService;
import com.yuyisz.pis.ui.service.alarm.impl.AlarmServiceImpl;
import com.yuyisz.pis.ui.util.SpringUtil;
import com.yuyisz.pis.ui.vo.RealTimeAlarmCondition;

@ServerEndpoint(value = "/realtime_alarm")
@Component
public class RealTimeAlarmWebSocketServer {
	private static Logger logger = LoggerFactory.getLogger(RealTimeAlarmWebSocketServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    
    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    public static CopyOnWriteArraySet<RealTimeAlarmWebSocketServer> webSocketSet = new CopyOnWriteArraySet<RealTimeAlarmWebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    
    private RealTimeAlarmCondition condition;
    
    private ObjectMapper mapper=new ObjectMapper();
    
    private AlarmService alarmService;
   
    private Runnable alarmListener;
    private boolean flag = true;
    private Consumer consumer;
    /**
     * 连接建立成功调用的方法
     * 建立链接是，启动一个线程，异步向前端发送返回消息。
     * */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        logger.info("有新连接加入！当前在线人数为" + getOnlineCount());
        alarmService = SpringUtil.getBean(AlarmServiceImpl.class);
        logger.info("已经启动消息监听......");
    }
 
	private void runRabbitClientServer() {
		logger.debug("===============初始化rabbitmq连接====================");
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.99.100");
		factory.setPort(32771);
		factory.setUsername("guest");
		factory.setPassword("guest");
		Connection connection = null;
		try {
			connection = factory.newConnection();
			final Channel channel = connection.createChannel();
			String EXCHANGE_NAME="alarm_real_time";
			String ROUTING_KEY = "";
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			DeclareOk queueOk = channel.queueDeclare();
			String queueName = queueOk.getQueue();
			channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);
			System.out.println(" [x] Awaiting  requests");
			consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					String alarmId = new String(body, "UTF-8");
					logger.info("新的告警到达》》》》"+alarmId);
					Alarm alarm = alarmService.findAlarmById(alarmId);
					sendMessage(mapper.writeValueAsString(alarm));
				}
			};
			channel.basicConsume(queueName, true, consumer);
			while (flag) {
				synchronized (consumer) {
					try {
						consumer.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (IOException _ignore) {
				}
		}
	}
    
    /**
     * 连接关闭调用的方法
     * 关闭连接时，停止消息发送线程，
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        flag = false;
        logger.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * 接收过滤条件参数，json格式字符串，
     * 改变参数，使用新的条件查询，并返回结果到客户端
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
    	logger.info("来自客户端的消息:" + message);
    	try {
			condition = mapper.readValue(message, RealTimeAlarmCondition.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 发生错误时调用
     * 关闭线程，清理资源
     * */
    @OnError
    public void onError(Session session, Throwable error) {
    	logger.info("发生错误");
        error.printStackTrace();
    }


    /**
     * 向前端发送消息的方法
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        if(this.session != null) {
        	this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 群发自定义消息
     * 向所有客户端发送消息
     * */
    public static void sendInfo(String message) throws IOException {
        for (RealTimeAlarmWebSocketServer item : webSocketSet) {
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
        RealTimeAlarmWebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        RealTimeAlarmWebSocketServer.onlineCount--;
    }
}