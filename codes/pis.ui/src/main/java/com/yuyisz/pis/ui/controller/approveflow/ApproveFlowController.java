package com.yuyisz.pis.ui.controller.approveflow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
/***
 * 流程审批模块访问接口
 * @author mengz
 *
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuyisz.pis.ui.module.approveflow.ApproveFlow;
import com.yuyisz.pis.ui.module.approveflow.FlowProcess;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.approveflow.ApproveFlowService;
import com.yuyisz.pis.ui.vo.FlowProcessData;
import com.yuyisz.pis.ui.vo.PageParam;
@Controller
@RequestMapping("/approveflow")
public class ApproveFlowController {

	@Autowired
	private ApproveFlowService approveFlowService;
	private int currentUserId = 1;
	@RequestMapping("/findaspage")
	@ResponseBody
	public Page<ApproveFlow> findApproveFlowPage(@RequestBody(required=false) PageParam param){
		return approveFlowService.findAll(param);
	}
	
	@RequestMapping(value="/create",produces = "application/json; charset=UTF-8")
	@ResponseBody
	public ApproveFlow create(@RequestBody ApproveFlow param) {
		ApproveFlow result = approveFlowService.createApproveFlow(param);
		return result;
	}
	
	@RequestMapping(value="/process",produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String,Boolean> process(@RequestBody FlowProcessData param) {
		boolean result = approveFlowService.process(currentUserId,param);
		Map<String,Boolean> temp = new HashMap<String,Boolean>();
		temp.put("result", result);
		return temp;
	}
	/**
	 * 
	 * @param id: 流程id
	 * @param type ：是否是   查看/编辑/审批 :0:1:2
	 * @param model
	 * @return
	 */
	@RequestMapping("/flowinfo/{id}")
	public String showFlowInfo(@PathVariable(value="id")String id,@RequestParam("type")int type,Model model) {
		if(id!=null) {
			ApproveFlow flow = approveFlowService.findFlowById(id);
			model.addAttribute("flow", flow);
			User currentApprover = flow.getCurrentApprover();
			if(currentApprover!=null) {
				for(FlowProcess fp:flow.getFlowProcessSet()) {
					if(fp.getProcesser().getUserId() == currentApprover.getUserId()) {
						model.addAttribute("process", fp);
					}
				}
			}
		}
		
		model.addAttribute("type", type);
		return "/approveflow/flowinfo";
	}
	/**
	 * 根据id查找流程单详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/findflow/{id}")
	@ResponseBody
	public ApproveFlow findflow(@PathVariable(value="id") String id) {
		return approveFlowService.findFlowById(id);
	}
	
	/**
	 * 查看我的任务 ，即我提交的审批 ，分页
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/findmyflow",produces = "application/json; charset=UTF-8")
	@ResponseBody
	public Map<String,Object> findmyflow(@RequestBody PageParam param) {
		/*PageParam param = new PageParam();
		param.setPage((int) map.get("start"));
		param.setPageSize((int) map.get("length"));*/
		currentUserId=4;
		Page<ApproveFlow> pageResult =  approveFlowService.findMyFlow(currentUserId,param);
		Map<String,Object> result = new HashMap<>();
		result.put("draw", param.getDraw());
		result.put("total", pageResult.getTotalElements());
		result.put("pageData", pageResult.getContent());
		return result;
	}
	
	/**
	 * 查找我当前待办任务 ，即需要我审批的流程单
	 * @param param
	 * @return
	 */
	@RequestMapping("/findmyapprove")
	@ResponseBody
	public List<ApproveFlow> findmyapprove() {
		currentUserId=2;
		return approveFlowService.findMyApprove(currentUserId);
	}
	
	/**
	 * 查找我审批过的流程单
	 * @param param
	 * @return
	 */
	@RequestMapping("/findmyapprovedflow")
	@ResponseBody
	public Map<String,Object> findmyapprovedflow(@RequestBody(required=false) PageParam param) {
		currentUserId=1;
		Page<ApproveFlow> pageResult =  approveFlowService.findMyApprovedFlow(currentUserId,param);
		Map<String,Object> result = new HashMap<>();
		result.put("draw", param.getDraw());
		result.put("total", pageResult.getTotalElements());
		result.put("pageData", pageResult.getContent());
		return result;
	}
}
