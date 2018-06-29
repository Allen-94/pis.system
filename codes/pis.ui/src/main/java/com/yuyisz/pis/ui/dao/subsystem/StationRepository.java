package com.yuyisz.pis.ui.dao.subsystem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.subsystem.Station;
/**
 * 车站dao
 * @author 郭芝辰
 *
 */
@Repository
public interface StationRepository extends JpaRepository<Station, Integer>, JpaSpecificationExecutor<Station> {

	@Query(value="select * from station where line_id =?1",nativeQuery=true)
	List<Station> findByLine_id(Integer line_id);

	@Query(value="select * from station where line_id =?1 and station_id = ?2",nativeQuery=true)
	Station findByLine_idAndStation_id(int lineid, int stationid);
	
}
