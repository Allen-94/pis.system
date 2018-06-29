package com.yuyisz.pis.ui.controller.security;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.security.OperationLog;
import com.yuyisz.pis.ui.service.security.OperationLogService;

@Controller
@RequestMapping("/security")
public class OperationLogController {
	@Autowired
	private OperationLogService logService;
	
	@RequestMapping("/findAllOperationLog")
	@ResponseBody
	public List<OperationLog> findALlOperationLog(){
		System.out.println("-->>com.yuyisz.pis.ui.controller.log.OperationLogController.findALlOperationLog()");
		return logService.findALl();
	}
	
	@RequestMapping(value="/logConditionSearch")
	@ResponseBody
	public List<OperationLog> logConditionSearch(@RequestParam String createTime,@RequestParam String operator,
			@RequestParam String result,@RequestParam String clientIP,@RequestParam String command){
		System.out.println("-->>com.yuyisz.pis.ui.controller.log.OperationLogController.logConditionSearch(Map<String, Object>):"+createTime);
		String time = createTime.replace('T', ' ');
		System.out.println("-->>time:"+time);
		return logService.findByOperatorLike(operator,clientIP,createTime,
				command, result);
		
	}
}
