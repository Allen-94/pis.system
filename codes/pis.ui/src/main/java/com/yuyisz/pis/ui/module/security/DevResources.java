package com.yuyisz.pis.ui.module.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yuyisz.pis.ui.module.performance.CollectionTask;
import com.yuyisz.pis.ui.module.reserveplan.ReserveCmd;
import com.yuyisz.pis.ui.module.reserveplan.ReservePlan;
import com.yuyisz.pis.ui.module.reserveplan.ReserverPlanTask;
/*
 * 资源
 */
@Entity
@Table(name="t_resources",uniqueConstraints= {@UniqueConstraint(
		columnNames= {"id1","id2","id3","id4","id5","resource_type"})})
public class DevResources{
	
	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private int id;
	@Column(name="ID1", nullable=false)
	private int id1;
	@Column(name="ID2", nullable=false)
	private int id2;
	@Column(name="ID3", nullable=false)
	private int id3;
	@Column(name="ID4", nullable=false)
	private int id4;
	@Column(name="ID5", nullable=false)
	private int id5;
	@Column(name="RESOURCE_TYPE", nullable=false)
	private int resourceType;
	@Column(name="RESOURCE_NAME", nullable=false)
	private String resourceName;
	
	@JsonIgnore
	@ManyToOne(cascade=CascadeType.MERGE,fetch=FetchType.LAZY)
	private CollectionTask collectionTask;
	
	@JsonIgnore
	@OneToMany(cascade= CascadeType.ALL ,fetch=FetchType.EAGER)
	private Set<ReserverPlanTask> reserveTask;			//设备要执行的预案任务列表
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public void setIds(int id1, int id2, int id3, int id4, int id5) {
		this.id1=id1;
		this.id2=id2;
		this.id3=id3;
		this.id4=id4;
		this.id5=id5;
	}
	public CollectionTask getCollectionTask() {
		return collectionTask;
	}
	public void setCollectionTask(CollectionTask collectionTask) {
		this.collectionTask = collectionTask;
	}
	public Set<ReserverPlanTask> getReserveTask() {
		return reserveTask;
	}
	public void setReserveTask(Set<ReserverPlanTask> reserveTask) {
		this.reserveTask = reserveTask;
	}
	
	@JsonIgnore
	public List<ReserverPlanTask> getSortedReserverTasks() {
		List<ReserverPlanTask> tasks = new ArrayList<>(this.reserveTask);
		Collections.sort(tasks, new Comparator<ReserverPlanTask>() {
			@Override
			public int compare(ReserverPlanTask o1, ReserverPlanTask o2) {
				return o1.getStep()<o2.getStep() ? -1 :1;
			}
		});
		return tasks;
	}
}
