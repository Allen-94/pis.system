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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;

/**
 * 播控分组
 * @author mengz
 *
 */
@Entity
@Table(name="t_playergroup")
public class PlayerGroup {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(nullable=false,unique=true,updatable=true)
	private String groupName;
	private String description;
	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.EAGER,optional=false,targetEntity=User.class)
	private User creater;
	private Date createDate;
	@ManyToMany(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY,targetEntity=DevResources.class)
	private Set<DevResources> players;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Set<DevResources> getPlayers() {
		return players;
	}
	public void setPlayers(Set<DevResources> players) {
		this.players = players;
	}
}
