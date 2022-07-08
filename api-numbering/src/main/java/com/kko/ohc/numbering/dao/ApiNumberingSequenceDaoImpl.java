package com.kko.ohc.numbering.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ApiNumberingSequenceDaoImpl implements ApiNumberingSequenceDao {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public int getNext() {
		
		return 0;
	}

}
