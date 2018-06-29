package pis.ui.com.yuyisz.pis.ui.service.performance;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yuyisz.pis.Application;
import com.yuyisz.pis.ui.module.performance.CollectionInfo;
import com.yuyisz.pis.ui.module.performance.CollectionTemplate;
import com.yuyisz.pis.ui.service.performance.CollectionInfoService;
import com.yuyisz.pis.ui.service.performance.CollectionTemplateService;
import com.yuyisz.pis.ui.service.security.DevResourceService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class PerformanceServiceTest extends TestBase {

	@Autowired
	CollectionInfoService service;
	@Autowired
	DevResourceService devService;

	@Autowired
	CollectionTemplateService tempService;

	@Autowired
	CollectionInfoService monitorInfoService;
	
	CollectionInfo info = null;
	@Override
	public void setUp() throws Exception {

	}

	@Test
	public void test() {
		info = new CollectionInfo();
		info.setCreateTime(new Date());
		info.setRatios(12, 3, 23, 21, 3);
		info.setDev(devService.findRandomOne());
		service.save(info);
		info = new CollectionInfo();
		info.setCreateTime(new Date());
		info.setRatios(12, 3, 23, 21, 3);
		info.setDev(devService.findRandomOne());
		service.save(info);
		info = new CollectionInfo();
		info.setCreateTime(new Date());
		info.setRatios(12, 3, 23, 21, 3);
		info.setDev(devService.findRandomOne());
		service.save(info);
		info = new CollectionInfo();
		info.setCreateTime(new Date());
		info.setRatios(12, 3, 23, 21, 3);
		info.setDev(devService.findRandomOne());
		service.save(info);
	}

	@Test
	public void testSaveTemp() {
		/*
		 * MonitorTemplate temp = new MonitorTemplate(); temp.setTempName("测试1");
		 * temp.setRatio(true,true,true,false,false); tempService.save(temp);
		 */
		/*CollectionTemplate temp2 = new CollectionTemplate();
		temp2.setTempName("测试2");
		temp2.setRatio(true, true, true, false, false);
		tempService.save(temp2);
		CollectionTemplate temp3 = new CollectionTemplate();
		temp3.setTempName("测试3");
		temp3.setRatio(true, true, true, false, false);
		tempService.save(temp3);
		CollectionTemplate temp4 = new CollectionTemplate();
		temp4.setTempName("测试4");
		temp4.setRatio(true, true, true, false, false);
		tempService.save(temp4);*/
	}

	/**
	 * 测试生成监控信息
	 */
	/*@Test
	public void testCreateMonitorInfo() {

		Random random = new Random(100);
		Calendar cal = Calendar.getInstance();
		cal.set(2017, 11, 20, 8, 30, 0);
		for(int i=0 ; i < 1000 ; i++) {
			MonitorInfoBean bean = new MonitorInfoBean();
			bean.setCreateTime(cal.getTime());
			bean.setDev(devService.findRandomOne());
			bean.setRatios(random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100), random.nextInt(100));
			monitorInfoService.save(bean);
			cal.add(Calendar.MINUTE, 10);
		}
	}*/

}
