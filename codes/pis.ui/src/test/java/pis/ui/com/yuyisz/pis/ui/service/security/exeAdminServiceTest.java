package pis.ui.com.yuyisz.pis.ui.service.security;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyisz.pis.ui.util.exeAdminService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class exeAdminServiceTest extends TestBase {
	@Autowired
	exeAdminService exeAdminService;
	
	@Test
	public void test() {
		exeAdminService.exe();
	}
}
