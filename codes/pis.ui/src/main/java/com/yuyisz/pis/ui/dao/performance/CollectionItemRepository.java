package com.yuyisz.pis.ui.dao.performance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.performance.CollectionItem;
@Repository
public interface CollectionItemRepository extends JpaRepository<CollectionItem, Long>,JpaSpecificationExecutor<CollectionItem> {

	/*@Query(value="select * from t_monitor_info t where t.create_time in (select max(m.create_time) from t_monitor_info m " + 
			"group by m.dev_id " + 
			"having m.dev_id in (?1))" ,nativeQuery=true)
	List<CollectionInfo> findCurrentMonitorInfos(List<Integer> devIds);

	List<CollectionInfo> findByDevIdAndCreateTimeGreaterThanEqualOrderByCreateTime(int devId, Date endDate);
*/
}
