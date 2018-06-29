package com.yuyisz.pis.ui.module.approveflow;
/**
 * 流程审批过程信息
 * @author mengz
 *
 */

import java.util.Date;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuyisz.pis.ui.module.security.User;
@Entity
@Table(name="t_flow_process")
public class FlowProcess {
	@Id
	private String id=UUID.randomUUID().toString();
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER,targetEntity=ApproveFlow.class)
	@JoinColumn(name="approve_flow_id")
	@JsonIgnore
	private ApproveFlow approveFlow;
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	private User processer;
	private int processResult;
	private String processOpinionr;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date processDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ApproveFlow getApproveFlow() {
		return approveFlow;
	}
	public void setApproveFlow(ApproveFlow approveFlow) {
		this.approveFlow = approveFlow;
	}
	public User getProcesser() {
		return processer;
	}
	public void setProcesser(User processer) {
		this.processer = processer;
	}
	public int getProcessResult() {
		return processResult;
	}
	public void setProcessResult(int processResult) {
		this.processResult = processResult;
	}
	public String getProcessOpinionr() {
		return processOpinionr;
	}
	public void setProcessOpinionr(String processOpinionr) {
		this.processOpinionr = processOpinionr;
	}
	public Date getProcessDate() {
		return processDate;
	}
	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}
}
