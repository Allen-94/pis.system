package pis.ui.com.yuyisz.pis.ui.controller;

import static org.junit.Assert.*;

import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.yuyisz.pis.ui.module.approveflow.ApproveFlow;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.approveflow.ApproveFlowService;
import com.yuyisz.pis.ui.service.security.UserService;
import com.yuyisz.pis.ui.vo.PageParam;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;
//@Transactional
public class ApproveFlowTest extends TestBase {
	@Autowired
	private ApproveFlowService service;
	@Autowired
	private UserService userService;
	@Autowired
    private WebApplicationContext context;
	private MockMvc mvc;
	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
	}

	@Test
	public void testCreateFlow() {
		User approver1 = userService.findUserById(1);
		User approver2 = userService.findUserById(2);
		User approver3 = userService.findUserById(3);
		User currtent = userService.findUserById(1);
		User submitter = userService.findUserById(4);
		ApproveFlow flow = new ApproveFlow();
		flow.setApprover1(approver1);
		flow.setApprover2(approver2);
		flow.setApprover3(approver3);
		flow.setCurrentApprover(currtent);
		flow.setCreateDate(new Date());
		flow.setFlowDescribe("测试");
		flow.setFlowName("测试流程");
		flow.setSubmitter(submitter);
		//flow.setResourcePath("sss,bbb,ccc");
		flow.initFlowProcessSet();
		try {
			String strParam = mapper.writeValueAsString(flow);
			System.out.println(strParam);
			mvc.perform(MockMvcRequestBuilders.post("/approveflow/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(strParam))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindMyFlow() {
		PageParam param = new PageParam();
		//param.setProperties("id");
		try {
			String strParam = mapper.writeValueAsString(param);
			System.out.println(strParam);
			mvc.perform(MockMvcRequestBuilders.post("/approveflow/findmyflow")
					.contentType(MediaType.APPLICATION_JSON)
					.content(strParam))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print())
			.andExpect(MockMvcResultMatchers.content().string(Matchers.notNullValue()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindFlow() {
		User approver1 = userService.findUserById(1);
		User approver2 = userService.findUserById(2);
		User approver3 = userService.findUserById(3);
		User currtent = userService.findUserById(3);
		User submitter = userService.findUserById(4);
		ApproveFlow flow = new ApproveFlow();
		flow.setApprover1(approver1);
		flow.setApprover2(approver2);
		flow.setApprover3(approver3);
		flow.setCurrentApprover(currtent);
		flow.setCreateDate(new Date());
		flow.setFlowDescribe("测试");
		flow.setFlowName("测试流程");
		flow.setSubmitter(submitter);
		//flow.setResourcePath("sss,bbb,ccc");
		flow.initFlowProcessSet();
		try {
			String strParam = mapper.writeValueAsString(flow);
			System.out.println(strParam);
			mvc.perform(MockMvcRequestBuilders.post("/approveflow/create")
					.contentType(MediaType.APPLICATION_JSON)
					.content(strParam))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andReturn();
			mvc.perform(MockMvcRequestBuilders.post("/approveflow/findflow/"+flow.getId())
					.contentType(MediaType.APPLICATION_JSON)
					.content(strParam))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(flow.getId())));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
