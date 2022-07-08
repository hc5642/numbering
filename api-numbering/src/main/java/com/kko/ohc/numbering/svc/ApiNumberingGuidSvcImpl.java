package com.kko.ohc.numbering.svc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ApiNumberingGuidSvcImpl implements ApiNumberingGuidSvc {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String doService() {
		// TODO Auto-generated method stub
		return "guid";
	}

}
