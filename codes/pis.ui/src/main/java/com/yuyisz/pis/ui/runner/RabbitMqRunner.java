package com.yuyisz.pis.ui.runner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.yuyisz.pis.ui.config.WebSocketServer;
import com.yuyisz.pis.ui.message.GeneralMsg;
import com.yuyisz.pis.ui.message.P2PMessageCommands;
import com.yuyisz.pis.ui.module.alarm.Alarm;
import com.yuyisz.pis.ui.service.alarm.AlarmService;

/**
 * 开机启动   rbmq消息监听服务
 * @author mengz
 *
 */
@Component
@Order(value=1)
public class RabbitMqRunner implements ApplicationRunner {
	private Logger logger =  LoggerFactory.getLogger(RabbitMqRunner.class);
	private Consumer consumer;
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private WebSocketServer wsServer ;
	@Autowired
	private AlarmService alarmService;
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		runRabbitClientServer();
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
			channel.queueDeclare("PisUiServer", false, false, false, null);
			channel.basicQos(1);
			System.out.println(" [x] Awaiting  requests");
			consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder()
							.correlationId(properties.getCorrelationId()).build();
					try {
						GeneralMsg msg = mapper.readValue(body, GeneralMsg.class);
						processMsg(msg);
					} catch (RuntimeException e) {
						System.out.println(" [.] " + e.toString());
					} finally {
						channel.basicPublish("", properties.getReplyTo(), replyProps, "".getBytes("UTF-8"));
						channel.basicAck(envelope.getDeliveryTag(), false);
						synchronized (this) {
							this.notify();
						}
					}
				}
			};
			channel.basicConsume("PisUiServer", false, consumer);
			while (true) {
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
	
	@SuppressWarnings("static-access")
	private void processMsg(GeneralMsg msg) {
		logger.info("=======收到告警模块发来的消息=============");
		if(msg.getBody().getMessageCommand().equals(P2PMessageCommands.ADD_REAL_TIME_ALARM)) {
			String alarmId = msg.getBody().getMessageBody();
			//查询告警，将告警内容推送至前端
			Alarm alarm = alarmService.findAlarmById(alarmId);
			try {
				wsServer.sendInfo(mapper.writeValueAsString(alarm));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
