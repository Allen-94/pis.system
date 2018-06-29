package com.yuyisz.pis.ui.module.approveflow;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyisz.pis.ui.module.infosources.MediaSource;
import com.yuyisz.pis.ui.module.security.User;

/**
 * 审批流程单
 * @author mengz
 *
 */
@Entity
@Table(name="t_approve_flow")
public class ApproveFlow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String id = UUID.randomUUID().toString();
	private String flowName;
	private String flowDescribe;
	@OneToMany(cascade= {CascadeType.REFRESH},targetEntity=MediaSource.class,orphanRemoval=false,mappedBy="flow_id")
	@NotFound(action=NotFoundAction.IGNORE)
	private Set<MediaSource> referencedSources;
	@ManyToOne(cascade= {CascadeType.REFRESH},fetch=FetchType.EAGER)
	private User approver1;
	@ManyToOne(cascade= {CascadeType.REFRESH},fetch=FetchType.EAGER)
	private User approver2;
	@ManyToOne(cascade= {CascadeType.REFRESH},fetch=FetchType.EAGER)
	private User approver3;
	@ManyToOne(cascade= {CascadeType.REFRESH},fetch=FetchType.EAGER)
	private User currentApprover;
	private int approveState;
	@ManyToOne(cascade= {CascadeType.REFRESH},fetch=FetchType.EAGER)
	private User submitter;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createDate;
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="approve_flow_id")
	private Set<FlowProcess> flowProcessSet;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getFlowDescribe() {
		return flowDescribe;
	}
	public void setFlowDescribe(String flowDescribe) {
		this.flowDescribe = flowDescribe;
	}
	public Set<MediaSource> getReferencedSources() {
		return referencedSources;
	}
	public void setReferencedSources(Set<MediaSource> referencedSources) {
		this.referencedSources = referencedSources;
	}
	public User getApprover1() {
		return approver1;
	}
	public void setApprover1(User approver1) {
		this.approver1 = approver1;
	}
	public User getApprover2() {
		return approver2;
	}
	public void setApprover2(User approver2) {
		this.approver2 = approver2;
	}
	public User getApprover3() {
		return approver3;
	}
	public void setApprover3(User approver3) {
		this.approver3 = approver3;
	}
	public User getCurrentApprover() {
		return currentApprover;
	}
	public void setCurrentApprover(User currentApprover) {
		this.currentApprover = currentApprover;
	}
	public int getApproveState() {
		return approveState;
	}
	public void setApproveState(int approveState) {
		this.approveState = approveState;
	}
	public User getSubmitter() {
		return submitter;
	}
	public void setSubmitter(User submitter) {
		this.submitter = submitter;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Set<FlowProcess> getFlowProcessSet() {
		return flowProcessSet;
	}
	public void setFlowProcessSet(Set<FlowProcess> flowProcessSet) {
		this.flowProcessSet = flowProcessSet;
	}
	public void initFlowProcessSet() {
		this.flowProcessSet = new HashSet<>();
		FlowProcess process1 = new FlowProcess();
		process1.setApproveFlow(this);
		process1.setProcesser(approver1);
		flowProcessSet.add(process1);
		if(this.approver2!=null) {
			FlowProcess process2 = new FlowProcess();
			process2.setApproveFlow(this);
			process2.setProcesser(approver2);
			flowProcessSet.add(process2);
		}
		if(this.approver3 != null) {
			FlowProcess process3 = new FlowProcess();
			process3.setApproveFlow(this);
			process3.setProcesser(approver3);
			flowProcessSet.add(process3);
		}
	}
	
	public User getNextApprover() {
		// 获取下一个处理人
		if(this.approveState == 1) {
			//通过
			if(this.currentApprover == this.approver1) {
				return this.approver2;
			}else if(this.currentApprover == this.approver2) {
				return this.approver3;
			}
		}else if(this.approveState == 2) {
			//打回修改，当前处理人改为提交人
			return this.submitter;
		}
		
		return null;
	}
}
