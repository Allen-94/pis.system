package com.yuyisz.pis.ui.dao.performance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.performance.PerformanceIndex;
@Repository
public interface PerformanceIndexRepository extends JpaRepository<PerformanceIndex, Long>,JpaSpecificationExecutor<PerformanceIndex> {

}
