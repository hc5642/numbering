# numbering
카카오뱅크 전행 채번 API 프로젝트


# 어플리케이션 기동 스크립트를 작성해주시고, 기동방법을 기재해주세요

* (기동 스크립트) java -jar api-numbering-0.0.1-SNAPSHOT.jar - 최초 기동시 DB가 자동으로 같은 경로에 생성되게 구성했습니다.(DB파일명:  sqliteLocal.db)
* (프로그램소스 README.md) https://github.com/hc5642/numbering/blob/main/README.md
* (프로그램소스 clone URL) https://github.com/hc5642/numbering.git


# 구현하신 채번 API 서버의 최대 처리량을 산정하고, 그 근거를 함께 제출해주세요.

* (톰캣성능) 스프링부트 FW에 자체 내장된 톰캣의 기본스레드개수 200개 → 응답시간 1초기준 초당 200건 처리가능 
* (실제성능) JMETER를 이용하여 'GUID 채번요청 API' 부하테스트 실시 (200유저 10건) > 평균 초당 52건 처리(2.4%오류율: db락 오류)
* (실제성능) JMETER를 이용하여 '전행 시퀀스번호 채번요청 API' > 평균 초당 37건 처리(0%오류율 : synchronized 옵션)


# 가용성 확보를 위한 구성방안을 제출해주세요

* 채번 API 서버는 전행 모든 어플리케이션에서 공통적으로 참조하는 서버로써 가용성이 중요
* 최소 2중화 이상의 물리적노드 분리를 통해 인스턴스를 분산 구성하고
* L4 스위치를 통한 로드 발란싱을 실시(least-connection)
* 실환경에 구성할때 DB서버는 별도 노드에 1대로 구성
* 오라클 DBMS환경의 경우 동시성제어를 위한 쿼리 튜닝필요(SELECT FOR UPDATE WAIT 5 등)


# 테스트 방법도 함께 작성해주세요

* 신규 GUID 채번요청 API  - curl "http://localhost:8081/guid?hostame=MACBOOKAIR"
* GUID 연속번호 채번 요청 - curl "http://localhost:8081/guid?guid=22070912481425IR.LOCAL3443IR00&hostname=MACBOOKAIR"
* 전행 시퀀스 번호 채번요청 API - curl "http://localhost:8081/sequence"
* 전행 시퀀스 번호 채번 현황 조회 API - curl "http://localhost:8081/current-sequence"


# 오픈소스와 사용한 명세, 사용목적 등을 명시해주세요

* 스프링부트 2.7.1 - API 어플리케이션 구성을 위한 기초 FW (톰캣내장)
* SQLite 3.36.03 - 오픈소스 DBMS


# 개발 후기

* 내부계약번호와 같은 채번락 이슈는 단순히 synchronized 옵션으로 거래를 wait 시키는 방법만으로는 해결할 수 없다고 생각합니다.
* 실환경에서는 비즈니스 레벨의 옵션과 DBMS레벨의 옵션, 어플리케이션레벨의 옵션 등을 복합적으로 활용하여 상황에 따른 종합적인 제어가 필요할 것이라 생각합니다.
