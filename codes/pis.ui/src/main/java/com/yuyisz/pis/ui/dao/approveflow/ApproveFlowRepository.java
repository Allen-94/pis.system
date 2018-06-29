package com.yuyisz.pis.ui.dao.approveflow;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yuyisz.pis.ui.module.approveflow.ApproveFlow;
import com.yuyisz.pis.ui.module.security.User;
@Repository
public interface ApproveFlowRepository extends JpaRepository<ApproveFlow, String> ,PagingAndSortingRepository<ApproveFlow, String> {

	Page<ApproveFlow> findBySubmitter(User user, Pageable pageRequest);

	List<ApproveFlow> findByCurrentApprover(User user);
	
	@Query(value="SELECT t FROM ApproveFlow t WHERE t.id IN \r\n" + 
			"(SELECT approveFlow FROM FlowProcess WHERE processer=:user AND processResult>0)")
	Page<ApproveFlow> findMyApprovedFlow(@Param("user")User user,Pageable pageRequest);
}
