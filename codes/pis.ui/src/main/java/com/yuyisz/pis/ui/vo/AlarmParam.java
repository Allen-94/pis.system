package com.yuyisz.pis.ui.vo;

public class AlarmParam {
	private int dates;	        	//最近天数  0：全部
	private int grade;              //级别
	private int confirmStatu;   //确认状态  0:全部 1：确认 2：未确认
	private int cleanStatu;     //清楚状态 0:全部 1：确认 2：未确认
	private int sourceId;			//告警源

	public int getDates() {
		return dates;
	}

	public void setDates(int dates) {
		this.dates = dates;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getConfirmStatu() {
		return confirmStatu;
	}

	public void setConfirmStatu(int confirmStatu) {
		this.confirmStatu = confirmStatu;
	}

	public int getCleanStatu() {
		return cleanStatu;
	}

	public void setCleanStatu(int cleanStatu) {
		this.cleanStatu = cleanStatu;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
}
