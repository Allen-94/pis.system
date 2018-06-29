package com.yuyisz.pis.ui.controller.alarm;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.alarm.Alarm;
import com.yuyisz.pis.ui.module.alarm.AlarmForwardConfig;
import com.yuyisz.pis.ui.service.alarm.AlarmService;
import com.yuyisz.pis.ui.service.security.DevResourceService;
import com.yuyisz.pis.ui.service.security.UserService;
import com.yuyisz.pis.ui.vo.AlarmForwardConfigVO;
import com.yuyisz.pis.ui.vo.AlarmParam;
import com.yuyisz.pis.ui.vo.HistoryAlarmCondition;
import com.yuyisz.pis.ui.vo.RealTimeAlarmCondition;
/**
 * 告警相关
 * @author 42254
 *
 */
@Controller
@RequestMapping("/alarm")
public class AlarmController {
	private Logger logger =  LoggerFactory.getLogger(AlarmController.class);
	
	@Autowired
	private AlarmService alarmService;
	@Autowired
	private DevResourceService devResourceService;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/alarmForward/{id}/{type}")
	public String alarmForward(@PathVariable(name="id",required=false) long id,@PathVariable(name="type",required=false) int type ,Model model) {
		AlarmForwardConfig config = alarmService.findForwardConfigById(id,type);
		if(config==null)
			config = new AlarmForwardConfig();
		model.addAttribute("config", config);
		return "/alarm/alarmForwardByMail";
	}
	
	@RequestMapping("/deleteConfig/{id}/{type}")
	public String deleteConfig(@PathVariable(name="id",required=false) long id,@PathVariable(name="type",required=false) int type ,Model model) {
		alarmService.deleteConfig(id,type);
		return "/alarm/alarmFoward";
	}
	
	/**
	 * 根据条件查询历史告警，以告警时间排序
	 * @param condition
	 * @return
	 */
	@PostMapping(value="/findHistoryAlarms")
	@ResponseBody
	public List<Alarm> findHistoryAlarmes(@RequestBody HistoryAlarmCondition condition) {
		List<Alarm> alarms = alarmService.findAllHistoryAlarms(condition);
		return alarms;
	}
	
	@RequestMapping(value="/findRealtimeAlarms")
	@ResponseBody
	public List<Alarm> findRealtimeAlarms(@RequestBody RealTimeAlarmCondition condition){
		AlarmParam param = new AlarmParam();
		param.setGrade(condition.getGrade());
		param.setCleanStatu(condition.getClean());
		param.setConfirmStatu(condition.getConfirm());
		param.setDates(condition.getTimeType());
		param.setSourceId(condition.getSourceId());
		List<Alarm> alarm = alarmService.findAllRealtimeAlarms(param);
		return alarm;
	}
	
	@RequestMapping("/findForwardConfig")
	@ResponseBody
	public List<AlarmForwardConfig> findForwardConfig(){
		return alarmService.findForwardConfig();
	}
	
	@RequestMapping(value="/ForwardConfig",produces="application/json;charset=UTF-8")
	@ResponseBody
	public boolean ForwardConfig(@RequestBody AlarmForwardConfigVO vo) {
		logger.info(vo.toString());
		AlarmForwardConfig config  = new AlarmForwardConfig();
		config.setId(vo.getId());
		config.setConfigContent(vo.getConfigContent());
		config.setConfigName(vo.getConfigName());
		config.setGrade(vo.getGrade());
		config.setType(vo.getType());
		String ids = vo.getSourceIds();
		String[] idStr = ids.split(",");
		List<Integer> lIds = new ArrayList<>();
		for(String id:idStr) {
			lIds.add(Integer.valueOf(id));
		}
		config.setSources(new HashSet<>(devResourceService.findPlayByIds(lIds)));
		
		String acceptorIds = vo.getAcceptors();
		String[] acceptorIdStr = acceptorIds.split(",");
		List<Integer> acceptors = new ArrayList<>();
		for(String id:acceptorIdStr) {
			acceptors.add(Integer.valueOf(id));
		}
		config.setAcceptor(new HashSet<>(userService.findUserByIds(acceptors)));
		AlarmForwardConfig result = alarmService.saveAlarmConfig(config);
		return result!=null?true:false;
	}
	/**
     * 文件下载
     * @Description: 
     * @param fileName
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/download",produces = {"application/vnd.ms-excel;charset=UTF-8"})
    public ResponseEntity<byte[]> download(
    		@RequestParam(value="type",defaultValue="0")String type,
    		@RequestParam(value="grade",defaultValue="0")int grade,
			@RequestParam(value="confirmStatus",defaultValue="0")int confirmStatus,
			@RequestParam(value="cleanStatus",defaultValue="0")int cleanStatus,
			@RequestParam(value="receiptTime",defaultValue="0")int receiptTime/*,
			@RequestParam(value="rows",defaultValue="10")List<Integer> sourceTypes,
			@RequestParam(value="rows",defaultValue="10")List<Integer> sources*/) throws IOException {
    	AlarmParam param = new AlarmParam();
		/*param.setGrade(grade);
		param.setCleanStatu(cleanStatus);
		param.setConfirmStatu(confirmStatus);
		param.setDates(receiptTime);
		param.setSourceType(null);
		param.setSource(null);
		param.setSorts(new String[] {"receiptTime"});*/
		//List<Alarm> alarm = alarmService.findAllRealtimeAlarms(param);
        byte[] body = null;
        InputStream is = alarmService.getRealTimeExcel(param,type);
        body = new byte[is.available()];
        is.read(body);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attchement;filename=" + System.currentTimeMillis());
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
        return entity;
    }
   
	@PostMapping("/confirmAlarm")
	@ResponseBody
	public List<Alarm> confirmAlarm(@RequestParam("alarmId")String alarmIds,@RequestParam("isConfirm") boolean isConfirm) {
		if(alarmIds.trim().equals("")) {
			return null;
		}
		String[] ids = alarmIds.split(",");
		List<String> idList = Arrays.asList(ids);
		return alarmService.confirmAlarm(idList, isConfirm);
	}
	
	@PostMapping(value="/cleanAlarm")
	@ResponseBody
	public List<Alarm> cleanAlarm(@RequestParam("alarmId")String alarmIds,@RequestParam("isClean") boolean isClean) {
		if(alarmIds.trim().equals("")) {
			return null;
		}
		String[] ids = alarmIds.split(",");
		List<String> idList = Arrays.asList(ids);
		return alarmService.cleanAlarm(idList, isClean);
	}
	
	@PostMapping("/remarkAlarm")
	@ResponseBody
	public boolean remarkAlarm(@RequestParam("alarmId") String alarmId,@RequestParam("remark")String remark) {
		return alarmService.remarkAlarm(alarmId,remark);
	}
}
