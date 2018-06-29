package com.yuyisz.pis.ui.controller.infosources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.infosources.SystemVariable;
import com.yuyisz.pis.ui.service.infosources.SystemVariableService;

@Controller
@RequestMapping("/infosources")
public class SystemVariableController {

	@Autowired
	private SystemVariableService systemVariableService;
	
	@GetMapping("findAllSystemVariable")
	@ResponseBody
	public List<SystemVariable> findAllSystemVariable(){
		return systemVariableService.findAll();
	}
	
	
	@PostMapping("delSysVariable/{id}")
	@ResponseBody
	public Map<String,Object> delSysVariable(@PathVariable("id")int id){
		Map<String,Object> result = new HashMap<>();
		boolean flag = true;
		String message="";
		try {
			systemVariableService.delSystemVariable(id);
			message = "删除环境变量成功";
		}catch(Exception e) {
			flag = false;
			message = "删除环境变量异常，\n"+e.getMessage();
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
	
	@PostMapping("addSysVariable")
	@ResponseBody
	public Map<String,Object> addSysVariable(@RequestBody SystemVariable sv){
		
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			
			if(sv.getVariableName() == null || sv.getVariableName().trim().equals("")) {
				message="环境变量名不可为空！！！";
			}else {
				SystemVariable res = systemVariableService.saveSystemVariable(sv);
				if(res != null) {
					if(sv.getId()!=0) {
						flag = true;
						message = "修改环境变量成功！";
					}else {
						flag = true;
						message = "新增环境变量成功！";
					}
				}else {
					message = "新增环境变量失败！";
				}
				
			}
			
		}catch(Exception e) {
			message = "新增/修改环境变量异常\n,"+e.getMessage();
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
}
