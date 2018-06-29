package com.yuyisz.pis.ui.service.performance;

import java.util.List;
import com.yuyisz.pis.ui.module.performance.PerformanceIndex;

public interface PerformanceIndexService {

	public List<PerformanceIndex> findAllPerformanceIndex();

	public List<PerformanceIndex> findByIds(List<Long> indexIdList);
}
