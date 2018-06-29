package pis.ui.com.yuyisz.pis.ui.service.security;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuyisz.pis.ui.module.security.DevResources;
import com.yuyisz.pis.ui.service.performance.CollectionTaskService;
import com.yuyisz.pis.ui.service.security.DevResourceService;

import pis.ui.com.yuyisz.pis.ui.service.TestBase;

public class DevResourceServiceTest extends TestBase {

	@Autowired
	DevResourceService service;
	@Autowired
	CollectionTaskService collectionTaskService;
	
	/*
	@Override
	public void setUp() throws Exception {
		DevResources resource = new DevResources();
		resource.setCreateTime(new Date());
		resource.setResourceName("1号线南山站上行播放控制器");
		resource.setUpdateTime(new Date());
		resource.setResourceType(2);
		resource.setIds(1,2,3,4,5);
		service.save(resource);
		DevResources resource2 = new DevResources();
		resource2.setCreateTime(new Date());
		resource2.setResourceName("1号线南山站下行播放控制器");
		resource2.setUpdateTime(new Date());
		resource2.setResourceType(2);
		resource2.setIds(1,2,3,4,6);
		service.save(resource2);
	}*/
	
	@Test
	public void test() {
		
		DevResources resource = new DevResources();
		resource.setId(122);
		resource.setId1(1);
		resource.setId2(2);
		resource.setId3(3);
		resource.setId4(4);
		resource.setId5(5);
		resource.setResourceName("12好心啊");
		resource.setResourceType(2);
		service.save(resource);
	}
	
	@Test
	public void test2() {
		DevResources resource = service.findById(45);
		resource.setCollectionTask(collectionTaskService.findById(1));
		service.save(resource);
	}

}
