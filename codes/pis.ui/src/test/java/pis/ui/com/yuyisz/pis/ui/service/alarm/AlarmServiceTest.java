package pis.ui.com.yuyisz.pis.ui.service.alarm;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuyisz.pis.Application;
import com.yuyisz.pis.ui.module.alarm.Alarm;
import com.yuyisz.pis.ui.module.alarm.AlarmGrade;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.alarm.AlarmService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

/*@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！ 
@SpringBootTest(classes = Application.class) // 指定我们SpringBoot工程的Application启动类
@EnableWebSocket*/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes=Application.class)
public class AlarmServiceTest extends TestBase {

	@Autowired
	AlarmService alarmService;
	private List<Alarm> alarms = new ArrayList<Alarm>();
	private User user = new User();
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}
	
	/**
	 * 加载测试数据
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		/*Alarm alarm = new Alarm();
		alarm.setAlarmName("播放控制器硬盘故障");
		alarm.setGrade(AlarmGrade.URGENT);
		alarm.setSourceType("播放控制器");
		alarm.setSource("1号线南山站上行播放控制器（IP：192.168.0.9)");
		alarm.setLocation("硬盘序号＝1");
		alarm.setReceiptTime(new Date());
		alarm.setRemark("张三在处理中，等待配件");
		alarms.add(alarm);
		mapper.writeValue(new File("D:\\work\\resp\\pis100\\trunk\\pis.ui\\src\\test\\java\\alarms.json"), alarms);
		*/
		user.setAccount("张三");
		user.setUserId(1);
		user.setEmail("1232@qq.com");
		user.setGender("男");
		user.setPassword("123456");
		user.setPhone("123456789");
		user.setRealname("张三");
		 JavaType javaType = mapper.getTypeFactory().constructParametricType(  
	                List.class, Alarm.class);  
		List<Alarm> alarms = mapper.readValue(new File("C:\\work\\trunk\\pis.ui\\src\\test\\java\\alarms.json"),javaType);
		for(Alarm alarm : alarms) {
			alarmService.saveAlarm(alarm);
		}
	}

	/**
	 * 测试新增警告
	 */
	@Test
	@Transactional
	@Rollback(true)
	public void testSaveAlarm() {
		/*Alarm alarm = new Alarm();
		alarm.setAlarmName("播放控制器硬盘故障");
		alarm.setGrade(AlarmGrade.URGENT);
		alarm.setSourceType("播放控制器");
		alarm.setSource("1号线南山站上行播放控制器（IP：192.168.0.9)");
		alarm.setLocation("硬盘序号＝1");
		alarm.setReceiptTime(new Date());
		alarm.setRemark("张三在处理中，等待配件");
		boolean bool = alarmService.saveAlarm(alarm);
		assertFalse(!bool);*/
	}
	
	/**
	 * 测试获取当前告警信息
	 */
	@Test
	@Transactional
	@Rollback(false)
	public void testFindCurrentAlarm() {
		/*int page = 0;
		int size = 10;
		String[] properties = {"alarmId"};
		Alarm alarm = new Alarm();
		alarm.setAlarmName("播放控制器硬盘故障");
		Page<Alarm> resoult = alarmService.findCurrentAlarmCriteria(page, size, properties, alarm);
		assertTrue(resoult.getContent().get(0).getAlarmId()==15);*/
	}
	/**
	 * 测试获取历史告警信息
	 */
	@Test
	@Transactional
	@Rollback(true)
	public void testFindHistoryAlarm() {
		/*int page = 0;
		int size = 10;
		String[] properties = {"alarmId"};
		Alarm alarm = new Alarm();
		alarm.setAlarmName("播放控制器硬盘故障");
		Page<Alarm> resoult = alarmService.findCurrentAlarmCriteria(page, size, properties, alarm);
		assertTrue(resoult.getContent().get(0).getAlarmId()==15);*/
	}
	
	/**
	 * 测试确认告警
	 */
	@Test
	@Transactional
	@Rollback(true)
	public void testConfirmAlarm() {
		/*boolean confirm = alarmService.confirmAlarm(10, user.getUserId(), true);
		assertTrue(confirm);
		confirm = alarmService.confirmAlarm(10, user.getUserId(), false);
		assertFalse(confirm);*/
	}
	
	/**
	 * 测试清除告警
	 */
	@Test
	@Transactional
	@Rollback(true)
	public void testCleanAlarm() {
		fail("Not yet implemented");
	}
}
