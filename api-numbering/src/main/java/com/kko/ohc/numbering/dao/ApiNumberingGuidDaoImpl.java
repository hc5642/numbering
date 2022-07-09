package com.kko.ohc.numbering.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * guid 관리 테이블
 *  - guid (24) pk
 *  - seqnum (4)
 * @author hyonchuloh
 *
 */
@Repository
public class ApiNumberingGuidDaoImpl implements ApiNumberingGuidDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public int insert(String guid) {
		String sql = "insert into api_guid values (?,?)";
		logger.info("--- INSERT GUID : {}", guid.substring(0,26));
		return jdbcTemplate.update(sql, guid.substring(0,26), 0);
	}

	@Override
	public int getNext(String guid) {
		String sql = "select seqnum from api_guid where guid=?";
		logger.info("--- GET-NEXT SELECT SQL : {}" , sql);
		int maxValue = jdbcTemplate.queryForObject(sql, Integer.class, guid);
		logger.info("--- GET-NEXT SELECT RESULT : {}" , maxValue);
		String update = "update api_guid set seqnum=? where guid=?";
		logger.info("--- GET-NEXT UPDATE SQL : {}" , update);
		int updateResult = jdbcTemplate.update(update, maxValue+1, guid);
		logger.info("--- GET-NEXT UPDATE RESULT : {}" , updateResult);
		return maxValue+1;
	}
	
	@Override
	public int initTable() { 
		String sql = "create table api_guid (guid char(24) primary key , seqnum integer);";
		return jdbcTemplate.update(sql);
	}
	
}
