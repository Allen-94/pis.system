package com.yuyisz.pis.ui.dao.subsystem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.subsystem.Line;
/*
 * 线路dao
 */
@Repository
public interface LineRepository extends JpaRepository<Line, Integer>, JpaSpecificationExecutor<Line> {

}
