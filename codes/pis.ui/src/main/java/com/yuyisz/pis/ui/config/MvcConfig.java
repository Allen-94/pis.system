package com.yuyisz.pis.ui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
//@EnableWebMvc
public class MvcConfig extends WebMvcConfigurerAdapter {
	
/*	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/");
	}
	*/
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		super.addViewControllers(registry);
		registry.addViewController("/").setViewName("/index");
		registry.addViewController("/login").setViewName("/login");
		registry.addViewController("/alarm/alarmFoward").setViewName("/alarm/alarmFoward");
		registry.addViewController("/alarm/historyAlarmList").setViewName("/alarm/historyAlarmList");
		registry.addViewController("/alarm/realtimeAlarmList").setViewName("/alarm/realtimeAlarmList");
		registry.addViewController("/alarm/alarmForwardByMail").setViewName("/alarm/alarmForwardByMail");
		registry.addViewController("/alarm/alarmForwardBySMS").setViewName("/alarm/alarmForwardBySMS");
		registry.addViewController("/dev/loginRemotePlayer").setViewName("/dev/loginRemotePlayer");
		registry.addViewController("/dev/playerversion").setViewName("/dev/playerversion");
		registry.addViewController("/dev/screenonoff").setViewName("/dev/screenonoff");
		registry.addViewController("/dev/systemoperationmonitor").setViewName("/dev/systemoperationmonitor");
		registry.addViewController("/operation/playerbinding").setViewName("/operation/playerbinding");
		registry.addViewController("/operation/playerEPG").setViewName("/operation/playerEPG");
		registry.addViewController("/operation/playergroup").setViewName("/operation/playergroup");
		registry.addViewController("/operation/playerlayout").setViewName("/operation/playerlayout");
		registry.addViewController("/operation/playermonitor").setViewName("/operation/playermonitor");
		registry.addViewController("/performance/broadcastsumreport").setViewName("/performance/broadcastsumreport");
		registry.addViewController("/performance/devsumreport").setViewName("/performance/devsumreport");
		registry.addViewController("/performance/realtimesum").setViewName("/performance/realtimesum");
		registry.addViewController("/performance/performancedata").setViewName("/performance/performancedata");
		registry.addViewController("/performance/collectTask").setViewName("/performance/collectTask");
		registry.addViewController("/performance/taskThresholdSet").setViewName("/performance/taskThresholdSet");
		registry.addViewController("/performance/collectiontemplate").setViewName("/performance/collectiontemplate");
		registry.addViewController("/security/grouplist").setViewName("/security/grouplist");
		registry.addViewController("/security/userlist").setViewName("/security/userlist");
		registry.addViewController("/security/rolelist").setViewName("/security/rolelist");
		registry.addViewController("/security/roleAdded").setViewName("/security/roleAdded");
		registry.addViewController("/security/opraLogs").setViewName("/security/opraLogs");
		registry.addViewController("/security/UserAdded").setViewName("/security/UserAdded");
		registry.addViewController("/security/groupAdded").setViewName("/security/groupAdded");
		registry.addViewController("/security/operationLog").setViewName("/security/operationLog");
		registry.addViewController("/security/operationsMode").setViewName("/security/operationsMode");
		registry.addViewController("/approveflow/myflow").setViewName("/approveflow/myflow");
		registry.addViewController("/approveflow/todoflow").setViewName("/approveflow/todoflow");
		registry.addViewController("/infosource/mediaSourcesManage").setViewName("/infosource/mediaSourcesManage");
		registry.addViewController("/infosource/vedioSet").setViewName("/infosource/vedioSet");
		registry.addViewController("/infosource/atsSet").setViewName("/infosource/atsSet");
		registry.addViewController("/infosource/otherSet").setViewName("/infosource/otherSet");
		registry.addViewController("/infosource/systemVariable").setViewName("/infosource/systemVariable");
		registry.addViewController("/infosource/addSystemVariable").setViewName("/infosource/addSystemVariable");
		registry.addViewController("/player/groupmanage").setViewName("/player/groupmanage");
		registry.addViewController("/player/contentpackage").setViewName("/player/contentpackage");
		registry.addViewController("/player/format").setViewName("/player/format");
		registry.addViewController("/player/playlist").setViewName("/player/playlist");
		registry.addViewController("/player/sendplaylist").setViewName("/player/sendplaylist");
		registry.addViewController("/player/addContentPackage").setViewName("/player/addContentPackage");
		//registry.addViewController("/player/addPackageContent").setViewName("/player/addPackageContent");
		registry.addViewController("/player/addPlayerFormat").setViewName("/player/addPlayerFormat");
		registry.addViewController("/player/addPlayerList").setViewName("/player/addPlayerList");
		registry.addViewController("/player/addPlayerRole").setViewName("/player/addPlayerRole");
		registry.addViewController("/reserveplan/reserveplan").setViewName("/reserveplan/reserveplan");
		registry.addViewController("/reserveplan/reserveinfo").setViewName("/reserveplan/reserveinfo");
		registry.addViewController("/subsystem/examinePlayerlist").setViewName("/subsystem/examineplayercontrollerlist");
		registry.addViewController("/subsystem/examineStationServerlist").setViewName("/subsystem/examinestationserverlist");
		registry.addViewController("/subsystem/reinitializeplaycontrollerlist").setViewName("/subsystem/reinitializeplaycontrollerlist");
		registry.addViewController("/subsystem/reinitializestationserverlist").setViewName("/subsystem/reinitializestationserverlist");
		registry.addViewController("/reserveplan/reserveplan").setViewName("/reserveplan/reserveplan");
		registry.addViewController("/reserveplan/reserveinfo").setViewName("/reserveplan/reserveinfo");
		registry.addViewController("/playmonitor/reserveplanrun").setViewName("/playmonitor/reserveplanrun");
		registry.addViewController("/subsystem/upgradeplaycontroller").setViewName("/subsystem/upgradestationplaycontroller");
		registry.addViewController("/subsystem/upgradestationserver").setViewName("/subsystem/upgradestationserver");
		registry.addViewController("/subsystem/replacestationplaycontroller").setViewName("/subsystem/replacestationplaycontroller");
		registry.addViewController("/subsystem/replacestationserver").setViewName("/subsystem/replacestationserver");
		registry.addViewController("/subsystem/addstation").setViewName("/subsystem/addstation");
		registry.addViewController("/subsystem/addstationserver").setViewName("/subsystem/addstationserver");
		registry.addViewController("/subsystem/addstationplaycontroller").setViewName("/subsystem/addstationplaycontroller");
		registry.addViewController("/subsystem/deletestationserver").setViewName("/subsystem/deletestationserver");
		registry.addViewController("/subsystem/deletestation").setViewName("/subsystem/deletestation");
		registry.addViewController("/subsystem/deletestationplaycontroller").setViewName("/subsystem/deletestationplaycontroller");
		registry.addViewController("/subsystem/managevehiclemountedplaycontroller").setViewName("/subsystem/managevehiclemountedplaycontroller");
		registry.addViewController("/subsystem/reinitializevehiclemountedplaycontroller").setViewName("/subsystem/reinitializevehiclemountedplaycontroller");
		registry.addViewController("/subsystem/upgradevehiclemountedplaycontroller").setViewName("/subsystem/upgradevehiclemountedplaycontroller");
		registry.addViewController("/subsystem/replacevehiclemountedplaycontroller").setViewName("/subsystem/replacevehiclemountedplaycontroller");
		registry.addViewController("/subsystem/addvehiclemountedplaycontroller").setViewName("/subsystem/addvehiclemountedplaycontroller");
		registry.addViewController("/subsystem/deletevehiclemountedplaycontroller").setViewName("/subsystem/deletevehiclemountedplaycontroller");
		registry.addViewController("/subsystem/modifyusernameandpassword").setViewName("/subsystem/modifyusernameandpassword");
		registry.addViewController("/subsystem/serviceStatusList").setViewName("/subsystem/serviceStatusList");
		registry.addViewController("/subsystem/vmChange").setViewName("/subsystem/vmChange");
		registry.addViewController("/subsystem/serverChange").setViewName("/subsystem/serverChange");
		registry.addViewController("/subsystem/addMppDb").setViewName("/subsystem/addMppDb");
		registry.addViewController("/subsystem/addHdfs").setViewName("/subsystem/addHdfs");
		registry.addViewController("/subsystem/serverWeb").setViewName("/subsystem/serverWeb");
		registry.addViewController("/subsystem/rabbitMqWeb").setViewName("/subsystem/rabbitMqWeb");
		registry.addViewController("/subsystem/hdfsWeb").setViewName("/subsystem/hdfsWeb");
	}
	
/*	@Bean
	public RabbitMqClient rabbitMqClient() throws IOException, TimeoutException {
		return new RabbitMqClient();
	}*/
}
