package com.yuyisz.pis.ui.controller.fileupload;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.service.fileupload.FileUploadService;

@Controller
@RequestMapping("/fileupload")
public class FileUploadController {
	@Autowired
	private FileUploadService fileUploadService;
	
	@RequestMapping("/upload")
	@ResponseBody
	public Map<String, Object> fileUpload(@RequestParam("uploadpath")String uploadpath ,@RequestParam("file") MultipartFile file){
		Map<String,Object> result = new HashMap<>();
		boolean flag = true;
		String message = "上传成功";
		String fileId = "";
		try {
			result = fileUploadService.uploadFile(uploadpath, file);
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
	
	@RequestMapping("/delfile/{fileId}")
	@ResponseBody
	public Map<String, Object> delFile(@PathVariable("fileId")String fileId){
		Map<String,Object> result = new HashMap<>();
		boolean flag = true;
		String message = "删除文件成功";
		try {
			return  fileUploadService.delFile(fileId);
		} catch (Exception e) {
			flag = false;
			message = "删除文件异常！\n"+e.getMessage();
		}
		result.put("flag", flag);
	    result.put("message", message);
		return result;
	}
	
	@RequestMapping("/preview/{fileId}")
	public ResponseEntity<byte[]> preview(@PathVariable("fileId") String fileId) throws IOException {
		FileObject fp = fileUploadService.findById(fileId);
		if (fp == null) {
			return null;
		}

		String path = "C:\\upload\\tt.jpg";
		String fn = fp.getFileName();
		fn = fn.substring(0, fn.lastIndexOf("."));
		String entryName = fn+"/preview.jpg";
		File file = new File(fp.getPath(), fp.getRealName());
		ZipFile zf = new ZipFile(file);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;
		while ((ze = zin.getNextEntry()) != null) {
			if (ze.isDirectory()) {
			} else if(ze.getName().equals(entryName)){
				System.err.println("file - " + ze.getName() + " : " + ze.getSize() + " bytes");
				break;
			}
		}
		zin.closeEntry();
		// ZipEntry previewEntry = zipFile.getEntry("preview.jpg");
		if (ze == null) {
			return null;
		}
		HttpHeaders headers = new HttpHeaders();
		String fileName = new String("preview.jpg".getBytes("UTF-8"), "iso-8859-1");// 为了解决中文名称乱码问题
		headers.setContentDispositionFormData("attachment", fileName);
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		DataInputStream ds = new DataInputStream(zf.getInputStream(ze));
		byte[] buff = new byte[(int) ze.getSize()];
		ds.readFully(buff);
		return new ResponseEntity<byte[]>(buff, headers, HttpStatus.CREATED);
	}
}
