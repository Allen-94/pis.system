package pis.ui.com.yuyisz.pis.ui.message;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.yuyisz.pis.ui.message.RabbitMqClient;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class rabbitmqClientTest extends TestBase{
	@Test
	public void test() {
		try {
			new RabbitMqClient();
		} catch (IOException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
