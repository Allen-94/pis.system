package com.yuyisz.pis.ui.dao.subsystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
/*
 * 播放控制器的dao
 */
@Repository
public interface PcRepository extends JpaRepository<PlaybackController, Integer>,JpaSpecificationExecutor<PlaybackController> {

	//查找指定id的播放控制器
	@Query(value="select * from pc where line_id = ?1 and station_id = ?2 and pc_id=?3",nativeQuery=true)
	PlaybackController findByLine_idAndStation_idAndPc_id(int id2, int id3, int id4);

	PlaybackController findById(int id);
	
}
