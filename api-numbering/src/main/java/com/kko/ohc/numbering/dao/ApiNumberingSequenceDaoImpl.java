package com.kko.ohc.numbering.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * sequence 관리 테이블
 *  - date (8) - pk
 *  - seqnum (10) 
 * @author hyonchuloh
 *
 */
@Repository
public class ApiNumberingSequenceDaoImpl implements ApiNumberingSequenceDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public int getNext() {
		
		return 0;
	}
	
	@Override
	public int initTable() {
		return 0;
	}

}
