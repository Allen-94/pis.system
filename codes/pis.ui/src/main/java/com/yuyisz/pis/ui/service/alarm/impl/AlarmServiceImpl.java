package com.yuyisz.pis.ui.service.alarm.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.yuyisz.pis.ui.dao.alarm.AlarmForwardConfigRepository;
import com.yuyisz.pis.ui.dao.alarm.AlarmRepository;
import com.yuyisz.pis.ui.dao.security.UserRepository;
import com.yuyisz.pis.ui.module.alarm.Alarm;
import com.yuyisz.pis.ui.module.alarm.AlarmForwardConfig;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.alarm.AlarmService;
import com.yuyisz.pis.ui.util.ExcelUtil;
import com.yuyisz.pis.ui.vo.AlarmParam;
import com.yuyisz.pis.ui.vo.HistoryAlarmCondition;

@Service
public class AlarmServiceImpl implements AlarmService {

	@Autowired
	private AlarmRepository alarmRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AlarmForwardConfigRepository afRepository;
	
	@Override
	public List<Alarm> findCurrentAlarmCriteria(AlarmParam param) {
		//Pageable pageable = new PageRequest(param.getPage(), param.getSize(), new Sort(Direction.DESC,param.getSorts()));
		List<Alarm> alarms=null ;
		alarms = alarmRepository.findAll(new Specification<Alarm>() {
			
			@Override
			public Predicate toPredicate(Root<Alarm> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(param.getGrade()!=-1) {
					predicates.add(criteriaBuilder.equal(root.get("config").get("grade"),param.getGrade()));
				}
				if(param.getSourceId()!= 0) {
					predicates.add(criteriaBuilder.equal(root.get("source").get("id"), param.getSourceId()));
				}
				
				if(param.getDates()!=0) {
					predicates.add(criteriaBuilder.greaterThan(root.get("receiptTime"), getDate(param.getDates())));
				}
				
				Predicate p = null;
				if(param.getConfirmStatu()!=0) {
					p = criteriaBuilder.equal(root.get("confirmStatus"), param.getConfirmStatu()==1?true:false);
					//predicates.add(p);
				}
				if(param.getCleanStatu()!=0) {
					predicates.add(criteriaBuilder.or(p, criteriaBuilder.equal(root.get("cleanStatus"), param.getCleanStatu()==1?true:false)));
				}
				
				return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
			
		});
		return alarms;
	}

	public List<Alarm> findAllRealtimeAlarms(AlarmParam param){
		List<Alarm> alarms = alarmRepository.findAll(new Specification<Alarm>() {
			
			@Override
			public Predicate toPredicate(Root<Alarm> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(param.getGrade()!=-1) {
					predicates.add(criteriaBuilder.equal(root.get("config").get("grade"),param.getGrade()));
				}
				
				if(param.getSourceId()!= 0) {
					predicates.add(criteriaBuilder.equal(root.get("source").get("id"), param.getSourceId()));
				}
				
				if(param.getDates()!=0) {
					predicates.add(criteriaBuilder.greaterThan(root.get("receiptTime"), getDate(param.getDates())));
				}
				
				Predicate p1 = criteriaBuilder.equal(root.get("confirmStatus"), false);
				Predicate p2 = criteriaBuilder.equal(root.get("cleanStatus"), false);
				predicates.add(criteriaBuilder.or(p1,p2));
				
				if(param.getConfirmStatu()!=0) {
					predicates.add(criteriaBuilder.equal(root.get("confirmStatus"), param.getConfirmStatu()==1?true:false));
				}
				if(param.getCleanStatu()!=0) {
					predicates.add(criteriaBuilder.equal(root.get("cleanStatus"), param.getCleanStatu()==1?true:false));
				}
				
				return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
			
		});
		return alarms;
	}
	private Date getDate(int dates) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		switch(dates) {
			case 1://最近一天
				c.set(Calendar.DATE, c.get(Calendar.DATE)-1);
				break;
			case 2://最近3天
				c.set(Calendar.DATE, c.get(Calendar.DATE)-3);
				break;
			case 3://最近一周
				c.set(Calendar.WEEK_OF_MONTH, c.get(Calendar.WEEK_OF_MONTH)-1);
				break;
			case 4://最近一个月
				c.set(Calendar.MONTH, c.get(Calendar.MONTH)-1);
				break;
		}
		return c.getTime();
	}
	@Override
	public List<Alarm> confirmAlarm(List<String> alarmId, boolean isConfirm) {
		//Alarm alarm = alarmRepository.findOne((int) alarmId);
		List<Alarm> alarms = alarmRepository.findAll(alarmId);
		try {
		for(Alarm alarm : alarms) {
			if(alarm != null){
				Date now = new Date();
				if(isConfirm) {
					alarm.setConfirmStatus(true);
					alarm.setConfirmUser((User) SecurityUtils.getSubject().getPrincipal());
					alarm.setConfirmTime(now);
				}else {
					alarm.setConfirmStatus(false);
					alarm.setConfirmTime(null);
				}
				alarm.setUpdateTime(now);
			}
		}
		alarmRepository.save(alarms);
		}catch(Exception e) {
			alarms = null;
		}
		return alarms;
	}

	public List<Alarm> cleanAlarm(List<String> alarmId, boolean isClean) {
		List<Alarm> alarms = alarmRepository.findAll(alarmId);
		try {
			for(Alarm alarm : alarms) {
				if(alarm != null){
					Date now = new Date();
					if(isClean) {
						alarm.setCleanStatus(true);
						alarm.setCleanUser((User) SecurityUtils.getSubject().getPrincipal());
						alarm.setCleanTime(now);
					}else {
						alarm.setCleanStatus(false);
						alarm.setCleanTime(null);
					}
					alarm.setUpdateTime(now);
				}
				
			}
			alarmRepository.save(alarms);
		}catch(Exception e) {
			e.printStackTrace();
			alarms = null;
		}
		return alarms;
	}

	@Override
	public boolean saveAlarm(Alarm alarm) {
		alarm.setCreateTime(new Date());
		alarm.setUpdateTime(new Date());
		return alarmRepository.saveAndFlush(alarm)!=null?true:false;
	}

	@Override
	public List<Alarm> findHistoryAlarmCriteria(AlarmParam param) {
		List<Alarm> page=null ;
		page = alarmRepository.findAll(new Specification<Alarm>() {
			
			@Override
			public Predicate toPredicate(Root<Alarm> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if(param.getGrade()!=-1) {
					predicates.add(criteriaBuilder.equal(root.get("config").get("grade"),param.getGrade()));
				}
				
				if(param.getSourceId()!= 0) {
					predicates.add(criteriaBuilder.equal(root.get("source").get("id"), param.getSourceId()));
				}
				
				if(param.getDates()!=0) {
					predicates.add(criteriaBuilder.greaterThan(root.get("receiptTime"), getDate(param.getDates())));
				}
				predicates.add(criteriaBuilder.equal(root.get("confirmStatus"), true));
				predicates.add(criteriaBuilder.equal(root.get("cleanStatus"), true));
				
				return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
			}
			
		});
		return page;
	}

	@Override
	public boolean remarkAlarm(String alarmId, String remark) {
		boolean flag = true;
		try {
		Alarm alarm = alarmRepository.findOne(alarmId);
		if(alarm != null) {
			alarm.setRemark(remark);
			alarmRepository.saveAndFlush(alarm);
		}}catch(Exception e) {
			flag = false;
		}
		return flag;
	}

	@Override
	public List<Alarm> findAllHistoryAlarms(HistoryAlarmCondition condition) {
		
		List<Alarm> alarms = alarmRepository.findAll(new Specification<Alarm>() {
		
		@Override
		public Predicate toPredicate(Root<Alarm> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			if(condition.getGrade()!=-1) {
				predicates.add(criteriaBuilder.equal(root.get("config").get("grade"),condition.getGrade()));
			}
			
			if(condition.getSourceId()!=0) {
				predicates.add(criteriaBuilder.equal(root.get("source"), condition.getSourceId()));
			}
			
			if(condition.getTimeType()!=0) {
				predicates.add(criteriaBuilder.greaterThan(root.get("receiptTime"), getDate(condition.getTimeType())));
			}
			predicates.add(criteriaBuilder.equal(root.get("confirmStatus"), true));
			predicates.add(criteriaBuilder.equal(root.get("cleanStatus"), true));
			
			return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
		}
		
	});
		return alarms;
	}

	private List<Map<String, Object>> createExcelRecord(List<Alarm> AlarmList) {
        List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sheetName", "sheet1");
        listmap.add(map);
        for (int j = 0; j < AlarmList.size(); j++) {
        	Alarm alarm=AlarmList.get(j);
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue.put("grade", alarm.getConfig().getGrade());
            mapValue.put("alarmName", alarm.getConfig().getAlarmName());
            mapValue.put("sourceType", alarm.getSource().getResourceType());
            mapValue.put("source", alarm.getSource().getResourceName());
            mapValue.put("location", alarm.getContent());
            mapValue.put("receiptTime", alarm.getReceiptTime());
            mapValue.put("clearTime", alarm.getCleanTime());
            mapValue.put("confirmStatus", alarm.isConfirmStatus());
            mapValue.put("cleanStatus", alarm.isCleanStatus());
            if(alarm.getConfirmUser()!=null) {
            	mapValue.put("confirmUser", alarm.getConfirmUser().getAccount());
            }else {
            	mapValue.put("confirmUser", "----");
            }
            if(alarm.getCleanUser()!=null) {
            	mapValue.put("cleanUser", alarm.getCleanUser().getAccount());
            }else {
            	mapValue.put("cleanUser", "-----");
            }
            mapValue.put("remark", alarm.getRemark());
            listmap.add(mapValue);
        }
        return listmap;
    }
	@Override
	public InputStream getRealTimeExcel(AlarmParam param,String type) {
		List<Alarm> alarms = null;
		List<Map<String,Object>> list = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		if(type.equals("realtime")) {
			alarms = findAllRealtimeAlarms(param);
		    list=createExcelRecord(alarms);
		    String columnNames[]={"级别","名称","告警源类型", "告警源", "具体定位","告警时间","确认状态","清除状态","确认人","清除人","备注"};//列名
	        String keys[] = {"grade","alarmName","sourceType","source","location","receiptTime","confirmStatus",
	        		"cleanStatus","confirmUser","cleanUser","remark"};//map中的key
	        try {
	            ExcelUtil.createWorkBook(list,keys,columnNames).write(os);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}else if(type.equals("history")){
			/*alarms = findAllHistoryAlarms(param);
		    list=createExcelRecord(alarms);
		    String columnNames[]={"级别","名称","告警源类型", "告警源", "具体定位","告警时间","清除时间","确认人","清除人","备注"};//列名
	        String keys[] = {"grade","alarmName","sourceType","source","location","receiptTime",
	        		"clearTime","confirmUser","cleanUser","remark"};//map中的key
	        try {
	            ExcelUtil.createWorkBook(list,keys,columnNames).write(os);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }*/
		}
        
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
		return is;
	}

	@Override
	public AlarmForwardConfig saveAlarmConfig(AlarmForwardConfig config) {
		AlarmForwardConfig result = afRepository.saveAndFlush(config);
		return result;
	}

	@Override
	public List<AlarmForwardConfig> findForwardConfig() {
		return afRepository.findAll();
	}

	@Override
	public AlarmForwardConfig findForwardConfigById(long id,int type) {
		return afRepository.findOne(id);
	}

	@Override
	public void deleteConfig(long id, int type) {
		afRepository.delete(id);
	}

	@Override
	public Alarm findAlarmById(String alarmId) {
		return alarmRepository.findOne(alarmId);
	}
}
