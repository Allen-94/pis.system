package com.yuyisz.pis.ui.controller.alarm;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yuyisz.pis.ui.module.alarm.AlarmForwardConfig;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.alarm.AlarmForwardConfigService;
import com.yuyisz.pis.ui.vo.AlarmForwardConfigVO;  
  
@RestController  
@RequestMapping("/mail")  
public class MailController {  
    
	@Autowired
	private AlarmForwardConfigService alarmForwardConfigService;
      
    @RequestMapping("/sendemail")
    @ResponseBody
    public Map<String,String> sendEmail(@RequestBody AlarmForwardConfigVO param)  
    {  
    	Map<User,String> re = alarmForwardConfigService.forwardAlarm2(param, "告警转发邮件测试");
    	Map<String,String> result = new HashMap<>();
    	for( Entry<User, String> entry:re.entrySet()) {
    		result.put(entry.getKey().getRealname()+"/"+entry.getKey().getAccount(), entry.getValue());
    	}
    	
    	return result;
        /*try  
        {  
			#spring.mail.host=smtp.sina.cn
			#spring.mail.username=17302602080@sina.cn
			#spring.mail.password=zhaomeng198784
			#spring.mail.port=25
			#spring.mail.protocol=smtp
        	JavaMailSenderImpl sender = new JavaMailSenderImpl();
        	sender.setHost("smtp.sina.cn");
        	sender.setPort(25);
    		sender.setUsername("17302602080@sina.cn");
    		sender.setPassword("zhaomeng198784");
    		sender.setProtocol("smtp");
    		sender.setDefaultEncoding("UTF-8");
            final MimeMessage mimeMessage = sender.createMimeMessage();  
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);  
            message.setFrom("17302602080@sina.cn");  
            message.setTo("meng.zh@yuyisz.com");  
            message.setSubject("新的测试邮件主题");  
            message.setText("从哪里发来的垃圾，测试邮件内容");  
            sender.send(mimeMessage);  
            return true;  
        }  
        catch(Exception ex)  
        {  
        	ex.printStackTrace();
            return false;  
        }*/  
    }  
}  