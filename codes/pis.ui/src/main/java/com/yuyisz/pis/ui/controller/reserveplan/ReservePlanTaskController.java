package com.yuyisz.pis.ui.controller.reserveplan;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuyisz.pis.ui.config.WebSocketServer;
import com.yuyisz.pis.ui.module.reserveplan.ReserveCmd;
import com.yuyisz.pis.ui.module.reserveplan.ReservePlan;
import com.yuyisz.pis.ui.module.reserveplan.ReserverPlanTask;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.reserveplan.ReservePlanService;
import com.yuyisz.pis.ui.service.reserveplan.ReserverPlanTaskService;

@Controller
@RequestMapping("/reserveplan")
public class ReservePlanTaskController {

	@Autowired
	private ReserverPlanTaskService reserverPlanTaskService;
	
	@Autowired
	private ReservePlanService reservePlanService;
	
	@GetMapping("/findAllPlanTaskByPlanId/{planId}")
	@ResponseBody
	public List<ReserverPlanTask> findAllPlanTaskByPlanId(@PathVariable("planId")int planId){
		return reserverPlanTaskService.findAllPlanTaskByPlanId(planId);
	}
	/**
	 * 启动预案
	 * @param planId：预案ID
	 */
	@PostMapping("/planTaskRun/{planId}")
	@ResponseBody
	public Map<String, Object> planTaskRun(@PathVariable("planId")int planId) {
		
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			//删除已有的预案启动任务
			reserverPlanTaskService.delByPlanId(planId);
			
			//创建新的预案启动任务
			ReservePlan plan = reservePlanService.findById(planId);
			Set<DevResources> players = plan.getPlayers();
			Set<ReserveCmd> cmds = plan.getCmds();
			for(DevResources player:players) {
				for(ReserveCmd cmd:cmds) {
					ReserverPlanTask task = new ReserverPlanTask();
					task.setId(UUID.randomUUID().toString());
					task.setActionName(cmd.getAction().getActionName());
					task.setActionContent(cmd.getAction().getActionContent());
					task.setContent(cmd.getContent());
					task.setStep(cmd.getStep());
					task.setPlayer(player);
					task.setReservePlanId(plan.getId());
					task.setCreateDate(new Date());
					task.setState(0);
					reserverPlanTaskService.savePlanTask(task);
				}
			}
			flag = true;
			message = "启动预案成功！";
		}catch(Exception e) {
			message = "启动预案失败！\n"+e.getMessage();
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
}
