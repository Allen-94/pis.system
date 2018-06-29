package com.yuyisz.pis.ui.dao.subsystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.subsystem.PcConfig;
import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
/**
 * 播放控制器配置表的dao
 * @author 郭芝辰
 *
 */
@Repository
public interface PcConfigRepository extends JpaRepository<PcConfig, Integer>, JpaSpecificationExecutor<PcConfig> {
	PcConfig findByPc(PlaybackController pc);
}
