package pis.ui.com.yuyisz.pis.ui.service.approveflow;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;

import com.yuyisz.pis.ui.module.approveflow.ApproveFlow;
import com.yuyisz.pis.ui.service.approveflow.ApproveFlowService;
import com.yuyisz.pis.ui.vo.PageParam;

import junit.framework.TestCase;
import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class ApproveFlowServiceTest extends TestBase{

	@Autowired
	private ApproveFlowService service;
	private int currentUserId = 1 ;
	private PageParam param = null;
	@Before
	public void setUp() throws Exception {
		currentUserId = 1;
		
		param = new PageParam();
		param.setPage(0);
		param.setDirection(Direction.ASC.name());
		param.setPageSize(10);
		param.setProperties("createDate");
	}

	@Test
	public void testFindMyFlow() {
		Page<ApproveFlow> result = service.findMyFlow(currentUserId, param);
		TestCase.assertTrue(result.getContent().size()==1);
	}
	@Test
	public void testFindMyApprove() {
		currentUserId = 3;
		param.setPage(2);
		List<ApproveFlow> result = service.findMyApprove(currentUserId);
		TestCase.assertTrue(result.size()==1);
	}
	@Test
	public void testFindMyApprovedFlow() {
		currentUserId = 2;
		Page<ApproveFlow> result = service.findMyApprovedFlow(currentUserId, param);
		TestCase.assertTrue(result.getContent().size()==1);
	}
}
