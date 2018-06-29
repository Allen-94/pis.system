package com.yuyisz.pis.ui.controller.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.security.OperationsMode;
import com.yuyisz.pis.ui.service.security.OperationsModeService;

@Controller
@RequestMapping("/security")
public class OperationsModeController {
	@Autowired
	private OperationsModeService operationsModeService;
	
	@RequestMapping(value="/findOperationsModeList")
	@ResponseBody
	public List<OperationsMode> findOperationsModeList(){
		
		return operationsModeService.findAllOperationsMode();
	}
	
	@RequestMapping(value="/OperationsModeEdited")
	public String OperationsModeEdited(ModelMap modelMap,@RequestParam int id) {
		System.out.println("-->>com.yuyisz.pis.ui.controller.security.OperationsModeController.OperationsModeEdited(ModelMap, int)");
		OperationsMode mode = operationsModeService.findById(id);
		modelMap.addAttribute("OperationsMode", mode);
		return "security/OperationsModeEdited";
		
	}
	
	@RequestMapping(value="/saveOperationMode")
	@ResponseBody
	public String saveOperationMode(@RequestBody OperationsMode mode) {
		operationsModeService.save(mode);
		return "修改成功";
		
	}
}
