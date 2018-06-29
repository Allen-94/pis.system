package com.yuyisz.pis.ui.module.player;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.mockito.Incubating;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;

/**
 * 比表的规则
 * @author mengz
 *
 */
@Entity
@Table(name="t_playrole",uniqueConstraints= {@UniqueConstraint(columnNames= {"playRoleName","play_list_id"})})
public class PlayerRole {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false)
	private String playRoleName;
	/*@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,optional=true,targetEntity=PlayerGroup.class)
	private PlayerGroup playerGroup;*/
	@ManyToMany(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY,targetEntity=DevResources.class)
	private Set<DevResources> players;
	private boolean monday ;
	private boolean tuesday ;
	private boolean wednesday ;
	private boolean thursday ;
	private boolean friday ;
	private boolean saturday ;
	private boolean sunday;
	private String startTime;
	private String endTime;
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,optional=true,targetEntity=ContentPackage.class)
	private ContentPackage contentPackage;
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,optional=true,targetEntity=PlayerFormat.class)
	private PlayerFormat playerFormat;
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,optional=true,targetEntity=PlayerFormat.class)
	private PlayerFormat switchPlayerFormat;
	private int switchTime;
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,optional=false,targetEntity=User.class)
	private User creater;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createDate;
	@JsonIgnore
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.EAGER,optional=true,targetEntity=PlayerList.class)
	private PlayerList playList;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPlayRoleName() {
		return playRoleName;
	}
	public void setPlayRoleName(String playRoleName) {
		this.playRoleName = playRoleName;
	}
	
	public Set<DevResources> getPlayers() {
		return players;
	}
	public void setPlayers(Set<DevResources> players) {
		this.players = players;
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
	public ContentPackage getContentPackage() {
		return contentPackage;
	}
	public void setContentPackage(ContentPackage contentPackage) {
		this.contentPackage = contentPackage;
	}
	public PlayerFormat getPlayerFormat() {
		return playerFormat;
	}
	public void setPlayerFormat(PlayerFormat playerFormat) {
		this.playerFormat = playerFormat;
	}
	public PlayerFormat getSwitchPlayerFormat() {
		return switchPlayerFormat;
	}
	public void setSwitchPlayerFormat(PlayerFormat switchPlayerFormat) {
		this.switchPlayerFormat = switchPlayerFormat;
	}
	public int getSwitchTime() {
		return switchTime;
	}
	public void setSwitchTime(int switchTime) {
		this.switchTime = switchTime;
	}
	public User getCreater() {
		return creater;
	}
	public void setCreater(User creater) {
		this.creater = creater;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public PlayerList getPlayList() {
		return playList;
	}
	public void setPlayList(PlayerList playList) {
		this.playList = playList;
	}
}
