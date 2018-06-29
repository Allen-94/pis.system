package com.yuyisz.pis.ui.util;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		exeAdminService adminService = contextRefreshedEvent.getApplicationContext().getBean(exeAdminService.class);
		//创建超级管理员
		adminService.exe();
	}

}
