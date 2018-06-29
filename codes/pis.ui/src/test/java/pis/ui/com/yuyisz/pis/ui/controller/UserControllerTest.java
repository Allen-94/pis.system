package pis.ui.com.yuyisz.pis.ui.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JavaType;
import com.yuyisz.pis.ui.module.security.User;
import com.yuyisz.pis.ui.service.security.UserService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;
@Transactional
@Rollback(true)
public class UserControllerTest extends TestBase {
	@Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    private List<User> users = new ArrayList<User>();
    @Autowired
	private UserService userService;
	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
		JavaType javaType = mapper.getTypeFactory().constructParametricType(  
                List.class, User.class);  
		 users = mapper.readValue(new File("D:\\work\\resp\\pis100\\trunk\\pis.ui\\src\\test\\java\\users.json"), javaType);
	}

	@Test
	public void test() {
		//准备数据
		for (User user : users) {
			 userService.saveUser(user);
		 }
		
		//开始调用
		Map<String,Object> param = new HashMap<>();
		param.put("page", "0");
		param.put("size", "2");
		param.put("properties", "account");
		User user = new User();
		user.setGender("男");
		param.put("user", user);
		try {
			String strParam = mapper.writeValueAsString(param);
			System.out.println(strParam);
			mvc.perform(MockMvcRequestBuilders.post("/security/findAllUsers")
					.contentType(MediaType.APPLICATION_JSON)
					.content(strParam)
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(MockMvcResultHandlers.print())
			//.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("'size':2")))
			.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("size")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
