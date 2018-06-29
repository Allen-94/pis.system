package com.yuyisz.pis.ui.module.player;
/**
 * 播表下发任务，下发时先创建下发任务，后台下发程序安排下发，并接受反馈，
 * 前端监听下发过程，如果下发失败，可以单独进行重新下发
 * 一个播控列表，可以根据设备分割成多个下发任务
 * @author mengz
 *
 */

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuyisz.pis.ui.module.resources.FileObject;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;
@Entity
@Table(name="t_PlayerListIssuedTask")
public class PlayerListIssuedTask {
	/**
	 * 任务ID
	 */
	@Id
	private String issuedTaskId;
	/**
	 * 任务相关播表
	 */
	@JsonIgnore
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER,optional=true,targetEntity=PlayerList.class)
	private PlayerList playerList;
	
	/**
	 * 下发的播控
	 */
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER,optional=true,targetEntity=DevResources.class)
	private DevResources resources;
	
	/**
	 * 下载资源类型,1:规则，2：版式 ，3：素材
	 */
	private int downloadType;
	
	/**
	 * 要下载的资源
	 */
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER,optional=true,targetEntity=FileObject.class)
	private FileObject file;
	
	/**
	 * 完成状态   
	 */
	private int state=0;
	
	/**
	 * 进度
	 */
	private int rate=0;
	
	
	/**
	 * 下发消息：主要存储下发失败的原因
	 */
	private String message;
	
	/**
	 * 下发人
	 */
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY,optional=true,targetEntity=User.class)
	private User creater;
	/**
	 * 创建日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createDate=new Date();;
	/**
	 * 最后更新日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date updateDate=new Date();
	/**
	 * 是否有效，软删除
	 */
	private boolean alive=true;
	
	public String getIssuedTaskId() {
		return issuedTaskId;
	}
	public void setIssuedTaskId(String issuedTaskId) {
		this.issuedTaskId = issuedTaskId;
	}
	public PlayerList getPlayerList() {
		return playerList;
	}
	
	
	public void setPlayerList(PlayerList playerList) {
		this.playerList = playerList;
	}
	public DevResources getResources() {
		return resources;
	}
	public void setResources(DevResources resources) {
		this.resources = resources;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public User getCreater() {
		return creater;
	}
	public void setCreater(User creater) {
		this.creater = creater;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public int getDownloadType() {
		return downloadType;
	}
	public void setDownloadType(int downloadType) {
		this.downloadType = downloadType;
	}
	public FileObject getFile() {
		return file;
	}
	public void setFile(FileObject file) {
		this.file = file;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
}
