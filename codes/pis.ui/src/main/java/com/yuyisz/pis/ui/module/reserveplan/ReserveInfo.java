package com.yuyisz.pis.ui.module.reserveplan;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyisz.pis.ui.module.security.User;

/**
 * 预定义信息
 * @author mengz
 *
 */
@Entity
@Table(name="t_reserveInfo")
public class ReserveInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String infoName; //信息名曾
	private String infoContent;//显示内容
	private int infoType; //信息类别    1：线网级，2：线路级，3：车站级 4：列车级 5：其他
	private boolean isApproved;//是否批准
	
	@Column(nullable=true)
	private String  approveFlowId;//审批流程单
	
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER,optional=false,targetEntity=User.class)
	private User creater;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getInfoName() {
		return infoName;
	}
	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}
	public String getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	public int getInfoType() {
		return infoType;
	}
	public void setInfoType(int infoType) {
		this.infoType = infoType;
	}
	public boolean isApproved() {
		return isApproved;
	}
	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	/*public ApproveFlow getApproveFlow() {
		return approveFlow;
	}
	public void setApproveFlow(ApproveFlow approveFlow) {
		this.approveFlow = approveFlow;
	}*/
	
	public User getCreater() {
		return creater;
	}
	public String getApproveFlowId() {
		return approveFlowId;
	}
	public void setApproveFlowId(String approveFlowId) {
		this.approveFlowId = approveFlowId;
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
}
