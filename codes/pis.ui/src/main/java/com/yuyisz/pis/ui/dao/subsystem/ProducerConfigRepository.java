package com.yuyisz.pis.ui.dao.subsystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.subsystem.ProducerConfig;
/**
 * 厂家配置信息dao
 * @author 郭芝辰
 *
 */
@Repository
public interface ProducerConfigRepository extends JpaSpecificationExecutor<ProducerConfig>, JpaRepository<ProducerConfig, Integer> {

	ProducerConfig findByModel(int pc_model);
	
}
