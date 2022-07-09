package com.kko.ohc.numbering;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.kko.ohc.numbering.dao.ApiNumberingGuidDao;
import com.kko.ohc.numbering.dao.ApiNumberingSequenceDao;

/**
 * 채번 API
 * 실행 방법 : java -jar api-numbering-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
 * 소스 CLONE : 
 * @author hyonchuloh
 * 
 */
@SpringBootApplication
public class ApiNumberingApplication {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ApiNumberingGuidDao guidDao;
	@Autowired
	private ApiNumberingSequenceDao seqDao;

	public static void main(String[] args) {
		SpringApplication.run(ApiNumberingApplication.class, args);
	}
	
	/**
	 * 최초 기동시 GUID 테이블과 SEQUENCE 테이블이 존재하지 않는경우 생성줍니다.
	 */
	@PostConstruct
	public void init() {
		logger.info("--- INIT PROJECT");
		logger.info("--- GUID DB CHECK {}", guidDao.initTable());
		logger.info("--- SEQ DB CHECK {}", seqDao.initTable());
	}

}
