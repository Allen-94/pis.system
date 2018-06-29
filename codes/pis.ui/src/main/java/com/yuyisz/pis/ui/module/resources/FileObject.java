package com.yuyisz.pis.ui.module.resources;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 保存上传的文件的对象
 * @author mengz
 *
 */
@Entity
@Table(name="t_fileobject")
public class FileObject {

	@Id
	private String id;          //文件id
	private String fileName;	//文件名
	private String path;		//文件保存路径
	private String realName;	//文件保存到系统的真实名称
	
	public FileObject() {
		this.id = UUID.randomUUID().toString();
	}
	
	public FileObject(String path ,String fileName) {
		this.id = UUID.randomUUID().toString();
		this.fileName = fileName;
		this.path = path;
		String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
		this.realName = this.id+"."+prefix;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
}
