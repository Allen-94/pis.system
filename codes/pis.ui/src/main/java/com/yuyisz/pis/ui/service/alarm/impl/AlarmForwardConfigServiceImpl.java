package com.yuyisz.pis.ui.service.alarm.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuyisz.pis.ui.module.alarm.Alarm;
import com.yuyisz.pis.ui.module.alarm.AlarmForwardConfig;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.alarm.AlarmForwardConfigService;
import com.yuyisz.pis.ui.service.security.UserService;
import com.yuyisz.pis.ui.vo.AlarmForwardConfigContent;
import com.yuyisz.pis.ui.vo.AlarmForwardConfigVO;
import com.yuyisz.pis.ui.vo.AlarmForwardParam;
@Service
public class AlarmForwardConfigServiceImpl implements AlarmForwardConfigService {
	private Logger logger =  LoggerFactory.getLogger(AlarmForwardConfigServiceImpl.class);
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private UserService userService;
	@Override
	public void forwardAlarm(AlarmForwardConfig config, Alarm alarm) {
		String content = config.getConfigContent();
		AlarmForwardConfigContent con;
		try {
			con = mapper.readValue(content, AlarmForwardConfigContent.class);
			Set<User> acceptors = config.getAcceptor();
			for(User acceptor:acceptors) {
				AlarmForwardParam param = new AlarmForwardParam();
				param.setHost(con.getSmtp());
				param.setPort(con.getSafeport());
				param.setUserName(con.getEmailusername());
				param.setPassword(con.getEmailpassword());
				param.setFrom(con.getEmail());
				param.setTo(acceptor.getEmail());
				sendAlarm(param, alarm);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	@Override
	public Map<User, String> forwardAlarm2(AlarmForwardConfigVO vo, String testMes) {
		String content = vo.getConfigContent();
		AlarmForwardConfigContent con;
		Map<User,String> result = new HashMap<>();
		try {
			con = mapper.readValue(content, AlarmForwardConfigContent.class);
			List<User> acceptors = new ArrayList<>();
			String acceptorIds = vo.getAcceptors();
			String[] acceptorIdStr = acceptorIds.split(",");
			List<Integer> ids = new ArrayList<>();
			for(String id:acceptorIdStr) {
				ids.add(Integer.valueOf(id));
			}
			acceptors = userService.findUserByIds(ids);
			List<Future<String>> futures = new ArrayList<>();
			Map<User,Future<String>> temp = new HashMap<>();
			for(User acceptor:acceptors) {
				AlarmForwardParam param = new AlarmForwardParam();
				param.setHost(con.getSmtp());
				param.setPort(con.getSafeport());
				param.setUserName(con.getEmailusername());
				param.setPassword(con.getEmailpassword());
				param.setFrom(con.getEmail());
				param.setTo(acceptor.getEmail());
				Future<String> future = sendAlarm2(param, testMes);
				futures.add(future);
				temp.put(acceptor, future);
			}
			
			while(true) {
				try {
					boolean flag = true;
					//检查是否还有未完成的任务
					for(Future<String> future:futures) {
						if(!future.isDone()) {
							flag = false;
							break;
						}
					}
					if(flag) {
						break;
					}
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			for(Entry<User, Future<String>> entry:temp.entrySet()) {
				try {
					result.put(entry.getKey(), entry.getValue().get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return result;
	}

	@Async
	private void sendAlarm(AlarmForwardParam param, Alarm alarm) {
		 
        try {
        	JavaMailSenderImpl sender = new JavaMailSenderImpl();
        	sender.setHost(param.getHost());
        	sender.setPort(param.getPort());
    		sender.setUsername(param.getUserName());
    		sender.setPassword(param.getPassword());
    		sender.setDefaultEncoding("UTF-8");
            final MimeMessage mimeMessage = sender.createMimeMessage();  
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage); 
			message.setFrom(param.getFrom());
			message.setTo(param.getTo());  
	        message.setSubject("告警转发[from:"+alarm.getSource().getResourceName()+"]");  
	        message.setText(alarm.getContent());  
	        sender.send(mimeMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
       
	}
	
	@Async
	private Future<String> sendAlarm2(AlarmForwardParam param, String testMsg) {
		logger.info("测试邮件发送：From:"+param.getFrom()+",To:"+param.getTo());
        try {
        	JavaMailSenderImpl sender = new JavaMailSenderImpl();
        	sender.setHost(param.getHost());
        	sender.setPort(param.getPort());
    		sender.setUsername(param.getUserName());
    		sender.setPassword(param.getPassword());
    		sender.setDefaultEncoding("UTF-8");
            final MimeMessage mimeMessage = sender.createMimeMessage();  
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage); 
			message.setFrom(param.getFrom());
			message.setTo(param.getTo());  
	        message.setSubject("邮件发送测试");  
	        message.setText(testMsg);  
	        sender.send(mimeMessage);
	        return new AsyncResult<String>("向地址【"+param.getTo()+"】发送成功！！！");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new AsyncResult<String>("向地址【"+param.getTo()+"】发送失败！！！");
		}  
       
	}

}
