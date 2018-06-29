package com.yuyisz.pis.ui.dao.subsystem.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import au.com.bytecode.opencsv.CSVReader;

import com.yuyisz.pis.ui.controller.subsystem.centersubsystem.CenterSubsystemController;
import com.yuyisz.pis.ui.dao.subsystem.ServiceInformationRepository;
import com.yuyisz.pis.ui.module.subsystem.MyService;

@Repository
public class ServiceInformationRepositoryImpl implements
		ServiceInformationRepository {

	@Override
	public List<MyService> findAll(File configFile) throws IOException {
		List<MyService> ls = new ArrayList<MyService>();
		FileReader fReader = new FileReader(configFile);
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

	@Override
	public List<MyService> findByClusterName(File configFile, String clusterName)
			throws IOException {
		List<MyService> ls = new ArrayList<MyService>();
		FileReader fReader = new FileReader(configFile);
		CSVReader csvReader = new CSVReader(fReader);
		List<String[]> str = csvReader.readAll();
		for (String[] strings : str) {
			if (clusterName.equals(strings[0])) {
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
		}
		csvReader.close();
		return ls;
	}

	@Override
	public MyService getIpPassword(String clusterName) throws IOException {
		FileReader fReader = new FileReader(CenterSubsystemController.CoreFile);
		CSVReader csvReader = new CSVReader(fReader);
		List<String[]> str = csvReader.readAll();
		MyService serve = new MyService();
		for (String[] strings : str) {
			if (clusterName.equals(strings[0])) {
				serve.setVmIp(strings[1]);
				serve.setPassword(strings[4]);
			}
		}
		csvReader.close();
		return serve;
	}

	@Override
	public List<MyService> findByServerName(File configFile, String serverName)
			throws IOException {
		List<MyService> ls = new ArrayList<MyService>();
		FileReader fReader = new FileReader(configFile);
		CSVReader csvReader = new CSVReader(fReader);
		List<String[]> str = csvReader.readAll();
		for (String[] strings : str) {
			if (serverName.equals(strings[6])) {
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
		}
		csvReader.close();
		return ls;
	}

}
