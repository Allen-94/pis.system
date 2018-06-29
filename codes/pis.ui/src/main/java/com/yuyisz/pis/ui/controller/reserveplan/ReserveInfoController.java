package com.yuyisz.pis.ui.controller.reserveplan;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.reserveplan.ReserveInfo;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.reserveplan.ReserveInfoService;

@Controller
@RequestMapping("/reserveplan")
public class ReserveInfoController {

	@Autowired
	private ReserveInfoService reserveInfoService;
	
	@GetMapping("toAddReserveInfo")
	public String toAddReserveInfo(@RequestParam(value="infoId",required=false,defaultValue="-1")int infoId,Model model) {
		ReserveInfo info = null;
		if(infoId != -1) {
			info = reserveInfoService.findById(infoId);
		}else {
			info = new ReserveInfo();
			info.setInfoType(1);
		}
		model.addAttribute("info", info);
		return "/reserveplan/addReserveInfo";
	}
	
	@GetMapping("findAllReserveInfo")
	@ResponseBody
	public List<ReserveInfo> findAllReserveInfo(){
		return reserveInfoService.findAllReserveInfo();
	}
	
	/**
	 * 新增/修改预定义信息
	 */
	@PostMapping("addReserveInfo")
	@ResponseBody
	public Map<String, Object> addReserveInfo(
			@RequestParam(value="id",required=false,defaultValue="0")int infoId,
			@RequestParam("infoName")String infoName,
			@RequestParam("infoContent")String infoContent,
			@RequestParam("infoType")int infoType){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		ReserveInfo info = null;
		try {
			if(infoId == 0) {
				//新建
				info = new ReserveInfo();
				info.setInfoName(infoName);
				info.setInfoContent(infoContent);
				info.setInfoType(infoType);
				info.setCreateDate(new Date());
				info.setCreater((User) SecurityUtils.getSubject().getPrincipal());
				
			}else {
				//修改
				info = reserveInfoService.findById(infoId);
				info.setInfoName(infoName);
				info.setInfoContent(infoContent);
				info.setInfoType(infoType);
			}
			
			reserveInfoService.saveInfo(info);
			message = "保存预定义信息成功";
			flag = true;
		}catch(Exception e) {
			message = "保存预定义信息失败！\n"+e.getMessage();
		}
		
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
	/**
	 * 删除预定义信息
	 */
	@PostMapping("delReserveInfo/{id}")
	@ResponseBody
	public Map<String, Object> delReserveInfo(@PathVariable("id")int id){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			reserveInfoService.deleteById(id);
			flag = true;
			message = "删除成功！";
		}catch(Exception e) {
			message = "删除失败！\n"+e.getMessage();
		}
		
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
}
