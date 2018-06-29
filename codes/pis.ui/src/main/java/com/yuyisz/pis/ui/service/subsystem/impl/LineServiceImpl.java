package com.yuyisz.pis.ui.service.subsystem.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.subsystem.LineRepository;
import com.yuyisz.pis.ui.module.subsystem.Line;
import com.yuyisz.pis.ui.service.subsystem.LineService;
@Service
public class LineServiceImpl implements LineService {
	@Autowired
	private LineRepository lineRepository;
	@Override
	public List<Line> findAll() {
		return lineRepository.findAll();
	}

}
