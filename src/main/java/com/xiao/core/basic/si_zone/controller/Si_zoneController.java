package com.xiao.core.basic.si_zone.controller;



import javax.annotation.Resource;
import com.xiao.base.BaseController;
import com.xiao.core.basic.si_zone.service.Si_zoneService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/admin/si_zone")
public class Si_zoneController extends BaseController {

	@Resource
	Si_zoneService si_zoneService;
 

}