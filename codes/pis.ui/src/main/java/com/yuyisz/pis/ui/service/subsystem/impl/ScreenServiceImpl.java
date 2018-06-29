package com.yuyisz.pis.ui.service.subsystem.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuyisz.pis.ui.dao.subsystem.ScreenRepository;
import com.yuyisz.pis.ui.module.subsystem.Screen;
import com.yuyisz.pis.ui.service.subsystem.ScreenService;
@Service
public class ScreenServiceImpl implements ScreenService {
	@Autowired
	private ScreenRepository screenRepository;
	@Override
	public Screen findByLine_idAndStation_idAndPc_idAndScreen_id(int a, int b, int c, int d) {
		return screenRepository.findByLine_idAndStation_idAndPc_idAndScreen_id(a, b, c, d);
	}

}
