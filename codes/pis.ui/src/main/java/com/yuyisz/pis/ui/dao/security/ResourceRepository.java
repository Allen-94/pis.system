package com.yuyisz.pis.ui.dao.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.security.DevResources;

@Repository
public interface ResourceRepository extends JpaRepository<DevResources, Integer>, JpaSpecificationExecutor<DevResources> {

	List<DevResources> findByResourceType(int type);
	
	@Query(value="select b.id,b.id1,b.id2,b.id3,b.id4,b.id5 ,b.collection_task_id,b.resource_name,b.create_time,b.update_time,b.resource_type \r\n" + 
			"from t_groupfields a ,t_resources b where a.user_group_id = ?1 \r\n" + 
			"and a.id1 = b.id1 and a.id2 = b.id2 and a.id3 = b.id3 and a.id4 = b.id4 \r\n" + 
			"and a.id5 = b.id5 and a.resource_type = b.resource_type",nativeQuery=true)
	List<DevResources> findResourceOfGroup(int groupId);

	List<DevResources> findById2AndResourceType(int lineId, int type);

	DevResources findById(int lineId);

	List<DevResources> findById2AndId3AndResourceType(int id2, int id3, int type);

	List<DevResources> findByResourceTypeIn(List<Integer> types);
	
	DevResources findById2AndId3AndId4AndId5AndAndResourceType(int id2,int id3,int id4,int id5,int type);

	List<DevResources> findById2AndId3AndId4AndResourceType(Integer line_id, Integer station_id, Integer pc_id, int i);
}
