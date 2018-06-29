package com.yuyisz.pis.ui.service.approveflow.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuyisz.pis.ui.dao.approveflow.ApproveFlowRepository;
import com.yuyisz.pis.ui.dao.approveflow.FlowProcessRepository;
import com.yuyisz.pis.ui.dao.security.UserRepository;
import com.yuyisz.pis.ui.module.approveflow.ApproveFlow;
import com.yuyisz.pis.ui.module.approveflow.FlowProcess;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.approveflow.ApproveFlowService;
import com.yuyisz.pis.ui.vo.FlowProcessData;
import com.yuyisz.pis.ui.vo.PageParam;
@Service
public class ApproveFlowServiceImpl implements ApproveFlowService {
	@Autowired
	private ApproveFlowRepository approveFlowRepository;
	@Autowired
	private FlowProcessRepository flowProcessRepository;
	@Autowired 
	private UserRepository userRepository;
	
	public Page<ApproveFlow> findAll(PageParam param) {
		if(param == null) {
			param = new PageParam();
		}
		param.setPage(1);
		param.setPageSize(15);
		param.setDirection(Direction.DESC.name());
		param.setProperties("id");
		Page<ApproveFlow>  result = approveFlowRepository.findAll(param.buildRequest());
		return result;
	}

	@Override
	public ApproveFlow createApproveFlow(ApproveFlow param) {
		ApproveFlow result = approveFlowRepository.saveAndFlush(param);
		return result;
	}

	@Override
	public ApproveFlow findFlowById(String id) {
		return approveFlowRepository.findOne(id);
	}

	@Override
	public Page<ApproveFlow> findMyFlow(int userId,PageParam param) {
		Pageable pageRequest = param.buildRequest();
		User user = userRepository.findOne(userId);
		Page<ApproveFlow> result = approveFlowRepository.findBySubmitter(user,pageRequest);
		return result;
	}

	@Override
	public List<ApproveFlow> findMyApprove(int userId) {
		User user = userRepository.findOne(userId);
		List<ApproveFlow> result = approveFlowRepository.findByCurrentApprover(user);
		return result;
	}

	@Override
	public Page<ApproveFlow> findMyApprovedFlow(int userId,PageParam param) {
		User user = userRepository.findOne(userId);
		Page<ApproveFlow> result = approveFlowRepository.findMyApprovedFlow(user,param.buildRequest());
		return result;
	}

	/**
	 * 0:1:2:3; 未处理：通过，返回修改，终止
	 * 0:1 :处理中，处理完成
	 */
	@Override
	@Transactional
	public boolean process(int currentUserId, FlowProcessData param) {
		try {
		//User user = userRepository.findOne(currentUserId);
		FlowProcess flowProcess = flowProcessRepository.findOne(param.getFlowProcessId());
		flowProcess.setProcessResult(param.getProcessResult());
		flowProcess.setProcessOpinionr(param.getProcessOpinionr());
		flowProcess.setProcessDate(new Date());
		flowProcessRepository.saveAndFlush(flowProcess);
		ApproveFlow approveFlow = flowProcess.getApproveFlow();
		approveFlow.setApproveState(param.getProcessResult());
		if(param.getProcessResult()==1) {
			approveFlow.setApproveState(1);
		}
		User nextApprover = approveFlow.getNextApprover();
		approveFlow.setCurrentApprover(nextApprover);
		if(nextApprover == null) {
			//没有下一个处理人，表示处理流程完成
			approveFlow.setApproveState(1);
		}else {
			approveFlow.setApproveState(0);
		}
		approveFlowRepository.saveAndFlush(approveFlow);
		}catch(Exception e) {
			return false;
		}
		return true;
	}
}
