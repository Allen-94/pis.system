package com.yuyisz.pis.ui.module.performance;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yuyisz.pis.ui.module.security.DevResources;

/**
 * 设备性能监控的对象
 * @author mengz
 */
@Entity
@Table(name="t_collection_info")
public class CollectionInfo {   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;	
	
	@ManyToOne(cascade = CascadeType.MERGE, optional = true,fetch=FetchType.EAGER)
	private DevResources dev;
	
	/**
     * cpu使用率.
     */
    private double cpuRatio;   
	/**
     * 内存使用率
     */
    private double memRatio;   
   /**
    * 硬盘使用率
    */
    private double diskRatio;
    /**
     * 功耗
     */
    private double powerDiss;
    
    /**
     * 网络流量
     */
    private double netTraffic;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime; //创建时间

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public DevResources getDev() {
		return dev;
	}

	public void setDev(DevResources dev) {
		this.dev = dev;
	}

	public double getMemRatio() {
		return memRatio;
	}

	public void setMemRatio(double memRatio) {
		this.memRatio = memRatio;
	}

	public double getDiskRatio() {
		return diskRatio;
	}

	public void setDiskRatio(double diskRatio) {
		this.diskRatio = diskRatio;
	}

	public double getCpuRatio() {
		return cpuRatio;
	}

	public void setCpuRatio(double cpuRatio) {
		this.cpuRatio = cpuRatio;
	}

	public double getPowerDiss() {
		return powerDiss;
	}

	public void setPowerDiss(double powerDiss) {
		this.powerDiss = powerDiss;
	}

	public double getNetTraffic() {
		return netTraffic;
	}

	public void setNetTraffic(double netTraffic) {
		this.netTraffic = netTraffic;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setRatios(double cpuRatio, double memRatio, double diskRatio, double powerDiss, double netTraffic) {
		this.cpuRatio=cpuRatio;
		this.memRatio=memRatio;
		this.diskRatio=diskRatio;
		this.powerDiss=powerDiss;
		this.netTraffic=netTraffic;
	}
}  
