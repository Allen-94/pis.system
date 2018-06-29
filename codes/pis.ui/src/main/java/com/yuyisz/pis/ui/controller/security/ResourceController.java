package com.yuyisz.pis.ui.controller.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.yuyisz.pis.ui.module.performance.CollectionTask;
import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.service.performance.CollectionTaskService;
import com.yuyisz.pis.ui.service.performance.CollectionTemplateService;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.service.security.UserGroupService;
import com.yuyisz.pis.ui.vo.ResourceTreeVO;

/**
 * 资源操作接口，
 * 应该不需要操作界面，所以可以只提供restful接口，只提供数据操作和查询
 * @author mengz
 *
 */
@RestController
@RequestMapping("/security")
public class ResourceController {
	
	@Autowired
	private DevResourceService resourceService;
	
	@Autowired
	private CollectionTaskService collectionTaskService;
	@Autowired
	private CollectionTemplateService collectionTemplateService;
	@Autowired
	private UserGroupService usergroupService;
	
	
	/**
	 * 根据
	 * @param taskId
	 * @param groupId
	 * @return
	 */
	@RequiresPermissions("查询所有资源列表")
	@SuppressWarnings("null")
	@RequestMapping("/findAllResourcesByTaskId")
	@ResponseBody
	public List<ResourceTreeVO> findAllReources(@RequestParam(name="taskId",required=false,defaultValue="-1") long taskId){
		List<Integer> ids = new ArrayList<>();
		Set<DevResources> checkResource = new HashSet<>();
		int deviceType = 0;
		if(taskId!=-1) {
			CollectionTask task = collectionTaskService.findById(taskId);
			if(task!=null) {
				checkResource = task.getDevices();
				deviceType = collectionTemplateService.findById(task.getTemplateId()).getDeviceType();
			}
		}
		
		 List<DevResources> resoureces = resourceService.findAllResources();
		 List<ResourceTreeVO> result = new ArrayList<ResourceTreeVO>();
		 ResourceTreeVO root = new ResourceTreeVO();	//设置根节点
		 root.setId(0);
		 root.setName("所有线路");
		 root.setValue(null);
		 root.setPid(0);
		 root.setOpen(true);
		 result.add(root);
		 
		 for(DevResources resource:resoureces) {
			 ResourceTreeVO vo = new ResourceTreeVO();
			 vo.setId(resource.getId());
			 vo.setName(resource.getResourceName());
			 vo.setValue(resource);
			 int pid = getParentId(resoureces,resource);
			 vo.setPid(pid);
			 if(pid==0)
				 vo.setOpen(true);
			 for(int id:ids) {
				 if(id==resource.getId()) {
					 vo.setChecked(true);
					 break;
				 }
			 }
			 for(DevResources res : checkResource) {
				 if(res.getId() == resource.getId()) {
					 vo.setChecked(true);
					 break;
				 }
			 }
			 result.add(vo);
		 }
		 return result;
	}
	
	//安全管理用戶組模塊調用
	@RequiresPermissions("查询所有资源列表")
	@RequestMapping("/findAllResourcesByGroupId")
	@ResponseBody
	public List<ResourceTreeVO> findAllResourcesByGroupId(@RequestParam(name="groupId",required=false,defaultValue="-1")int groupId){
		System.out.println("---->>findAllResourcesByGroupId");
		List<Integer> gid = new ArrayList<Integer>();//资源id
		if(groupId!=-1) {
			List<DevResources> resources =resourceService.findResourceOfGroup(groupId);
			for (DevResources devResource : resources) {
				gid.add(devResource.getId());
			}
		}
		
		 List<DevResources> resoureces = resourceService.findAllResources();
		 List<ResourceTreeVO> result = new ArrayList<ResourceTreeVO>();
		 for(DevResources resource:resoureces) {
			 ResourceTreeVO vo = new ResourceTreeVO();
			 vo.setId(resource.getId());
			 vo.setName(resource.getResourceName());
			 vo.setValue(resource);
			 int pid = getParentId(resoureces,resource);
			 vo.setPid(pid);
			 if(pid==0)
				 vo.setOpen(true);
			 if(gid.contains(resource.getId())) {//判断是否是group的管理域,checked
				 vo.setChecked(true);
				 vo.setOpen(true);
			 }
			 
			 result.add(vo);
		 }
		 return result;
	}
	
	@RequestMapping("/findAllPlayers")
	@ResponseBody
	public List<ResourceTreeVO> findAllPlayers(){
		 List<DevResources> resoureces = resourceService.findAllResources();
		 List<ResourceTreeVO> result = new ArrayList<ResourceTreeVO>();
		 for(DevResources resource:resoureces) {
			 if(resource.getId5() == -1) {//去掉不是播控的节点
				 ResourceTreeVO vo = new ResourceTreeVO();
				 vo.setId(resource.getId());
				 vo.setName(resource.getResourceName());
				 vo.setValue(resource);
				 int pid = getParentId(resoureces,resource);
				 vo.setPid(pid);
				 if(pid==0)
					 vo.setOpen(true);
				 result.add(vo);
			 }
		 }
		 return result;
	}
	
	//查找全部设备
	@RequiresPermissions("通过resourceType查询符合条件的资源")
	@RequestMapping("findAllResource")
	@ResponseBody
	public List<DevResources> findAllResource(){
		return resourceService.findAllResources();
	}
	//查找某种类型的设备
	@RequiresPermissions("通过resourceType查询符合条件的资源")
	@RequestMapping("findResourceByType")
	@ResponseBody
	public List<DevResources> findResourceByType(@RequestParam("type")int type){
		return resourceService.findByType(type);
	}
	//查找线路下的站点
	@RequiresPermissions("通过resourceType查询符合条件的资源")
	@RequestMapping("findResourceBylinkageType")
	@ResponseBody
	public List<DevResources> findResourceBylinkageType(@RequestBody Map<String,Object> reqMap){
		int type = Integer.parseInt(reqMap.get("type").toString());
		int lineId = Integer.parseInt(reqMap.get("lineId").toString());
		DevResources line = resourceService.findById(lineId);
		//通过id2 type 获取list
		return resourceService.findBylinkageType(type,line.getId2());
	}
	//查找站点下的设备
	@RequiresPermissions("通过resourceType查询符合条件的资源")
	@RequestMapping("findResourceBystationType")
	@ResponseBody
	public List<DevResources> findResourceBystationType(@RequestBody Map<String,Object> reqMap){
		int type = Integer.parseInt(reqMap.get("type").toString());
		int stationId = Integer.parseInt(reqMap.get("stationId").toString());
		DevResources station = resourceService.findById(stationId);
		//通过id2,id3 type 获取车站下的设备list
		return resourceService.findResourceBystationType(type,station.getId2(),station.getId3());
	}
	
	private int getParentId(final List<DevResources> resoureces,final DevResources re) {
		int id1 = 0,id2 = 0,id3 = 0,id4 = 0,id5 = 0,ptype=0;
		int resourceType = re.getResourceType();
		if(resourceType==5) {
			ptype = 4;
		}else if(resourceType==7) {
			ptype=6;
		}
		List<DevResources> temp = resoureces;;
		int pid=0;
		if(re.getId5() != -1) {
			id5 = -1;
			id4 = re.getId4();
			id3 = re.getId3();
			id2 = re.getId2();
			id1 = re.getId1();
		}else if(re.getId4()!=-1) {
			id5 = re.getId5();
			id4 = -1;
			id3 = re.getId3();
			id2 = re.getId2();
			id1 = re.getId1();
		}else if(re.getId3()!=-1){
			id5 = re.getId5();
			id4 = re.getId4();
			id3 = -1;
			id2 = re.getId2();
			id1 = re.getId1();
		}else if(re.getId2()!=-1){
			id5 = re.getId5();
			id4 = re.getId4();
			id3 = re.getId3();
			id2 = -1;
			id1 = re.getId1();
		}
		for(DevResources resource:resoureces) {
			if(resource.getId1()==id1
					&&resource.getId2()==id2
					&&resource.getId3()==id3
					&&resource.getId4()==id4
					&&resource.getId5()==id5) {
				if(ptype!=0) {
					//如果父节点ids都一样，就判断父节点类型是播控还是服务器
					if(resource.getResourceType()==ptype) {
						pid=resource.getId();
					}else {
						continue;
					}
				}else {
					pid=resource.getId();
				}
				break;
			}
		}
		return pid;
	}
}
