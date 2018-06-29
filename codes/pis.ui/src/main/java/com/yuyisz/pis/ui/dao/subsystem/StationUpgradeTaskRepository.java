package com.yuyisz.pis.ui.dao.subsystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.subsystem.StationUpgradeTask;
/**
 * 车站设备升级任务dao
 * @author 郭芝辰
 *
 */
@Repository
public interface StationUpgradeTaskRepository extends JpaRepository<StationUpgradeTask, Integer>, JpaSpecificationExecutor<StationUpgradeTask> {

}
