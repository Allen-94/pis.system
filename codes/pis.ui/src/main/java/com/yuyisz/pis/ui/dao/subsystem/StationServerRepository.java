package com.yuyisz.pis.ui.dao.subsystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.subsystem.StationServer;
/**
 * 车站服务器dao 
 * @author 郭芝辰
 *
 */
@Repository
public interface StationServerRepository extends JpaSpecificationExecutor<StationServer>, JpaRepository<StationServer, Integer> {
	@Query(value="select * from stationserver where line_id = ?1 and station_id = ?2 and station_server_id = ?3",nativeQuery=true)
	public StationServer findByLine_idAndStation_idAndStation_server_id(int line_id,int station_id,int station_server_id);

	public StationServer findById(int id);
}
