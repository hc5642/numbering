package com.kko.ohc.numbering.ctl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kko.ohc.numbering.svc.ApiNumberingGuidSvc;
import com.kko.ohc.numbering.svc.ApiNumberingSequenceSvc;

/**
 * API 채번 어플리케이션
 * @author 	hyonchuloh
 * @date	2022.07.09
 * 
 * 1. 채번 API 서버로 요청 시, 중복되지 않는 고유의 값을 생성하여 응답합니다. 
 *    1) HTTP Protocol을 사용하고 java로 구현해야 합니다. 
 *    2) 오픈소스 사용에 제한이 없습니다.
 * 2. 다음의 2종의 기능은 필수로 구현되어야 합니다.
 * 3. 채번 API 서버로의 Request 포맷, 인자 등에는 제한이 없습니다. 
 *    1) 중복 및 누락 없이 unique 한 값을 보장하도록 구성해 주세요.
 * 4. 채번 API 서버는 확장에 쉬울 수 있도록 N대 구성(Scale-Out)할 수 있어야 합니다.
 * 5. 대량 Request 발생 시에도 가능한 한 응답시간이 유지될 수 있도록 구현되어야 합니다.
 * 6. 채번 API 소스 코드 수정이 쉽도록 package 구성을 해야 합니다.
 */
@RestController
public class ApiNumberingMainCtl {
	
	@Autowired
	private ApiNumberingGuidSvc guidSvc;
	@Autowired
	private ApiNumberingSequenceSvc seqSvc;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 1) Globally Unique Identifier(이하 GUID) 생성 
	 *    - 하나의 사용자 요청은 내부적으로 여러 서버를 거쳐서야 응답이 가능한 경우가 많습니다. 
	 *      GUID는 사용자의 요청이 어떤 서버들을 거쳐서 처리되었는지 추적에 필요한 정보가 포함되어야 합니다. 
	 *    - N개의 서로 다른 서버에서 채번 API 서버로 GUID 생성을 요청합니다.
	 *    - 응답받은 GUID는 각 서버에서 Transaction의 고유식별키로 사용합니다. 
	 *    - 30자리 자릿수를 가지며, 문자와 숫자로 구성합니다. 
	 *    - 생성 룰은 직관성을 최우선으로 고려하여 직접 정의해주세요. 
	 *    - GUID는 중복이 발생하지 않아야 합니다.
	 *    
	 * @param 	guid 		- 같은 GUID에 연속번호를 추가하려할때 넘겨주어야합니다.
	 * @param	systemCode	- 요청 시스템으로부터 2자리 시스템코드를 받습니다.
	 * @return
	 */
	@RequestMapping("/guid")
	public String guid(
			@RequestParam(value="hostname", required=true) String hostname,
			@RequestParam(value="guid", required=false) String guid
			) {
		logger.info("------------------------------------------");
		logger.info("--- APP NAME : /guid");
		logger.info("--- PARAM [hostname] : {}", hostname);
		logger.info("--- PARAM [guid] : {}", guid);
		
		String retValue = "";
		hostname = hostname.toUpperCase();
		if ( guid == null ) {
			retValue = guidSvc.createGuid(hostname);
		} else {
			retValue = guidSvc.nextGuid(guid, hostname);
		}
		logger.info("------------------------------------------");
		return retValue;
	}
	
	/**
	 * 2) Sequence 생성 
	 *    - 업무요건상 하나의 일련번호를 다수의 서버에서 증번하여 획득이 필요합니다. 
	 *      (채번 API 서버에서 업무 로직및 업무 오류에 따른 처리는 고려하지 않습니다.)
	 *    - 1 ~ 9,999,999,999 까지 사용 가능합니다.
	 *    - 순차적으로 증가해야 하며 일자별로 생성, 관리되어야 합니다.
	 *      (일자가 바뀌면 1부터 다시 시작해야 합니다.)
	 *    - 누락이 발생하지 않아야 합니다. 
	 *      (ex. 1 -> 2 -> 4처럼 중간번호(3)가 누락되는 경우가 없어야 합니다. 
	 *         / 호출하는 시스템오류로 누락되는 경우는 고려하지 않습니다.)
	 *    - 중복이 발생하지 않아야 합니다.
	 *    - 현재 생성된 Sequence 값을 확인할 수 있는기능이 필요합니다. 
	 *      (화면이 구현될 필요는 없으며 커멘드 레벨로도 가능합니다.)
	 * 
	 * @return
	 */
	@RequestMapping("/sequence")
	public String sequence() {
		logger.info("------------------------------------------");
		logger.info("--- APP NAME : /sequence");
		String retValue = seqSvc.next();
		logger.info("------------------------------------------");
		return retValue;
	}
	
	/**
	 * 마지막 채번된 시퀀스 조회.
	 * @return
	 */
	@RequestMapping("/current-sequence")
	public String currentSequence() {
		logger.info("------------------------------------------");
		logger.info("--- APP NAME : /current-sequence");
		String retValue = seqSvc.current();
		logger.info("------------------------------------------");
		return retValue;
	}

}
