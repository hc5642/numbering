package com.kko.ohc.numbering.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * sequence 관리 테이블
 *  - date (8) - pk
 *  - seqnum (double) 
 * @author hyonchuloh
 *
 */
@Repository
public class ApiNumberingSequenceDaoImpl implements ApiNumberingSequenceDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 오늘날짜에 해당하는 레코드의 최종 시퀀스번호를 반환합니다.
	 */
	@Override
	public double select() {
		String date = sdf.format(new Date());
		double maxValue = 0d;
		try {
			maxValue = jdbcTemplate.queryForObject("SELECT seqnum FROM api_sequence WHERE api_date=?", Double.class, date);
		} catch ( IncorrectResultSizeDataAccessException irsdae ) { // 조회결과가 없는 경우
			logger.info("--- 금일 시퀀스번호가 채번되지 않았습니다.", irsdae.getMessage());
		}
		return new Double(maxValue).doubleValue();
	}

	/**
	 * 오늘날짜에 해당하는 레코드를 조회하여 최종 시퀀스번호의 +1을 리턴하고,
	 * DB에 다시 업데이트 합니다.
	 * 
	 */
	@Override
	public double getNext() {
		double maxValue = 0d;
		String date = sdf.format(new Date());
		try {
			maxValue = jdbcTemplate.queryForObject("SELECT seqnum FROM api_sequence WHERE api_date=?", Double.class, date);
		} catch ( IncorrectResultSizeDataAccessException irsdae ) { // 조회결과가 없는 경우
			logger.info("--- TODAY FIRST SEQUENCE NUMBER!! {}", irsdae.getMessage());
			jdbcTemplate.update("INSERT INTO api_sequence VALUES (?,?)", date, 0d);
		}
		int updateResult = jdbcTemplate.update("UPDATE api_sequence SET seqnum=? WHERE api_date=?", maxValue+1, date);
		logger.info("--- UPDATE RESULT : {}" , updateResult);
		return new Double(maxValue+1).doubleValue();
	}
	
	/**
	 * 테이블을 생성합니다. (최초기동시 1회만 수행)
	 */
	@Override
	public int initTable() {
		int retValue = 0;
		try {
			retValue = jdbcTemplate.update("CREATE TABLE api_sequence (api_date CHAR(10), seqnum DOUBLE);");
			logger.info("--- 테이블생성 완료.api_sequence");
		} catch ( Exception e ) {
			logger.info("--- 이미 테이블이 존재합니다. {}", e.getMessage());
		}
		return retValue;
	}

}
