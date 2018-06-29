package pis.ui.com.yuyisz.pis.ui.service.subsystem;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyisz.pis.ui.module.subsystem.PlaybackController;
import com.yuyisz.pis.ui.service.subsystem.PlaybackControllerService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class PcServiceTest extends TestBase{
	@Autowired
	private PlaybackControllerService pcService;
	@Override
	public void setUp() throws Exception {
		/*PlaybackController pc = new PlaybackController();
		pc.setLine_id(1);
		pc.setStation_id(29);
		pc.setPc_id(2);
		pc.setPc_name("testingpc");
		pc.setPc_place(1);
		pc.setPc_status(1);
		pc.setPc_producer(1);
		pc.setPc_model(1);
		pc.setPc_account("a");
		pc.setPc_password("123");
		pcService.save(pc);*/
		PlaybackController pc = pcService.findOne(2, 2, 2);
		System.out.println(pc.getPc_name());
	}
	@Test
	public void test() {
		
	}
}
