package com.yuyisz.pis.ui.controller.reserveplan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.reserveplan.ReserveCmd;
import com.yuyisz.pis.ui.module.reserveplan.ReservePlan;
import com.yuyisz.pis.ui.module.reserveplan.ReserverPlanTask;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.reserveplan.ReserveActionService;
import com.yuyisz.pis.ui.service.reserveplan.ReserveCmdService;
import com.yuyisz.pis.ui.service.reserveplan.ReservePlanService;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.vo.ReserveCmdVO;
import com.yuyisz.pis.ui.vo.ReservePlanVO;

@Controller
@RequestMapping("/reserveplan")
public class ReservePlanController {

	@Autowired
	private ReservePlanService reservePlanService;
	@Autowired
	private DevResourceService devResourceService;
	@Autowired
	private ReserveActionService reserveActionService;
	
	@GetMapping("toAddReservePlan")
	public String toAddReserveInfo(@RequestParam(value="planId",required=false,defaultValue="0")int planId,Model model) {
		ReservePlan plan = null;
		if(planId != 0) {
			plan = reservePlanService.findById(planId);
		}else {
			plan = new ReservePlan();
		}
		model.addAttribute("plan", plan);
		return "/reserveplan/addReservePlan";
	}
	
	@GetMapping("findAllPlan")
	@ResponseBody
	public List<ReservePlan> findAllPlan(){
		return reservePlanService.findAllPlan();
	}
	
	/**
	 * 新增/修改紧急预案
	 * @return
	 */
	@PostMapping("/addReservePlan")
	@ResponseBody
	public Map<String, Object> addReservePlan(@RequestBody ReservePlanVO vo){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			ReservePlan plan = null;
			if(vo.getId()!=0) {
				//修改
				plan = reservePlanService.findById(vo.getId());
			}else {
				//新增
				plan = new ReservePlan();
				plan.setCreateDate(new Date());
				plan.setCreater((User) SecurityUtils.getSubject().getPrincipal());
			}
			plan.setPlanName(vo.getPlanName());
			plan.setPlanDesc(vo.getPlanDesc());
			plan.setLevel(vo.getLevel());
			plan.setPriority(vo.getPriority());
			//获取播控
			String planerIds = vo.getPlayerIds();
			String[] ids = planerIds.split(",");
			Set<DevResources> players = new HashSet<>();
			List<Integer> lIds = new ArrayList<>();
			for(String id:ids) {
				int playId = Integer.valueOf(id);
				lIds.add(playId);
			}
			players.addAll(devResourceService.findPlayByIds(lIds));
			plan.setPlayers(players);
			//创建步骤
			List<ReserveCmdVO> cmdVos = vo.getCmds();
			Set<ReserveCmd> cmdSet = new HashSet<>();
			for(ReserveCmdVO cmdVo:cmdVos) {
				ReserveCmd cmd = new ReserveCmd();
				if(cmdVo.getId()!= -1) {
					cmd.setId(cmdVo.getId());
				}
				cmd.setStep(cmdVo.getStep());
				cmd.setContent(cmdVo.getContent());
				cmd.setAction(reserveActionService.findById(cmdVo.getActionId()));
				cmdSet.add(cmd);
			}
			plan.setCmds(cmdSet);
			Set<ReserverPlanTask> taskSet = new HashSet<>();
			for(DevResources dev:players) {
				Set<ReserverPlanTask> temp = new HashSet<>();
				for(ReserveCmd cmd:cmdSet) {
					ReserverPlanTask task = new ReserverPlanTask();
					task.setId(UUID.randomUUID().toString());
					task.setActionName(cmd.getAction().getActionName());
					task.setActionContent(cmd.getAction().getActionContent());
					task.setContent(cmd.getContent());
					task.setStep(cmd.getStep());
					task.setPlayer(dev);
					task.setReservePlanId(plan.getId());
					task.setCreateDate(new Date());
					task.setState(0);
					temp.add(task);
					taskSet.add(task);
				}
				dev.setReserveTask(temp);
			}
			plan.setTasks(taskSet);
			reservePlanService.savePlan(plan);
			flag=true;
			message = "保存成功";
		}catch(Exception e) {
			e.printStackTrace();
			message = "保存失败！";
		}
		
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	@PostMapping("/delReservePlan/{id}")
	@ResponseBody
	public Map<String, Object> delReservePlan(@PathVariable("id")int id){
		Map<String,Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			reservePlanService.delPlan(id);
			flag = true;
			message = "删除成功";
		}catch(Exception e) {
			message = "删除失败";
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
}
