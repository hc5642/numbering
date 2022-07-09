package com.kko.ohc.numbering.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kko.ohc.numbering.dao.ApiNumberingSequenceDao;

/**
 * 시퀀스 채번 서비스
 * 1) db로부터 next 시퀀스를 가져온다.
 * @author hyonchuloh
 *
 */
@Service
public class ApiNumberingSequenceSvcImpl implements ApiNumberingSequenceSvc {
	
	@Autowired
	private ApiNumberingSequenceDao seqDao;
	
	/**
	 * 현재 시퀀스 번호를 조회하여 반환합니다.
	 */
	@Override
	public String current() {
		return new Double(seqDao.select()).toString();
	}

	/**
	 * 다음 시퀀스번호를 조회하여 반환합니다. (synchronized)
	 */
	@Override
	public synchronized String next() {
		double result = seqDao.getNext();
		return new Double(result).toString();
	}

}
