package com.yuyisz.pis.ui.service.subsystem;

import com.yuyisz.pis.ui.module.subsystem.Screen;

public interface ScreenService {
	public Screen findByLine_idAndStation_idAndPc_idAndScreen_id(int a,int b,int c,int d);
}
