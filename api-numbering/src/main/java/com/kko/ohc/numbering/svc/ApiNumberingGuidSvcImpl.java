package com.kko.ohc.numbering.svc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.ParseException;
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
 * guid 구성 = yyyyMMddHHmmss(14) + hostname(8) + random(4) + seqnum(4)
 * 
 * 1) 최초 인풋값이 없는 경우 GUID를 새롭게 채번한다. (최초채번시 전행연속번호는 00)
 *    - 최초 채번시에는 관리테이블에 insert 하지 아니한다.
 *    
 * 2) 인풋값이 존재하는 경우 해당 GUID에 전행연속번호를 max+1 하여 채번한다.
 *    - 인풋값이 존재하는 경우는 관리테이블에서 max 연속번호를 조회하여 next 값을 가져오며 테이블에 update 한다.
 */
@Service
public class ApiNumberingGuidSvcImpl implements ApiNumberingGuidSvc {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ApiNumberingGuidDao dao;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
	private DecimalFormat df = new DecimalFormat("0000");
		
	@Override
	public String createGuid() {
		String yyMMddHHmmssSS = sdf.format(new Date());									
		yyMMddHHmmssSS = yyMMddHHmmssSS.substring(0, 14);
		String hostname = getHostname().toUpperCase();
		String random = getRandNumber();
		String retValue = yyMMddHHmmssSS + hostname + random + "0000";
		logger.info("--- CREATE GUID : {}", retValue);
		dao.insert(retValue);
		return retValue;
	}

	@Override
	public synchronized String nextGuid(String guid) {
		String guid_prifix = guid.substring(0,24);
		int result = dao.getNext(guid_prifix);
		logger.info("--- NEXT GUID : {}", guid_prifix + df.format(result));
		return guid_prifix + df.format(result);
	}
	
	private String getHostname() {
		String retValue = "DEFAULT1";
		try {
			retValue = InetAddress.getLocalHost().getHostName();
			if ( retValue != null && retValue.length() > 8 ) {
				retValue = retValue.substring(retValue.length()-8);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return retValue;
	}
	
	private String getRandNumber() {
		int rand = (int) (Math.random()*(9999));
		return df.format(rand);
	}

}
