package com.kko.ohc.numbering.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.sqlite.SQLiteException;

/**
 * guid 관리 테이블
 *  - guid (26) pk
 *  - seqnum (4)
 * @author hyonchuloh
 *
 */
@Repository
public class ApiNumberingGuidDaoImpl implements ApiNumberingGuidDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 채번된 GUID를 DB에 저장합니다. 
	 * 이때 GUID중 앞 26자리에 해당하는 값만 저장합니다.(나머지 4자리는 연속번호 채번용)
	 * 
	 * @param	guid	// 30자리의 FULL GUID 가 넘어옵니다.
	 * @return	update수행결과 1 또는 0 반환합니다.
	 */
	@Override
	public int insert(String guid) {
		logger.info("--- INSERT GUID : {}", guid.substring(0,26));
		return jdbcTemplate.update("INSERT INTO api_guid VALUES (?,?)", guid.substring(0,26), 0);
	}

	/**
	 * 넘어온 GUID 값을 가지고 DB조회하여 최종 연속번호의 +1 번호를 리턴합니다.
	 * 해당 값은 다시 DB에 업데이트합니다.
	 *
	 * @param 	guid	// 이미 26길이로 잘려진채로 넘어옵니다.
	 * @return	해당 guid의 다음 연속번호를 채번한 값을 반환합니다.
	 */
	@Override
	public int getNext(String guid) {
		int maxValue = jdbcTemplate.queryForObject("SELECT seqnum FROM api_guid WHERE guid=?", Integer.class, guid);
		logger.info("--- SELECT guid seqnum RESULT : guid=[{}], seqnum=[{}]", guid, maxValue);
		if ( maxValue >= 99 ) // 2자리수를 넘어가는 연속번호 발생시0으로 다시 초기화
			maxValue = 0;
		int updateResult = jdbcTemplate.update("UPDATE api_guid SET seqnum=? WHERE guid=?", maxValue+1, guid);
		logger.info("--- UPDATE RESULT : {}" , updateResult);
		return maxValue+1;
	}
	
	/**
	 * 테이블을 생성합니다. (최초기동시 1회만 수행)
	 */
	@Override
	public int initTable() {
		int retValue = 0;
		try {
			retValue = jdbcTemplate.update("CREATE TABLE api_guid (guid char(26) PRIMARY KEY, seqnum INTEGER);");
			logger.info("--- 테이블 생성 완료.api_guid");
		} catch ( Exception e ) {
			logger.info("--- 이미 테이블이 존재합니다. {}", e.getMessage());
		}
		return retValue;
	}
	
}
