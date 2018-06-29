package com.yuyisz.pis.ui.module.security;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 *管理域
 */
@Entity
@Table(name="t_groupfields")
public class GroupField implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FIELD_ID")
	private int fieldId;
	@JsonIgnore
	@ManyToOne(cascade= {CascadeType.REFRESH,CascadeType.MERGE},fetch=FetchType.LAZY, optional = true)
	@JoinColumn(name="USER_GROUP_ID",nullable=false)
	private UserGroup userGroup;
	@Column(name="ID1",nullable=false)
	private int id1;
	@Column(name="ID2",nullable=false)
	private int id2;
	@Column(name="ID3",nullable=false)
	private int id3;
	@Column(name="ID4",nullable=false)
	private int id4;
	@Column(name="ID5",nullable=false)
	private int id5;
	@Column(name="RESOURCE_TYPE",nullable=false)
	private int resourceType;
	public UserGroup getUserGroup() {
		return userGroup;
	}
	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}
	
	public int getFieldId() {
		return fieldId;
	}
	public void setFieldId(int fieldId) {
		this.fieldId = fieldId;
	}
	public int getId1() {
		return id1;
	}
	public void setId1(int id1) {
		this.id1 = id1;
	}
	public int getId2() {
		return id2;
	}
	public void setId2(int id2) {
		this.id2 = id2;
	}
	public int getId3() {
		return id3;
	}
	public void setId3(int id3) {
		this.id3 = id3;
	}
	public int getId4() {
		return id4;
	}
	public void setId4(int id4) {
		this.id4 = id4;
	}
	public int getId5() {
		return id5;
	}
	public void setId5(int id5) {
		this.id5 = id5;
	}
	public int getResourceType() {
		return resourceType;
	}
	public void setResourceType(int resourceType) {
		this.resourceType = resourceType;
	}
	
	
	
}
