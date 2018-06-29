package com.yuyisz.pis.system.api.dao.impl;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import au.com.bytecode.opencsv.CSVReader;

import com.yuyisz.pis.system.api.dao.SystemApiRepository;
import com.yuyisz.pis.system.api.module.CoreStatus;
import com.yuyisz.pis.system.api.module.MyService;

@Repository
public class SystemApiRespositoryImpl implements SystemApiRepository {

	@Override
	public List<CoreStatus> getCoreStatus() throws Exception {
		List<CoreStatus> ls = new ArrayList<CoreStatus>();
		FileReader fReader;
		fReader = new FileReader("/opt/Core_service_password.csv");
		CSVReader csvReader = new CSVReader(fReader);
		List<String[]> str = csvReader.readAll();
		for (String[] strings : str) {
			CoreStatus serve = new CoreStatus();
			serve.setClusterName(strings[0]);
			serve.setVmIp(strings[1]);
			serve.setFloatIp(strings[2]);
			serve.setPassword(strings[4]);
			serve.setServiceUser(strings[5]);
			serve.setServicePwd(strings[6]);
			ls.add(serve);
		}
		csvReader.close();

		return ls;
	}

	@Override
	public List<MyService> findStatus() throws Exception {
		List<MyService> ls = new ArrayList<MyService>();
		FileReader fReader;
		fReader = new FileReader("/opt/sys_status.csv");
		CSVReader csvReader = new CSVReader(fReader);
		List<String[]> str = csvReader.readAll();
		for (String[] strings : str) {
			MyService serve = new MyService();
			serve.setClusterName(strings[0]);
			serve.setNodeType(strings[1]);
			serve.setVmName(strings[2]);
			serve.setVmIp(strings[3]);
			serve.setFloatIp(strings[4]);
			serve.setStatus(Integer.parseInt(strings[5]));
			serve.setServerName(strings[6]);
			serve.setServerIp(strings[7]);
			serve.setCpuUtilization(strings[8]);
			serve.setMemUtilization(strings[9]);
			serve.setDiskUtilization(strings[10]);
			ls.add(serve);
		}
		csvReader.close();
		return ls;
	}

}
