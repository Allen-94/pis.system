package com.yuyisz.pis.ui.vo;

/**
 * 紧急预案步骤VO
 * @author mengz
 *
 */
public class ReserveCmdVO {

	private int id;
	private int step; //步骤编号
	private String content;//播放信息
	private int actionId;
	
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
	public int getActionId() {
		return actionId;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
}
