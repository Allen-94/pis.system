package com.yuyisz.pis.ui.module.player;
/**
 * 下发任务相关的文件
 * @author mengz
 *
 */

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.module.security.DevResources;
@Entity
@Table(name="t_issuedtaskfile")
public class IssuedTaskFile {

	@Id
	private String id;
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY,optional=true,targetEntity=PlayerListIssuedTask.class)
	private PlayerListIssuedTask task;	//下发任务
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY,optional=true,targetEntity=FileObject.class)
	private FileObject fileObject;		//下发的文件
	private int state;					//下发状态
	private int rate;					//下发的进度 百分比
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY,optional=true,targetEntity=DevResources.class)
	private DevResources receiver;		//接收文件的播控或其他设备
	private String recMsg; 				//反馈的消息
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public PlayerListIssuedTask getTask() {
		return task;
	}
	public void setTask(PlayerListIssuedTask task) {
		this.task = task;
	}
	public FileObject getFileObject() {
		return fileObject;
	}
	public void setFileObject(FileObject fileObject) {
		this.fileObject = fileObject;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public DevResources getReceiver() {
		return receiver;
	}
	public void setReceiver(DevResources receiver) {
		this.receiver = receiver;
	}
	public String getRecMsg() {
		return recMsg;
	}
	public void setRecMsg(String recMsg) {
		this.recMsg = recMsg;
	}
}
