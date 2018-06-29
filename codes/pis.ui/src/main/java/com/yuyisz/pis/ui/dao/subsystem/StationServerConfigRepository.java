package com.yuyisz.pis.ui.dao.subsystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.subsystem.StationServer;
import com.yuyisz.pis.ui.module.subsystem.StationServerConfig;
/**
 * 车站服务器配置信息dao 
 * @author 郭芝辰
 *
 */
@Repository
public interface StationServerConfigRepository extends JpaRepository<StationServerConfig, Integer>, JpaSpecificationExecutor<StationServerConfig> {
	StationServerConfig findByStationServer(StationServer stationserver);
}
