package com.yuyisz.pis.ui.dao.alarm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.alarm.AlarmForwardConfig;
@Repository
public interface AlarmForwardConfigRepository extends JpaRepository<AlarmForwardConfig, Long> {

}
