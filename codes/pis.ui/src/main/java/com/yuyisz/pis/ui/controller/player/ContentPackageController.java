package com.yuyisz.pis.ui.controller.player;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.player.ContentObject;
import com.yuyisz.pis.ui.module.player.ContentPackage;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.infosources.SystemVariableService;
import com.yuyisz.pis.ui.service.player.ContentPackageService;

@Controller
@RequestMapping("/playercontrol")
public class ContentPackageController {

	@Autowired
	private ContentPackageService contentPackageService;
	
	@Autowired
	private SystemVariableService systemVariableService;
	
	@GetMapping("addPackageContent/{contentType}")
	public String addPackageContent(@PathVariable("contentType")int contentType,Model model) {
		model.addAttribute("contentType", contentType);
		return "/player/addPackageContent";
	}
	
	@RequestMapping("findAllContentPackages")
	@ResponseBody
	public List<ContentPackage> findAll(){
		return contentPackageService.findAll();
	}
	
	@GetMapping("findPackageContentByPackageId")
	@ResponseBody
	public List<ContentObject> findPackageContentByPackageId(@RequestParam("packageId")long packageId ){
		return contentPackageService.findPackageContentByPackageId(packageId);
	}
	
	@RequestMapping("/delContentPackage/{id}")
	@ResponseBody
	public Map<String,Object> delContentPackage(@PathVariable("id")long id){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			flag = contentPackageService.delContentPackage(id);
			if(flag) {
				message = "删除内容包成功";
			}else {
				message = "删除内容包失败";
			}
		}catch(Exception e) {
			message = "删除内容包失败";
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
	/**
	 * 新增内容包
	 * @param contentId
	 * @param contentPackageName
	 * @param description
	 * @return
	 */
	@RequestMapping("/addContentPackage")
	@ResponseBody
	public Map<String,Object> addContentPackage(
			@RequestParam(value="id",required=false,defaultValue="0")long contentId,
			@RequestParam("contentPackageName")String contentPackageName,
			@RequestParam("description")String description/*,
			@RequestParam("contents")List<ContentObject> contents*/){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			ContentPackage cp = null;
			if(contentId == 0) {
				cp = new ContentPackage();
			}else {
				cp = contentPackageService.findById(contentId);
			}
			if(cp == null) {
				message = "要更新的内容包不存在！";
			}else {
				cp.setContentName(contentPackageName);
				cp.setDescription(description);
				cp.setCreateDate(new Date());
				Subject subject = SecurityUtils.getSubject();
				cp.setCreater((User) subject.getPrincipal());
				/*cp.setContents(contents);*/
				contentPackageService.saveContentPackage(cp);
				flag = true;
				message = "新增内容包成功！";
			}
			
			}catch(Exception e) {
				message="新增内容包异常！\n"+e.getMessage();
			}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
	/**
	 * 新增内容包中的内容
	 */
	@RequestMapping("/addPackageContent")
	@ResponseBody
	public Map<String,Object> addPackageContent(@RequestParam(value="id",required=false,defaultValue="")String id,
			@RequestParam(value="contentId")long contentId,
			@RequestParam("contentName")String contentName,
			@RequestParam("mainId")int mainId,
			@RequestParam("backupId")int backupId){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		
		try {
			ContentObject co = null;
			if(id.trim().equals("")) {
				//新建内容
				co = new ContentObject();
				co.setId(UUID.randomUUID().toString());
				ContentPackage cp = contentPackageService.findById(contentId);
				co.setCp(cp);
			}else {
				co = contentPackageService.findOneContentObjectById(id);
			}
			co.setContentName(contentName);
			co.setMainContent(systemVariableService.findOne(mainId));
			co.setBackupContent(systemVariableService.findOne(backupId));
			ContentPackage cp = contentPackageService.findById(contentId);
			cp.getContents().add(co);
			contentPackageService.saveContentPackage(cp);
			//contentPackageService.SavePackageContent(co);
			flag = true;
			message = "保存内容成功！";
		}catch(Exception e) {
			message = "保存内容失败！\n"+e.getMessage();
		}
		
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
}
