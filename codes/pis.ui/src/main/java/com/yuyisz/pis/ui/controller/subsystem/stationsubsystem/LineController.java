package com.yuyisz.pis.ui.controller.subsystem.stationsubsystem;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.subsystem.Line;
import com.yuyisz.pis.ui.service.subsystem.LineService;

@Controller
@RequestMapping("/subsystem")
public class LineController {
	@Autowired
	private LineService lineService;
	
	@RequestMapping("/findAllLine")
	@ResponseBody
	public List<Line> findAllLine(){
		List<Line> lines = lineService.findAll();
		return lines;
	}
}
