package com.yuyisz.pis.ui.vo;

import java.util.List;

/**
 * 新增/修改紧急预案的VO
 * @author mengz
 *
 */
public class ReservePlanVO {

	private int id;
	private String planName;
	private String planDesc;
	private String priority; //优先级
	private int level;	//预案级别    ， 设计了5级
	private String playerIds;//包含的播控ID，以逗号分隔
	
	private List<ReserveCmdVO> cmds;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanDesc() {
		return planDesc;
	}

	public void setPlanDesc(String planDesc) {
		this.planDesc = planDesc;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPlayerIds() {
		return playerIds;
	}

	public void setPlayerIds(String playerIds) {
		this.playerIds = playerIds;
	}

	public List<ReserveCmdVO> getCmds() {
		return cmds;
	}

	public void setCmds(List<ReserveCmdVO> cmds) {
		this.cmds = cmds;
	}
}
