package pis.ui.com.yuyisz.pis.ui.message;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ProducerApp  
{  
    public static void main(String[] args) throws IOException, TimeoutException {  
        Connection connection = null;  
        Channel channel = null;  
        try  
        {  
            ConnectionFactory factory = new ConnectionFactory();  
            factory.setHost("192.168.99.100");  
            factory.setPort(32771);  
            factory.setUsername("guest");  
            factory.setPassword("guest");  
            //创建与RabbitMQ服务器的TCP连接  
            connection  = factory.newConnection();  
            channel = connection.createChannel();  
            channel.queueDeclare("secondQueue", true, false, false, null);  
            String message = "second Message";             
            channel.basicPublish("", "secondQueue", null, message.getBytes());  
            System.out.println("Send Message is:'" + message + "'");              
        }  
        catch(Exception ex)  
        {  
            ex.printStackTrace();  
        }  
        finally  
        {  
            if(channel != null)  
            {  
                channel.close();  
            }  
            if(connection != null)  
            {  
                connection.close();  
            }  
        }  
    }  
}
