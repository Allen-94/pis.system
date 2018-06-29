package com.yuyisz.pis.ui.module.reserveplan;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 预案的指令
 * @author mengz
 *
 */
@Entity
@Table(name="t_reserveAction")
public class ReserveAction {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String actionName; 		//指令名称
	private String actionContent;	//指令内容
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getActionContent() {
		return actionContent;
	}
	public void setActionContent(String actionContent) {
		this.actionContent = actionContent;
	}
}
