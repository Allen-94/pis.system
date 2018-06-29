package com.yuyisz.pis.ui.vo;

import java.util.Date;

public class PlayerListVo {

	private long playerListId;
	private String playerListName;
	private String description;
	private boolean isDefault;
	private Date startDate;
	private Date endDate;
	public long getPlayerListId() {
		return playerListId;
	}
	public void setPlayerListId(long playerListId) {
		this.playerListId = playerListId;
	}
	
	public String getPlayerListName() {
		return playerListName;
	}
	public void setPlayerListName(String playerListName) {
		this.playerListName = playerListName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isDefault() {
		return isDefault;
	}
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
