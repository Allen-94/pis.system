package com.yuyisz.pis.ui.module.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 操作日志
 * @author 郭芝辰
 *
 */
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyisz.pis.ui.module.security.User;
@Entity
@Table(name="t_operationlogs")
public class OperationLog {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer logId;
	@Column(name="OPERATOR",nullable=true)
	private String operator;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	@Column(name="CLIENT_IP",nullable=true)
	private String clientIP;
	@Column(name="COMMAND",nullable=true)
	private String command;
	@Column(name="ARGUMENTS",nullable=true)
	private String arguments;
	@Column(name="RESULT",nullable=true)
	private String result;
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getClientIP() {
		return clientIP;
	}
	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getArguments() {
		return arguments;
	}
	public void setArguments(String arguments) {
		this.arguments = arguments;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
