package com.yuyisz.pis.ui.controller.infosources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yuyisz.pis.ui.module.infosources.MediaSource;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.fileupload.FileUploadService;
import com.yuyisz.pis.ui.service.infosources.MediaSourceService;


@Controller
@RequestMapping("/infosources")
public class MediaSourceController {

	@Autowired
	private MediaSourceService mediaSourceService;
	
	@Value("${upload.path.mediaresources}")
	private String mediaResourcesPath;
	
	@Autowired
	private FileUploadService fileUploadService;
	//相对资源根目录的路径
	private String relativePaht;
	
	/*@Value("${upload.path.businessdata.formats}")
	private String formatsPath;
	
	@Value("${upload.path.businessdata.playerdata}")
	private String playerDataPath;*/
	
	
	@RequestMapping("/findall")
	@ResponseBody
	public List<MediaSource> findAllMediaSource(@RequestParam(value="parentId",required=false)String parentFolderId){
		if(parentFolderId != null && parentFolderId.equals("null")) parentFolderId = null;
		if(parentFolderId != null) {
			MediaSource parent = mediaSourceService.findById(parentFolderId);
			if(parent != null) {
				this.relativePaht = parent.getMediaFilePath();
			}else {
				this.relativePaht = this.mediaResourcesPath;
			}
		}else {
			this.relativePaht = this.mediaResourcesPath;
		}
		return mediaSourceService.findAllMediaSourceByParentSourceId(parentFolderId);
	}
	
	/**
	 * 创建文件夹
	 * @param folderName：文件夹名称
	 * @param parantFolder：付文件夹id ，为空表示在根目录
	 * @return
	 */
	@RequestMapping("/createFolder")
	@ResponseBody
	public Map<String,Object> createFolder(@RequestParam("folderName")String folderName,
			@RequestParam(value="parentId",required=false,defaultValue="")String parentId,HttpServletRequest request){
		Map<String ,Object> map = new HashMap<>();
		MediaSource folder = new MediaSource();
		MediaSource parentFolder = mediaSourceService.findById(parentId);
		folder.setId(UUID.randomUUID().toString());
		//表示文件夹的路径，以逗号分割
		if(parentFolder == null) {
			parentFolder = makeRootFolder();
		}
		User curUser = (User) SecurityUtils.getSubject().getPrincipal();
		folder.setDescribe(parentFolder.getDescribe()+","+folderName+":"+folder.getId());
		folder.setMediaFilePath(parentFolder.getMediaFilePath()+folderName+File.separator);
		folder.setCreateDate(new Date());
		folder.setCreater(curUser);
		folder.setManager(curUser);
		folder.setMediaName(folderName);
		folder.setParentSource(parentFolder);
		folder.setMediaType(0);
		MediaSource result = mediaSourceService.saveMediaSource(folder);
		if(result!=null) {
			File file = new File(folder.getMediaFilePath());
			if(!file.exists()) {
				file.mkdirs();
			}
			map.put("result", true);
			map.put("message", "文件夹创建成功");
		}else {
			map.put("result", false);
			map.put("message", "文件夹创建失败");
		}
		return map;
	}
	
	@RequestMapping(value="uploadfile",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> uploadfile(@RequestParam("filename") String filename,
			@RequestParam("parentId")String parentId,
			@RequestParam("size")int size,
			HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		boolean flag = true;
		String message = "上传成功";
		MediaSource file = new MediaSource();
		MediaSource parentFolder = mediaSourceService.findById(parentId);
		file.setId(UUID.randomUUID().toString());
		//表示文件夹的路径，以逗号分割
		if(parentFolder != null) {
			file.setDescribe(parentFolder.getDescribe()+","+filename+":"+file.getId());
			file.setMediaFilePath(parentFolder.getMediaFilePath()+filename);
		}else {
			parentFolder = makeRootFolder();
			file.setDescribe(filename+":"+file.getId());
			file.setMediaFilePath(this.mediaResourcesPath);
		}
		User curUser = (User) SecurityUtils.getSubject().getPrincipal();
		file.setCreateDate(new Date());
		file.setCreater(curUser);
		file.setManager(curUser);
		file.setMediaName(filename);
		file.setParentSource(parentFolder);
		file.setSize(size);
		file.setMediaType(1);
		MediaSource fileResu = mediaSourceService.saveMediaSource(file);
        result.put("result", flag);
        result.put("message", message);
		return result;
	}
	
	private MediaSource makeRootFolder() {
		MediaSource root = new MediaSource();
		root.setId("MediaRoot");
		root.setDescribe("MediaRoot:MediaRoot");
		root.setMediaFilePath(this.mediaResourcesPath);
		return root;
	}
	
	@RequestMapping(value="upload",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> upload(@RequestParam("file") MultipartFile file){
		Map<String,Object> result = new HashMap<>();
		boolean flag = true;
		String message = "上传成功";
        if (!file.isEmpty()) {
        	try {
				return fileUploadService.uploadFile(this.relativePaht, file);
			} catch (IOException e) {
				flag = false;
				message = "上传文件发生异常!\n"+e.getMessage();
			}
        } else {
        	flag=false;
            message = "上传失败，因为文件是空的.";
        }
        result.put("result", flag);
        result.put("message", message);
		return result;
	}
}
