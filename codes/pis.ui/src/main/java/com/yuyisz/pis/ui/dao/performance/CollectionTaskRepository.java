package com.yuyisz.pis.ui.dao.performance;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.performance.CollectionTask;

@Repository
@Transactional
public interface CollectionTaskRepository extends JpaRepository<CollectionTask, Long>{
	
	/**
	 * 批量删除
	 * @param is_alive
	 * @param id
	 * @return
	 */
	@Modifying
    @Query("update CollectionTask set isAlive = ?1 where id in ?2")
    int setAlive(boolean is_alive,List<Long> id);

	/**
	 * 查找所有有效的性能采集任务
	 * @param b
	 * @return
	 */
	List<CollectionTask> findByIsAlive(boolean b);
	
	/**
	 * 启用任务
	 * @param id
	 * @param status
	 */
	@Modifying
    @Query("update CollectionTask set status = ?2 where id = ?1")
	void startUsing(long id, int status);
	
	/**
	 * 查找所有有效的性能采集任务,按照ID排序
	 * @param b
	 * @return
	 */
	List<CollectionTask> findByIsAliveOrderById(boolean b);
}
