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
 *  - seqnum (10) 
 * @author hyonchuloh
 *
 */
@Repository
public class ApiNumberingSequenceDaoImpl implements ApiNumberingSequenceDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public double getNext() {
		String sql = "SELECT seqnum FROM api_sequence WHERE api_date=?";
		logger.debug("--- GET-NEXT SELECT SQL : {}" , sql);
		double maxValue = 0d;
		String date = sdf.format(new Date());
		try {
			maxValue = jdbcTemplate.queryForObject(sql, Double.class, date);
		} catch ( IncorrectResultSizeDataAccessException irsdae ) { // 조회결과가 없는 경우
			logger.info("--- TODAY FIRST SEQUENCE NUMBER!!");
			jdbcTemplate.update("INSERT INTO api_sequence VALUES (?,?)", date, 0d);
		}
		String update = "UPDATE api_sequence SET seqnum=? WHERE api_date=?";
		logger.debug("--- GET-NEXT UPDATE SQL : {}" , update);
		int updateResult = jdbcTemplate.update(update, maxValue+1, date);
		logger.info("--- GET-NEXT UPDATE RESULT : {}" , updateResult);
		return new Double(maxValue+1).doubleValue();
	}
	
	@Override
	public int initTable() {
		String sql = "create table api_sequence (api_date char(10), seqnum double);";
		return jdbcTemplate.update(sql);
	}

}
