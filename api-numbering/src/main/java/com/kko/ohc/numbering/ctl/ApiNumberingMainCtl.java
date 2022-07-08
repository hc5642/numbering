package com.kko.ohc.numbering.ctl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiNumberingMainCtl {
	
	@RequestMapping("/guid")
	public String guid() {
		return "ok";
	}

}
