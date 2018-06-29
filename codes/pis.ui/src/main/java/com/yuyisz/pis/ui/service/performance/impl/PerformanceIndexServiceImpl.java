package com.yuyisz.pis.ui.service.performance.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.performance.PerformanceIndexRepository;
import com.yuyisz.pis.ui.module.performance.PerformanceIndex;
import com.yuyisz.pis.ui.service.performance.PerformanceIndexService;
/**
 * 性能指标的操作服务
 * @author mengz
 *
 */
@Service
public class PerformanceIndexServiceImpl implements PerformanceIndexService {

	@Autowired
	private PerformanceIndexRepository performanceIndexRepository;
	
	@Override
	public List<PerformanceIndex> findAllPerformanceIndex() {
		return performanceIndexRepository.findAll();
	}

	@Override
	public List<PerformanceIndex> findByIds(List<Long> indexIdList) {
		return performanceIndexRepository.findAll(indexIdList);
	}
}
