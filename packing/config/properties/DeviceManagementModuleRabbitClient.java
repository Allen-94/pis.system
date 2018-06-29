package com.yuyisz.pis.ui.message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.yuyisz.pis.ui.module.subsystem.PcInitializeCheck;
import com.yuyisz.pis.ui.service.subsystem.PcInitializeCheckService;
import com.rabbitmq.client.AMQP.BasicProperties;

@Component
public class DeviceManagementModuleRabbitClient {
	 	private Connection connection;
	    private Channel channel;
	    private String replyQueueName;
	    private ObjectMapper mapper=new ObjectMapper();
	    private Consumer consumer;
	    @Autowired
		private PcInitializeCheckService pcInitializeCheckService;
	    
	    public DeviceManagementModuleRabbitClient() throws IOException, TimeoutException {
	        ConnectionFactory factory = new ConnectionFactory();
	        factory.setHost("192.168.2.32");
			factory.setPort(5672);
			factory.setUsername("rabbitmqadmin");
			factory.setPassword("rabbitmqadmin");
	        connection = factory.newConnection();
	        channel = connection.createChannel();
	        replyQueueName = "devicemanagement";
	        channel.queueDeclare(replyQueueName, false, false, false, null);
	        channel.exchangeDeclare("direct_exchanger", "fanout", true);
	    }
	    
	    public void Recieve() throws IOException {
	    	channel.queueBind(replyQueueName, "direct_exchanger", "");//routingkey
	    	final BlockingQueue<Object> response = new ArrayBlockingQueue<Object>(1);
	    	channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
	    		@Override
	    		public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
	    				byte[] body) throws IOException {
	    			
	    			GeneralMsg msg = mapper.readValue(body, GeneralMsg.class);
	    			System.out.println("--->>接受消息："+msg.toString());
	    			//处理消息
	    			if(P2PMessageCommands.DEVICE_RESTART.getMsgCommand().equals(msg.getBody().getMessageCommand().msgCommand)) {
	    				
	    			}else if(P2PMessageCommands.DEVICE_MODIFY_USERNAME_PASSWORD.getMsgCommand().equals(msg.getBody().getMessageCommand().msgCommand)) {
	    				
	    			}else if(P2PMessageCommands.DEVICE_SET_PLAYCONTROLLER_SWITCH_TIME.getMsgCommand().equals(msg.getBody().getMessageCommand().msgCommand)) {
	    				
	    			}else if(P2PMessageCommands.DEVICE_SET_SCREEN_SWITCH_TIME.getMsgCommand().equals(msg.getBody().getMessageCommand().msgCommand)) {
	    				
	    			}else if(P2PMessageCommands.DEVICE_PC_REINITIALIZE.getMsgCommand().equals(msg.getBody().getMessageCommand().msgCommand)) {
		    			//重新初始化播控器的回执内容
	    				String messageBody = msg.getBody().getMessageBody();//假设取到回执
	    				String[] split = messageBody.split("&");
	    				String pc_id = split[0];//假设从回复中得到的pcid
	    				split = pc_id.split("=");
	    				PcInitializeCheck check = pcInitializeCheckService.findById(split[1]);
	    				//假设判断是否初始化成功
	    				if(messageBody.contains("reinitialize=true")) {
	    					check.setPcCheck(1);//已初始化完成
	    				}else {
	    					check.setPcCheck(2);//初始化失败
	    				}
	    				pcInitializeCheckService.save(check);
		    		}
	    		
	    			
	    		}
	    		
	    	});
	    }
	    
	    public void Send(GeneralMsg message) throws UnsupportedEncodingException, IOException {
	    	
	    	System.out.println("sendMsg==="+message.getBody().getMessageBody());
	        final String corrId = UUID.randomUUID().toString();
	        
	        AMQP.BasicProperties props = new AMQP.BasicProperties
	                .Builder()
	                .correlationId(corrId)
	                .replyTo(replyQueueName)
	                .build();
	        String mess=mapper.writeValueAsString(message);
	        channel.basicPublish("direct_exchanger","", props,  mess.getBytes("utf-8"));
	    }
	    
	    public static void main(String[] args) throws IOException, TimeoutException {
			DeviceManagementModuleRabbitClient deviceManagementModuleRabbitClient = new DeviceManagementModuleRabbitClient();
			deviceManagementModuleRabbitClient.Send(null);
	    }
}
