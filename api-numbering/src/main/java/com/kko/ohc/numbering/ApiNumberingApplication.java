package com.kko.ohc.numbering;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 채번 API
 * 실행 방법 : java -jar api-numbering-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
 * @author hyonchuloh
 *
 */
@SpringBootApplication
public class ApiNumberingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiNumberingApplication.class, args);
	}

}
