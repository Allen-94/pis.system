package com.yuyisz.pis.ui.dao.approveflow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.approveflow.FlowProcess;
@Repository
public interface FlowProcessRepository extends JpaRepository<FlowProcess, String>, PagingAndSortingRepository<FlowProcess, String> {

}
