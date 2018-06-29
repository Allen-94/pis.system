package com.yuyisz.pis.ui.service.fileupload.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yuyisz.pis.ui.dao.fileupload.FileObjectRepository;
import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.service.fileupload.FileUploadService;
@Service
public class FileUploadServiceImpl implements FileUploadService {

	@Autowired
	private FileObjectRepository fileObjectRepository;
	
	/**
	 * 保存文件信息
	 */
	@Override
	public FileObject saveFileObject(FileObject fb) {
		return fileObjectRepository.saveAndFlush(fb);
	}

	/**
	 * 保存文件到目录
	 */
	@Override
	public Map<String,Object> uploadFile(String uploadPath , MultipartFile file) throws IOException {
		getUploadFilePath(uploadPath);
		Map<String,Object> result = new HashMap<>();
		boolean flag = true;
		String message = "上传成功";
		FileObject fb = new FileObject(uploadPath, file.getOriginalFilename());
        if (!file.isEmpty()) {
            try {
	                BufferedOutputStream out = new BufferedOutputStream(
	                        new FileOutputStream(new File(fb.getPath(),fb.getRealName())));
	                out.write(file.getBytes());
	                out.flush();
	                out.close();
	            } catch (FileNotFoundException e) {
	                flag=false;
	                message =  "上传失败," + e.getMessage();
	            } catch (IOException e) {
	                flag=false;
	                message =  "上传失败," + e.getMessage();
	            }
        } else {
        	flag=false;
            message = "上传失败，因为文件是空的.";
        }
        result.put("flag", flag);
        result.put("message", message);
        result.put("fileobject", fb);
		return result;
	}

	/**
	 * 根据文件id删除文件
	 */
	@Override
	public Map<String,Object> delFile(String fileId) throws Exception {
		Map<String,Object> result = new HashMap<>();
		boolean flag = true;
		String message = "删除文件成功";
		try {
			FileObject fb = fileObjectRepository.findOne(fileId);
			if(fb == null) {
				flag = false;
				message = "文件不存在";
			}else {
				delRealFile(fb.getPath(),fb.getRealName());
				fileObjectRepository.delete(fileId);
			}
		}catch(Exception e) {
			flag = false;
			message = "删除文件异常！\n"+e.getMessage();
		}
		
		result.put("result", flag);
		result.put("message", message);
		return result;
	}

	private void delRealFile(String realPath,String fileName) {
		// TODO 真正的删除文件系统的文件，暂时不处理，远程删除，比较麻烦
		
	}

	@Override
	public String getUploadFilePath(String path) {
		//String filePath = this.uploadFilePath+File.separator+path;
		//校验版式文件夹是否存在，不存在就创建
		File foramtFolder = new File(path);
		if(!foramtFolder.exists()||!foramtFolder.isDirectory()) {
			foramtFolder.mkdirs();
		}
		return path+File.separator;
	}

	@Override
	public FileObject findById(String fileId) {
		return fileObjectRepository.findOne(fileId);
	}


}
