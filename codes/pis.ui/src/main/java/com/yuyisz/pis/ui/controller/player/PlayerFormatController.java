package com.yuyisz.pis.ui.controller.player;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yuyisz.pis.ui.module.player.PlayerFormat;
import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.fileupload.FileUploadService;
import com.yuyisz.pis.ui.service.player.PlayerFormatService;

@Controller
@RequestMapping("/playercontrol")
public class PlayerFormatController {

	@Autowired
	private PlayerFormatService playerFormatService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	@Value("${upload.path.businessdata.formats}")
	private String formatsPath;
	
	
	@RequestMapping("/findAllFormat")
	@ResponseBody
	public List<PlayerFormat> findAllFormat(){
		return playerFormatService.findAll();
	}
	
	@RequestMapping("/upload")
	@ResponseBody
	public Map<String, Object> fileUpload(@RequestParam("file") MultipartFile file){
		Map<String,Object> result = new HashMap<>();
		boolean flag = true;
		String message = "上传成功";
		String fileId = "";
		try {
			result = fileUploadService.uploadFile(this.formatsPath, file);
			if((boolean) result.get("flag")) {
				FileObject fb = (FileObject)result.get("fileobject");
				fileUploadService.saveFileObject(fb);
				fileId = fb.getId();
			}else {
				flag = false;
				message = "上传文件错误！";
			}
		} catch (IOException e) {
			flag = false;
			message = e.getMessage();
		}
		 result.put("flag", flag);
	     result.put("message", message);
	     result.put("fileId", fileId);
		return result;
	}
	
	@RequestMapping("/delPlayerFormat/{id}")
	@ResponseBody
	public Map<String,Object> delPlayerFormat(@PathVariable("id")long formatId){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			PlayerFormat fmt = playerFormatService.findById(formatId);
			flag = playerFormatService.delPlayerFormat(formatId);
			if(flag) {
				fileUploadService.delFile(fmt.getFileObject().getId());
				message = "版式删除成功！";
			}else {
				message = "版式删除失败！";
			}
		}catch(Exception e) {
			message = "保存版式异常\n"+e.getMessage();
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
	@RequestMapping("/addPlayerFormat")
	@ResponseBody
	public Map<String,Object> addPlayerFormat(@RequestParam(value="playerFormatId",required=false,defaultValue="0")long playerFormatId,
			@RequestParam("playerFormatName")String playerFormatName,
			@RequestParam("description")String description,
			@RequestParam("fileId")String fileId){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		//保存版式
		PlayerFormat format = null;
		if(playerFormatId == 0) {
			format = new PlayerFormat();
		}else {
			format = playerFormatService.findById(playerFormatId);
		}
		format.setCreateDate(new Date());
		format.setCreater((User) SecurityUtils.getSubject().getPrincipal());
		format.setDescription(description);
		FileObject fb = fileUploadService.findById(fileId);
		format.setFileObject(fb);
		format.setPlayFormatName(playerFormatName);
		try {
			format = playerFormatService.saveFormat(format);
			if(format!=null) {
				flag = true;
				message = "保存版式成功";
			}else {
				message = "保存版式失败";
			}
		}catch(Exception e) {
			message = "保存版式异常\n"+e.getMessage();
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
}
