package pis.ui.com.yuyisz.pis.ui.service.security;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyisz.pis.ui.module.security.OperationsMode;
import com.yuyisz.pis.ui.service.security.OperationsModeService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class OperationsModeServiceTest extends TestBase{
	@Autowired
	private OperationsModeService operationsModeService;
	
	private List<OperationsMode> OperationsModes = new ArrayList<OperationsMode>();
	@Before
	public void setUp() throws Exception{
		OperationsModes = mapper.readValue(new File("C:\\Users\\郭芝辰\\Documents\\workspace-sts-3.9.1.RELEASE\\pis.ui\\src\\test\\java\\opertionsMode.json")
				, mapper.getTypeFactory().constructParametricType(List.class, OperationsMode.class));
		for (OperationsMode operationsMode : OperationsModes) {
			operationsModeService.save(operationsMode);
		}
	}
	
	
	@Test
	public void test() {
		
//		operationsModeService.save();
	}
}
