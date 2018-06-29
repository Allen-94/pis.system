package com.yuyisz.pis.ui.service.fileupload;

import java.io.IOException;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.yuyisz.pis.ui.module.resources.FileObject;

public interface FileUploadService {

	FileObject saveFileObject(FileObject fb);
	Map<String,Object> uploadFile(String uploadPath ,MultipartFile file)throws IOException;
	Map<String,Object> delFile(String fileId)throws Exception;
	String getUploadFilePath(String uploadPath);
	FileObject findById(String fileId);
}
