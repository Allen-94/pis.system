package com.yuyisz.pis.ui.vo;

import java.util.List;

public class PlayerRoleVo {
	private long playerRoleId;
	private String playerRoleName;
	private long playerListId;
	private List<Integer> playerIds;
	private long contentPackageId;
	private long playerForamtId;
	private long switchPlayerFormatId;
	private int switchTime;
	private boolean monday ;
	private boolean tuesday ;
	private boolean wednesday ;
	private boolean thursday ;
	private boolean friday ;
	private boolean saturday ;
	private boolean sunday;
	private String startTime;
	private String endTime;
	public long getPlayerListId() {
		return playerListId;
	}
	public void setPlayerListId(long playerListId) {
		this.playerListId = playerListId;
	}
	public long getPlayerRoleId() {
		return playerRoleId;
	}
	public void setPlayerRoleId(long playerRoleId) {
		this.playerRoleId = playerRoleId;
	}
	/*public long getPlayerGroupId() {
		return playerGroupId;
	}
	public void setPlayerGroupId(long playerGroupId) {
		this.playerGroupId = playerGroupId;
	}*/
	
	public long getContentPackageId() {
		return contentPackageId;
	}
	
	public List<Integer> getPlayerIds() {
		return playerIds;
	}
	public void setPlayerIds(List<Integer> playerIds) {
		this.playerIds = playerIds;
	}
	public String getPlayerRoleName() {
		return playerRoleName;
	}
	public void setPlayerRoleName(String playerRoleName) {
		this.playerRoleName = playerRoleName;
	}
	public void setContentPackageId(long contentPackageId) {
		this.contentPackageId = contentPackageId;
	}
	public long getPlayerForamtId() {
		return playerForamtId;
	}
	public void setPlayerForamtId(long playerForamtId) {
		this.playerForamtId = playerForamtId;
	}
	public int getSwitchTime() {
		return switchTime;
	}
	public void setSwitchTime(int switchTime) {
		this.switchTime = switchTime;
	}
	public long getSwitchPlayerFormatId() {
		return switchPlayerFormatId;
	}
	public void setSwitchPlayerFormatId(long switchPlayerFormatId) {
		this.switchPlayerFormatId = switchPlayerFormatId;
	}
	public boolean isMonday() {
		return monday;
	}
	public void setMonday(boolean monday) {
		this.monday = monday;
	}
	public boolean isTuesday() {
		return tuesday;
	}
	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}
	public boolean isWednesday() {
		return wednesday;
	}
	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}
	public boolean isThursday() {
		return thursday;
	}
	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}
	public boolean isFriday() {
		return friday;
	}
	public void setFriday(boolean friday) {
		this.friday = friday;
	}
	public boolean isSaturday() {
		return saturday;
	}
	public void setSaturday(boolean saturday) {
		this.saturday = saturday;
	}
	public boolean isSunday() {
		return sunday;
	}
	public void setSunday(boolean sunday) {
		this.sunday = sunday;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
