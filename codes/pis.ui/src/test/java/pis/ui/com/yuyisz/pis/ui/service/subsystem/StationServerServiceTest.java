package pis.ui.com.yuyisz.pis.ui.service.subsystem;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyisz.pis.ui.module.subsystem.StationServer;
import com.yuyisz.pis.ui.service.subsystem.StationServerService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class StationServerServiceTest extends TestBase {
	@Autowired
	private StationServerService stationServerService;
	@Override
	public void setUp() throws Exception {
		StationServer stationServer = stationServerService.findOne(3, 1, 1);
		System.out.println();
	}
	
	@Test
	public void test() {
		
	}
}
