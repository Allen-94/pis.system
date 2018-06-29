package com.yuyisz.pis.ui.message;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


public class RabbitMqClient {

    private Connection connection;
    private Channel channel;
    private String replyQueueName;
    private ObjectMapper mapper=new ObjectMapper();
    
    public RabbitMqClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.99.100");
		factory.setPort(32774);
		factory.setUsername("guest");
		factory.setPassword("guest");
        connection = factory.newConnection();
        channel = connection.createChannel();
        replyQueueName = channel.queueDeclare().getQueue();
    }

   public GeneralMsg send(GeneralMsg message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();
        message.getHeader().setMessageSender(replyQueueName);
        String mess=mapper.writeValueAsString(message);
        channel.basicPublish("", message.getHeader().getMessageReciver(), props,  mess.getBytes("utf-8"));
        final BlockingQueue<GeneralMsg> response = new ArrayBlockingQueue<GeneralMsg>(1);
        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (body.length > 0) {
                	//将Java对象匹配JSON结构
                	response.offer(mapper.readValue(body, GeneralMsg.class));
                }
            }
        });
        return response.take();
    }

    public void close() throws IOException {
        connection.close();
    }
}