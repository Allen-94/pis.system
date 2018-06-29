package com.yuyisz.pis.ui.util;


import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import com.yuyisz.pis.ui.module.security.OperationLog;
import com.yuyisz.pis.ui.module.security.User;

public class LogMessageUtil {
	/**
	 * 返回一个封装好的日志
	 * @param request
	 * @param message
	 * @return
	 * @throws MessagingException 
	 */
	public static OperationLog getOperationLog(HttpServletRequest request,Message message) throws MessagingException {
		System.out.println("-->>OperationLog(注入日志)");
		OperationLog log = new OperationLog();
		User user = (User) request.getSession().getAttribute("cur_user");
		log.setOperator(user.getAccount()+"/"+user.getRealname());
		log.setClientIP(request.getRemoteAddr());
		log.setCreateTime(new Date());
		if(message==null) {
			log.setCommand("登录");
			log.setArguments("登录ID:12345");
			log.setResult("成功");
		}else {
			log.setCommand(message.getContentType());
			log.setArguments(message.getDescription());
			log.setResult(message.getDisposition());
		}
		return log;
		
	}
}
