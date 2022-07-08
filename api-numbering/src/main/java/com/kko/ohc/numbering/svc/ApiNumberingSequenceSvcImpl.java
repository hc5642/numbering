package com.kko.ohc.numbering.svc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kko.ohc.numbering.dao.ApiNumberingSequenceDao;

import com.kko.ohc.numbering.dao.ApiNumberingSequenceDao;

@Service
public class ApiNumberingSequenceSvcImpl implements ApiNumberingSequenceSvc {
	
	@Autowired
	private ApiNumberingSequenceDao seqDao;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public String doService() {
		// TODO Auto-generated method stub
		return "sequence";
	}

}
