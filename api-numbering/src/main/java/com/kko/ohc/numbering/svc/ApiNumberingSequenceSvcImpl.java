package com.kko.ohc.numbering.svc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kko.ohc.numbering.dao.ApiNumberingSequenceDao;

/**
 * 시퀀스 채번 서비스
 * 1) db로부터 next 시퀀스를 가져온다.
 * 2) 최종 발행된 시퀀스를 메모리에 캐싱한다.
 * @author hyonchuloh
 *
 */
@Service
public class ApiNumberingSequenceSvcImpl implements ApiNumberingSequenceSvc {
	
	private static String lastestSequence;
	
	@Autowired
	private ApiNumberingSequenceDao seqDao;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public String current() {
		return ApiNumberingSequenceSvcImpl.lastestSequence;
	}

	@Override
	public synchronized String next() {
		double result = seqDao.getNext();
		ApiNumberingSequenceSvcImpl.lastestSequence = new Double(result).toString();
		return ApiNumberingSequenceSvcImpl.lastestSequence;
	}

}
