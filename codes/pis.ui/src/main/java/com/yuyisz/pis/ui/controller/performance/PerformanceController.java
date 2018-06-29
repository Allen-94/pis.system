package com.yuyisz.pis.ui.controller.performance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.yuyisz.pis.ui.module.performance.CollectionInfo;
import com.yuyisz.pis.ui.module.performance.CollectionItem;
import com.yuyisz.pis.ui.module.performance.CollectionTask;
import com.yuyisz.pis.ui.module.performance.CollectionTemplate;
import com.yuyisz.pis.ui.module.performance.PerformanceIndex;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.performance.CollectionInfoService;
import com.yuyisz.pis.ui.service.performance.CollectionItemService;
import com.yuyisz.pis.ui.service.performance.CollectionTaskService;
import com.yuyisz.pis.ui.service.performance.CollectionTemplateService;
import com.yuyisz.pis.ui.service.performance.PerformanceIndexService;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.vo.CollectionTaskVO;
import com.yuyisz.pis.ui.vo.CollectionTemplateVO;

@Controller
@RequestMapping("/performance")
public class PerformanceController {
	@Autowired
	private CollectionInfoService collectionInfoService;
	@Autowired
	private CollectionTaskService collectionTaskService;
	@Autowired
	private CollectionInfoService monitorInfoService;
	@Autowired
	private CollectionTemplateService collectionTemplateService;
	@Autowired
	private PerformanceIndexService performanceIndexService;
	@Autowired
	private CollectionItemService collectionItemService;
	@Autowired
	private DevResourceService devResourceService;
	/**
	 * 查找所有采集模板
	 * @return
	 */
	@RequestMapping(value="findAllCollectionTemplates")
	@ResponseBody
	public List<CollectionTemplate> findAllCollectionTemplates(){
		return collectionTemplateService.findAllCollectionTemplates();
	}
	
	/**
	 * 跳转到新增/修改性能采集模板的对话框
	 * @return
	 */
	@GetMapping("addTemplate/{id}")
	public String showAddCollectionTemplateDiaglog(@PathVariable(value="id",required=false) long templateId,Model model) {
		if(templateId!=0) {
			CollectionTemplate template = collectionTemplateService.findById(templateId);
			model.addAttribute("template", template);
		}else {
			model.addAttribute("template", new CollectionTemplate());
		}
		return "/performance/collectionTemplateDialog";	
	}
	/**
	 * 获取所有性能采集指标
	 * @return
	 */
	@GetMapping("findAllIndex")
	@ResponseBody
	public List<PerformanceIndex> findAllIndex(){
		return performanceIndexService.findAllPerformanceIndex();
	}
	
	/**
	 * 性能采集模板新增/修改
	 * @param vo
	 * @return
	 */
	@PostMapping("addCollectionInfo")
	@ResponseBody
	public Map<String,Object> addCollectionInfo(@RequestBody CollectionTemplateVO vo){
		Map<String, Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		try {
			CollectionTemplate template = null;
			Set<CollectionItem> items = null;
			if(vo.getId()!= 0) {
				template = collectionTemplateService.findById(vo.getId());
				items = template.getItems();
			}else {
				template = new CollectionTemplate();
			}
			if(items == null) {
				items = new HashSet<>();
			}
			template.setTemplateName(vo.getTemplateName());
			template.setDescription(vo.getDescription());
			template.setDeviceType(vo.getDeviceType());
			String indexIds = vo.getIndexIds();
			List<Long> indexIdList = new ArrayList<>();
			String[] ids = indexIds.split(",");
			for(String id:ids) {
				indexIdList.add(Long.valueOf(id));
			}
			List<PerformanceIndex> indexs =performanceIndexService.findByIds(indexIdList);
			Set<CollectionItem> addItem = new HashSet<>();
			for(PerformanceIndex index:indexs) {
				boolean itemFlag = false;
				for(CollectionItem item:items) {
					if(item.getItemCode().equals(index.getItemCode())) {
						addItem.add(item);
						itemFlag = true;
						break;
					}
				}
				if(!itemFlag) {
					CollectionItem item = new CollectionItem();
					item.setItemName(index.getItemName());
					item.setItemName(index.getItemName());
					item.setItemCode(index.getItemCode());
					item.setDescription(index.getDescription());
					addItem.add(item);
				}
			}
			
			template.setItems(addItem);
			if(template != null) {
				collectionTemplateService.save(template);
				message = "保存成功";
				flag = true;
			}
		}catch(Exception e) {
			message = "保存失败！\n"+e.getMessage();
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
	@GetMapping("showCollectionTemplateInfo/{id}")
	public String showCollectionTemplateInfo(@PathVariable("id") long templateId,Model model) {
		CollectionTemplate template = collectionTemplateService.findById(templateId);
		model.addAttribute("template", template);
		return "/performance/collectionTemplateInfoDialog";
	}
	
	@PostMapping("delTemplate/{id}")
	@ResponseBody
	public boolean delTemplate(@PathVariable("id")long templateId) {
		return collectionTemplateService.delById(templateId);
	}
	
	@GetMapping("setThreshold/{id}")
	public String setThreshold(@PathVariable("id")long templateId,Model model) {
		CollectionTemplate template = collectionTemplateService.findById(templateId);
		model.addAttribute("template", template);
		return "/performance/thresholdSet";
	}
	
	@PostMapping("updateItem")
	@ResponseBody
	public Map<String,Object> updateItem(@RequestBody List<CollectionItem> itemsParam){
		Map<String, Object> result = new HashMap<>();
		boolean flag = false;
		String message="";
		//查询收到的采集项，修改后重新保存
		try {
			List<Long> ids = new ArrayList<>();
			for(CollectionItem item : itemsParam) {
				ids.add(item.getId());
			}
			Set<CollectionItem> items = collectionItemService.findByIds(ids);
			for(CollectionItem item : items) {
				for(CollectionItem p : itemsParam) {
					if(p.getId() == item.getId()) {
						item.setLowerAlarm(p.isLowerAlarm());
						item.setLowerThreshold(p.getLowerThreshold());
						item.setUpperAlarm(p.isUpperAlarm());
						item.setUpperThreshold(p.getUpperThreshold());
						break;
					}
				}
			}
			collectionItemService.updateBatch(items);
			flag = true;
			message = "成功了";
		}catch(Exception e) {
			message = "失败";
		}
		result.put("flag", flag);
		result.put("message", message);
		return result;
	}
	
	
	
	@GetMapping(value="findNewestCollectionInfo",produces="application/json")
	@ResponseBody
	public List<CollectionInfo> findNewestCollectionInfo(@RequestParam("devType")int devType,
			@RequestParam("devId")int devId){
		List<CollectionInfo> infos = new ArrayList<>();
		List<Integer> devs = new ArrayList<>();
		if(devType == 0) {
			//查找全部类型的设备性能统计最新数据
			List<Integer> types = new ArrayList<>();
			types.add(4);
			types.add(5);
			types.add(6);
			types.add(7);
			List<DevResources> result = devResourceService.findByTypes(types);
			for(DevResources dev:result) {
				devs.add(dev.getId());
			}
		}else {
			//考虑设备ID
			if(devId == 0) {
				//查找具体类型的全部设备性能信息
				List<DevResources> result = devResourceService.findByType(devType);
				for(DevResources dev:result) {
					devs.add(dev.getId());
				}
			}else {
				devs.add(devId);
			}
		}
		//根据要统计的设备，查找所有设备的最新的性能数据
		infos = collectionInfoService.findNewestCollectionInfo(devs);
		return infos;
	}
	
	@RequestMapping(value="historydata/{devId}")
	public String historydata(Model model,@PathVariable(value = "devId") Integer devId) {
		model.addAttribute("devId", devId);
		return "/performance/preformanceHistoryData";
	}
	
	
	@RequestMapping(value="realtimedata/{devId}")
	public String realtimedata(Model model,@PathVariable(value = "devId") Integer devId) {
		model.addAttribute("devId", devId);
		return "/performance/performanceRealtimeData";
	}
	
	
	@RequestMapping(value="/collectTaskInfo")
	public String collectTaskInfo(Model model,@RequestParam(value="id",required=false,defaultValue="-1") long id) {
		CollectionTask task = null;
		if(id!=-1) {
			 task = collectionTaskService.findById(id);
		}else {
			 task = new CollectionTask();
		}
		
		model.addAttribute("taskinfo", task);
		model.addAttribute("templates", collectionTemplateService.findAllCollectionTemplates());
		return "/performance/collectTaskInfo";
	}
	
	/**
	 * 查询所有采集任务
	 * @return
	 */
	@RequestMapping("/findAllCollectTasks")
	@ResponseBody
	public List<CollectionTask> findAllCollectTasks() {
		return collectionTaskService.findAllTask();
	}
	
	/**
	 * 保存提交的采集任务，
	 * @return
	 */
	@RequestMapping(value="/saveCollectTaskInfo",produces="application/json;charset=UTF-8")
	@ResponseBody
	public boolean saveCollectTaskInfo(@RequestBody CollectionTaskVO vo) {
		CollectionTask task = null;
		Set<DevResources> devices = new HashSet<>();
		Set<CollectionItem> items = new HashSet<>();
		String devIds = vo.getDevices();
		String[] dIds = devIds.split(",");
		List<Integer> dIdsList = new ArrayList<>();
		for(String id:dIds) {
			dIdsList.add(Integer.valueOf(id));
		}
		devices.addAll(devResourceService.findPlayByIds(dIdsList));
		
		String itemIdstr = vo.getItems();
		String[] iIds = itemIdstr.split(",");
		List<Long> itemIds = new ArrayList<>();
		for(String id:iIds) {
			itemIds.add(Long.valueOf(id));
		}
		items.addAll(collectionItemService.findByIds(itemIds));
		
		if(vo.getId() != 0 ) {
			task = collectionTaskService.findById(vo.getId());
			task.setTaskName(vo.getTaskName());
			task.setCollectDelay(vo.getCollectDelay());
			task.setCollectEndTime(vo.getCollectEndTime());
			task.setCollectStartTime(vo.getCollectStartTime());
			task.setEffectiveEndDate(vo.getEffectiveEndDate());
			task.setEffectiveStartDate(vo.getEffectiveStartDate());
			task.setTemplateId(vo.getTemplateId());
			task.setItems(items);
			task.setStatus(vo.getStatus());
			task.setUpdateDate(new Date());
			task.setUpdateUser((User) SecurityUtils.getSubject().getPrincipal());
		}else {
			task = new CollectionTask();
			task.setTaskName(vo.getTaskName());
			task.setCollectDelay(vo.getCollectDelay());
			task.setCollectEndTime(vo.getCollectEndTime());
			task.setCollectStartTime(vo.getCollectStartTime());
			task.setEffectiveEndDate(vo.getEffectiveEndDate());
			task.setEffectiveStartDate(vo.getEffectiveStartDate());
			task.setTemplateId(vo.getTemplateId());
			task.setItems(items);
			task.setStatus(vo.getStatus());
			task.setCreateDate(new Date());
			task.setCreaterUser((User) SecurityUtils.getSubject().getPrincipal());
			task.setUpdateDate(new Date());
			task.setUpdateUser((User) SecurityUtils.getSubject().getPrincipal());
		}
		boolean result = false;
		if(task != null) {
			for(DevResources dev:devices) {
				dev.setCollectionTask(task);
			}
			task.setDevices(devices);
			CollectionTask resultTask = collectionTaskService.saveTaskInfo(task);
			if(resultTask!=null) {
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 查询设备对应的当前性能数据
	 * @param devType：设备类型
	 * @param ids：设备id
	 * @return
	 */
	@RequestMapping("/findCurrentMonitorInfos")
	@ResponseBody
	public List<CollectionInfo> findCurrentMonitorInfos(@RequestParam(value="devType",required=false)String devType,
			@RequestParam(value="ids",required=false)String ids) {
		List<Integer> devIds = new ArrayList<>();
		devIds.add(1);
		devIds.add(2);
		return monitorInfoService.findCurrentMonitorInfos(devIds);
		//return null;//monitorInfoService.findCurrentMonitorInfos(devType, ids);
	}
	
	/**
	 * 根据设备ID查询设备的历史性能数据
	 * @param devId
	 * @return
	 */
	@RequestMapping("/findHistoryMonitorInfoById")
	@ResponseBody
	public List<CollectionInfo> findHistoryMonitorInfoById(@RequestParam("devId")long devId,
			@RequestParam("dateType") int dateType) {
		//monitorInfoService.findCurrentMonitorInfos(devType, ids);
		Calendar cal = Calendar.getInstance();
		switch(dateType) {
		case 0://一天
			cal.add(Calendar.DAY_OF_MONTH, -1);
			break;
		case 1://一周
			cal.add(Calendar.WEEK_OF_YEAR, -1);
			break;
		case 2://一月
			cal.add(Calendar.MONTH, -1);
			break;
		case 3://三月
			cal.add(Calendar.MONTH, -3);
			break;
		}
		System.out.println(cal.getTime());
		List<CollectionInfo> result = monitorInfoService.findHistoryMonitorInfoByIds((int) devId, cal.getTime());
		return result;
	}
	
	/**
	 * 根据设备ID查询设备的当前性能数据
	 * @param devId
	 * @return
	 */
	@RequestMapping("/findRealTimeMonitorInfoById")
	@ResponseBody
	public List<CollectionInfo> findRealTimeMonitorInfoById(@RequestParam("devId")long devId) {
		//monitorInfoService.findCurrentMonitorInfos(devType, ids);
		return null;
	}
	/**
	 * 删除选中的任务
	 * 像设备发送采集任务删除消息，停止采集任务
	 * 一个设备只有一个采集任务。
	 * 不可以同时对应多个采集任务。
	 * @param id 可表示多个，以逗号分割
	 * @return
	 */
	@RequestMapping("/deleteTaskInfo")
	public String deleteTaskInfo(@RequestParam(value="id",required=false) String id) {
		List<Long> ids = new ArrayList<Long>();
		if(id!=null&& !id.trim().equals("")) {
			String[] temp = id.split(",");
			for(String i : temp) {
				long t = Long.valueOf(i);
				ids.add(t);
			}
		}
		if(!ids.isEmpty()) {
			collectionTaskService.deleteTask(ids);
		}
		
		return "redirect:/performance/collectTask";
	}
	
	/**
	 * 启用性能采集任务，项涉及到的设备发送消息
	 * 设备通过解析采集任务，重新启动性能采集程序
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping("/startusingtask")
	public String startUsingTask(@RequestParam("id")long id,@RequestParam("status")int status) {
		collectionTaskService.startUsingTask(id,status);
		//发送消息给设备
		CollectionTask task = collectionTaskService.findById(id);
		Set<DevResources> devices = task.getDevices();
		for(DevResources dev:devices) {
			//遍历设备，启动性能采集任务，
			//采集任务发送采集消息到设备，设备返回性能数据到系统，保存性能数据
			collectionTaskService.startIndexCollectionTask(dev);
		}
		return "redirect:/performance/collectTask";
	}
}
