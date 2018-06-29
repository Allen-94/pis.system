package com.yuyisz.pis.ui.service.approveflow;

import java.util.List;

import org.springframework.data.domain.Page;

import com.yuyisz.pis.ui.module.approveflow.ApproveFlow;
import com.yuyisz.pis.ui.vo.FlowProcessData;
import com.yuyisz.pis.ui.vo.PageParam;

public interface ApproveFlowService {
	/**
	 * 分页查找
	 * @param param
	 * @return
	 */
	public Page<ApproveFlow> findAll(PageParam param) ;

	public ApproveFlow createApproveFlow(ApproveFlow param);

	public ApproveFlow findFlowById(String id);

	public Page<ApproveFlow> findMyFlow(int userId,PageParam param);

	/**
	 * 我的待办任务
	 * @param userId
	 * @return
	 */
	public List<ApproveFlow> findMyApprove(int userId);

	public Page<ApproveFlow> findMyApprovedFlow(int userId,PageParam param);

	public boolean process(int currentUserId, FlowProcessData param);
}
