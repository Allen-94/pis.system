package com.yuyisz.pis.ui.module.subsystem;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 站点模板
 */
@Entity
@Table(name="stationtemplate")
public class StationTemplate implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer TEMPLATE_ID;
	
	@Column(name="TEMPLATE_NAME",nullable=false)
	private String TEMPLATE_NAME;
	
	@Column(name="STATION_SERVER_IS_USED",nullable=false)
	private Integer STATION_SERVER_IS_USED;
	
	@Column(name="STATION_SERVER_NAME",nullable=false)
	private String STATION_SERVER_NAME;
	
	@Column(name="STATION_SERVER_PRODUCER",nullable=false)
	private Integer STATION_SERVER_PRODUCER;
	
	@Column(name="STATION_SERVER_MODEL",nullable=false)
	private Integer STATION_SERVER_MODEL;
	
	@Column(name="STATION_WS_NUMBER",nullable=false)
	private Integer STATION_WS_NUMBER;
	
	@Column(name="STATION_WS_FAMILY_NAME",nullable=false)
	private String STATION_WS_FAMILY_NAME;
	
	@Column(name="PC1_NAME",nullable=false)
	private String PC1_NAME;
	
	@Column(name="PC1_PLACE",nullable=false)
	private Integer PC1_PLACE;
	
	@Column(name="PC1_PRODUCER",nullable=false)
	private Integer PC1_PRODUCER;
	
	@Column(name="PC1_MODEL",nullable=false)
	private Integer PC1_MODEL;
	
	@Column(name="PC1_SCREEN_NUMBER",nullable=false)
	private Integer PC1_SCREEN_NUMBER;
	
	@Column(name="PC1_SCREEN_FAMILY_NAME",nullable=false)
	private String PC1_SCREEN_FAMILY_NAME;
	
	@Column(name="PC1_SCREEN_PRODUCER",nullable=false)
	private Integer PC1_SCREEN_PRODUCER;
	
	@Column(name="PC1_SCREEN_MODEL",nullable=false)
	private Integer PC1_SCREEN_MODEL;
	
	@Column(name="PC2_NAME",nullable=false)
	private String PC2_NAME;
	
	@Column(name="PC2_PLACE",nullable=false)
	private Integer PC2_PLACE;
	
	@Column(name="PC2_PRODUCER",nullable=false)
	private Integer PC2_PRODUCER;
	
	@Column(name="PC2_MODEL",nullable=false)
	private Integer PC2_MODEL;
	
	@Column(name="PC2_SCREEN_NUMBER",nullable=false)
	private Integer PC2_SCREEN_NUMBER;
	
	@Column(name="PC2_SCREEN_FAMILY_NAME",nullable=false)
	private String PC2_SCREEN_FAMILY_NAME;
	
	@Column(name="PC2_SCREEN_PRODUCER",nullable=false)
	private Integer PC2_SCREEN_PRODUCER;
	
	@Column(name="PC2_SCREEN_MODEL",nullable=false)
	private Integer PC2_SCREEN_MODEL;
	
	@Column(name="PC3_NAME",nullable=false)
	private String PC3_NAME;
	
	@Column(name="PC3_PLACE",nullable=false)
	private Integer PC3_PLACE;
	
	@Column(name="PC3_PRODUCER",nullable=false)
	private Integer PC3_PRODUCER;
	
	@Column(name="PC3_MODEL",nullable=false)
	private Integer PC3_MODEL;
	
	@Column(name="PC3_SCREEN_NUMBER",nullable=false)
	private Integer PC3_SCREEN_NUMBER;
	
	@Column(name="PC3_SCREEN_FAMILY_NAME",nullable=false)
	private String PC3_SCREEN_FAMILY_NAME;
	
	@Column(name="PC3_SCREEN_PRODUCER",nullable=false)
	private Integer PC3_SCREEN_PRODUCER;
	
	@Column(name="PC3_SCREEN_MODEL",nullable=false)
	private Integer PC3_SCREEN_MODEL;
	
	public Integer getTEMPLATE_ID() {
		return TEMPLATE_ID;
	}
	public void setTEMPLATE_ID(Integer tEMPLATE_ID) {
		TEMPLATE_ID = tEMPLATE_ID;
	}
	public String getTEMPLATE_NAME() {
		return TEMPLATE_NAME;
	}
	public void setTEMPLATE_NAME(String tEMPLATE_NAME) {
		TEMPLATE_NAME = tEMPLATE_NAME;
	}
	public Integer getSTATION_SERVER_IS_USED() {
		return STATION_SERVER_IS_USED;
	}
	public void setSTATION_SERVER_IS_USED(Integer sTATION_SERVER_IS_USED) {
		STATION_SERVER_IS_USED = sTATION_SERVER_IS_USED;
	}
	public String getSTATION_SERVER_NAME() {
		return STATION_SERVER_NAME;
	}
	public void setSTATION_SERVER_NAME(String sTATION_SERVER_NAME) {
		STATION_SERVER_NAME = sTATION_SERVER_NAME;
	}
	public Integer getSTATION_SERVER_PRODUCER() {
		return STATION_SERVER_PRODUCER;
	}
	public void setSTATION_SERVER_PRODUCER(Integer sTATION_SERVER_PRODUCER) {
		STATION_SERVER_PRODUCER = sTATION_SERVER_PRODUCER;
	}
	public Integer getSTATION_SERVER_MODEL() {
		return STATION_SERVER_MODEL;
	}
	public void setSTATION_SERVER_MODEL(Integer sTATION_SERVER_MODEL) {
		STATION_SERVER_MODEL = sTATION_SERVER_MODEL;
	}
	public Integer getSTATION_WS_NUMBER() {
		return STATION_WS_NUMBER;
	}
	public void setSTATION_WS_NUMBER(Integer sTATION_WS_NUMBER) {
		STATION_WS_NUMBER = sTATION_WS_NUMBER;
	}
	public String getSTATION_WS_FAMILY_NAME() {
		return STATION_WS_FAMILY_NAME;
	}
	public void setSTATION_WS_FAMILY_NAME(String sTATION_WS_FAMILY_NAME) {
		STATION_WS_FAMILY_NAME = sTATION_WS_FAMILY_NAME;
	}
	public String getPC1_NAME() {
		return PC1_NAME;
	}
	public void setPC1_NAME(String pC1_NAME) {
		PC1_NAME = pC1_NAME;
	}
	public Integer getPC1_PLACE() {
		return PC1_PLACE;
	}
	public void setPC1_PLACE(Integer pC1_PLACE) {
		PC1_PLACE = pC1_PLACE;
	}
	public Integer getPC1_PRODUCER() {
		return PC1_PRODUCER;
	}
	public void setPC1_PRODUCER(Integer pC1_PRODUCER) {
		PC1_PRODUCER = pC1_PRODUCER;
	}
	public Integer getPC1_MODEL() {
		return PC1_MODEL;
	}
	public void setPC1_MODEL(Integer pC1_MODEL) {
		PC1_MODEL = pC1_MODEL;
	}
	public Integer getPC1_SCREEN_NUMBER() {
		return PC1_SCREEN_NUMBER;
	}
	public void setPC1_SCREEN_NUMBER(Integer pC1_SCREEN_NUMBER) {
		PC1_SCREEN_NUMBER = pC1_SCREEN_NUMBER;
	}
	public String getPC1_SCREEN_FAMILY_NAME() {
		return PC1_SCREEN_FAMILY_NAME;
	}
	public void setPC1_SCREEN_FAMILY_NAME(String pC1_SCREEN_FAMILY_NAME) {
		PC1_SCREEN_FAMILY_NAME = pC1_SCREEN_FAMILY_NAME;
	}
	public Integer getPC1_SCREEN_PRODUCER() {
		return PC1_SCREEN_PRODUCER;
	}
	public void setPC1_SCREEN_PRODUCER(Integer pC1_SCREEN_PRODUCER) {
		PC1_SCREEN_PRODUCER = pC1_SCREEN_PRODUCER;
	}
	public Integer getPC1_SCREEN_MODEL() {
		return PC1_SCREEN_MODEL;
	}
	public void setPC1_SCREEN_MODEL(Integer pC1_SCREEN_MODEL) {
		PC1_SCREEN_MODEL = pC1_SCREEN_MODEL;
	}
	public String getPC2_NAME() {
		return PC2_NAME;
	}
	public void setPC2_NAME(String pC2_NAME) {
		PC2_NAME = pC2_NAME;
	}
	public Integer getPC2_PLACE() {
		return PC2_PLACE;
	}
	public void setPC2_PLACE(Integer pC2_PLACE) {
		PC2_PLACE = pC2_PLACE;
	}
	public Integer getPC2_PRODUCER() {
		return PC2_PRODUCER;
	}
	public void setPC2_PRODUCER(Integer pC2_PRODUCER) {
		PC2_PRODUCER = pC2_PRODUCER;
	}
	public Integer getPC2_MODEL() {
		return PC2_MODEL;
	}
	public void setPC2_MODEL(Integer pC2_MODEL) {
		PC2_MODEL = pC2_MODEL;
	}
	public Integer getPC2_SCREEN_NUMBER() {
		return PC2_SCREEN_NUMBER;
	}
	public void setPC2_SCREEN_NUMBER(Integer pC2_SCREEN_NUMBER) {
		PC2_SCREEN_NUMBER = pC2_SCREEN_NUMBER;
	}
	public String getPC2_SCREEN_FAMILY_NAME() {
		return PC2_SCREEN_FAMILY_NAME;
	}
	public void setPC2_SCREEN_FAMILY_NAME(String pC2_SCREEN_FAMILY_NAME) {
		PC2_SCREEN_FAMILY_NAME = pC2_SCREEN_FAMILY_NAME;
	}
	public Integer getPC2_SCREEN_PRODUCER() {
		return PC2_SCREEN_PRODUCER;
	}
	public void setPC2_SCREEN_PRODUCER(Integer pC2_SCREEN_PRODUCER) {
		PC2_SCREEN_PRODUCER = pC2_SCREEN_PRODUCER;
	}
	public Integer getPC2_SCREEN_MODEL() {
		return PC2_SCREEN_MODEL;
	}
	public void setPC2_SCREEN_MODEL(Integer pC2_SCREEN_MODEL) {
		PC2_SCREEN_MODEL = pC2_SCREEN_MODEL;
	}
	public String getPC3_NAME() {
		return PC3_NAME;
	}
	public void setPC3_NAME(String pC3_NAME) {
		PC3_NAME = pC3_NAME;
	}
	public Integer getPC3_PLACE() {
		return PC3_PLACE;
	}
	public void setPC3_PLACE(Integer pC3_PLACE) {
		PC3_PLACE = pC3_PLACE;
	}
	public Integer getPC3_PRODUCER() {
		return PC3_PRODUCER;
	}
	public void setPC3_PRODUCER(Integer pC3_PRODUCER) {
		PC3_PRODUCER = pC3_PRODUCER;
	}
	public Integer getPC3_MODEL() {
		return PC3_MODEL;
	}
	public void setPC3_MODEL(Integer pC3_MODEL) {
		PC3_MODEL = pC3_MODEL;
	}
	public Integer getPC3_SCREEN_NUMBER() {
		return PC3_SCREEN_NUMBER;
	}
	public void setPC3_SCREEN_NUMBER(Integer pC3_SCREEN_NUMBER) {
		PC3_SCREEN_NUMBER = pC3_SCREEN_NUMBER;
	}
	public String getPC3_SCREEN_FAMILY_NAME() {
		return PC3_SCREEN_FAMILY_NAME;
	}
	public void setPC3_SCREEN_FAMILY_NAME(String pC3_SCREEN_FAMILY_NAME) {
		PC3_SCREEN_FAMILY_NAME = pC3_SCREEN_FAMILY_NAME;
	}
	public Integer getPC3_SCREEN_PRODUCER() {
		return PC3_SCREEN_PRODUCER;
	}
	public void setPC3_SCREEN_PRODUCER(Integer pC3_SCREEN_PRODUCER) {
		PC3_SCREEN_PRODUCER = pC3_SCREEN_PRODUCER;
	}
	public Integer getPC3_SCREEN_MODEL() {
		return PC3_SCREEN_MODEL;
	}
	public void setPC3_SCREEN_MODEL(Integer pC3_SCREEN_MODEL) {
		PC3_SCREEN_MODEL = pC3_SCREEN_MODEL;
	}

}

