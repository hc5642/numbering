package com.kko.ohc.numbering.ctl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kko.ohc.numbering.dao.ApiNumberingGuidDao;
import com.kko.ohc.numbering.dao.ApiNumberingSequenceDao;

@RestController
public class ApiNumberingInitCtl {
	
	@Autowired
	private ApiNumberingGuidDao guidDao;
	@Autowired
	private ApiNumberingSequenceDao seqDao;
	
	@RequestMapping("/init-guid-table")
	public String initGuidTable() {
		return new Integer(guidDao.initTable()).toString();
	}
	
	@RequestMapping("/init-seq-table")
	public String initSeqTable() {
		return new Integer(seqDao.initTable()).toString();
	}

}
