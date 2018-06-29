package com.yuyisz.pis.ui.controller.reserveplan;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuyisz.pis.ui.module.reserveplan.ReserveAction;
import com.yuyisz.pis.ui.service.reserveplan.ReserveActionService;

@Controller
@RequestMapping("/reserveplan")
public class ReserveActionController {

	@Autowired
	private ReserveActionService reserveActionService;
	
	@GetMapping("findAllAction")
	@ResponseBody
	public List<ReserveAction> findAllAction(){
		return reserveActionService.findAllAction();
	}
}
