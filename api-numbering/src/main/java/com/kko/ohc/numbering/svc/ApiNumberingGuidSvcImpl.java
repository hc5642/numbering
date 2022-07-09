package com.kko.ohc.numbering.svc;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kko.ohc.numbering.dao.ApiNumberingGuidDao;

/**
 * GUID 채번 서비스 
 * @author hyonchuloh
 * 
 * guid 구성 = yyMMddHHmmssSS(14) + hostname(8) + random(4) + systemCode + seqnum(2)
 * 
 * 1) GUID를 새롭게 채번한다. (최초 채번시 연속번호는 0000)
 *    
 * 2) 해당 GUID에 연속번호를 max+1 하여 채번한다.
 *    - 인풋값이 존재하는 경우는 관리테이블에서 max 연속번호를 조회하여 next 값을 가져오며 테이블에 update 한다.
 */
@Service
public class ApiNumberingGuidSvcImpl implements ApiNumberingGuidSvc {
	
	@Autowired
	private ApiNumberingGuidDao dao;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
	private DecimalFormat df = new DecimalFormat("00");
	private DecimalFormat df2 = new DecimalFormat("0000");
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * GUID 를 최초로 생성합니다.
	 * @param hostname	- 호스트네임 8자리를 넘겨받습니다.
	 */
	@Override
	public String createGuid(String hostname) {
		// 날짜 조립
		String yyMMddHHmmssSS = sdf.format(new Date());									
		yyMMddHHmmssSS = yyMMddHHmmssSS.substring(0, 14);
		// 호스트네임 정비
		if ( hostname.length() > 8 ) {
			hostname = hostname.substring(hostname.length()-8);
		} else if ( hostname.length() < 8 ) {
			hostname = hostname + hostname.substring(8-hostname.length());
		}
		// 랜덤값 4자리 추출
		String random = getRandNumber();
		// 최종 조립
		String retValue = yyMMddHHmmssSS + hostname + random + hostname.substring(0,2) + "00";
		// DB 저장
		logger.info("--- GUID [{}], DB INSERT [{}]", retValue ,dao.insert(retValue));
		return retValue;
	}

	/**
	 * 기존GUID를 받아 뒤에있는 연속번호를 증가시킵니다. (synchronized)
	 * @param	guid 		- 30자리의 FULL GUID를 넘겨받습니다.
	 * @param	systemCode	- 요청시스템으로부터 2자리 시스템코드를 넘겨받습니다.
	 */
	@Override
	public synchronized String nextGuid(String guid, String hostname) {
		String guid_prifix = guid.substring(0,26);
		int result = dao.getNext(guid_prifix);
		return guid_prifix + hostname.substring(0,2) + df.format(result);
	}
	
	/**
	 * 4자리의 랜덤 숫자를 반환합니다.
	 * @return
	 */
	private String getRandNumber() {
		int rand = (int) (Math.random()*(9999));
		return df2.format(rand);
	}

}
