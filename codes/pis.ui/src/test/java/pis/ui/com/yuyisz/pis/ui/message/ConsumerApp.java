package pis.ui.com.yuyisz.pis.ui.message;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ConsumerApp  
{  
    public static void main(String[] args)  
    {  
        Connection connection = null;  
        Channel channel = null;  
        try  
        {  
            ConnectionFactory factory = new ConnectionFactory();  
            factory.setHost("192.168.99.100");  
            factory.setPort(32771);  
            factory.setUsername("guest");  
            factory.setPassword("guest");  
            connection = factory.newConnection();  
            channel = connection.createChannel();  
   
            Consumer consumer = new DefaultConsumer(channel) {  
                @Override  
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)  
                        throws IOException {  
                    String message = new String(body, "UTF-8");  
                    System.out.println(" Consumer have received '" + message + "'");  
                }  
            };  
            channel.basicConsume("secondQueue", true, consumer);  
        }  
        catch(Exception ex)  
        {  
            ex.printStackTrace();  
        }  
    }  
}  
