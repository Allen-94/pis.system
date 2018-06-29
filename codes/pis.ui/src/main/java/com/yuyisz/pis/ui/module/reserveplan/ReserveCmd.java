package com.yuyisz.pis.ui.module.reserveplan;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 预案包含的步骤
 * @author mengz
 *
 */
@Entity
@Table(name="t_reserveCmd")
public class ReserveCmd {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int step; //步骤编号
	private String content;//播放信息
	
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,targetEntity=ReserveAction.class)
	private ReserveAction action; //步骤包含的指令
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public ReserveAction getAction() {
		return action;
	}
	public void setAction(ReserveAction action) {
		this.action = action;
	}
}
