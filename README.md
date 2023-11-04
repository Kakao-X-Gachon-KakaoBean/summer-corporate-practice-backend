# 카카오빈 실무 프로젝트

카카오 엔터프라이즈 SW 아카데미 2기 실무 프로젝트 백엔드 레포지토리입니다. 

## 기술 스택

* Java 11, Gradle, Jacoco
* Spring Boot
* Spring Data JPA
* Spring Security
* Junit, Mockito
* Spring Rest Docs
* Redis, RabbitMQ
* Kakao Cloud

<br>

## 멀티 모듈 전략

* api: 클라이언트와 소통하기 위한 인터페이스 계층
* common: 모든 모듈에서 사용할 util 클래스 계층
* independently-system: 프로젝트에 직접적으로 엮이지 않은 시스템 모듈 계층
* core: 영속성 계층과 비즈니스 도메인 클래스가 포함된 핵심 계층

<br>

## 테스트 전략

* api 모듈 테스트
  * Controller 단위 테스트 : `mockMVC` 프레임워크를 사용, `Spring Rest Docs` 생성과 병행하였다.
  * 인수 테스트 : `RestAssured` java 테스팅 라이브러리 사용하여 API 테스트 자동화하였다.
    * CRUD 성공 케이스는 모두 작성하였다.
    * DB, Message Queue 모두 검증하였다.

* core 모듈 테스트
  * 통합 테스트 : Service layer + Persistence layer
    * 예외 케이스를 포함해 생성, 삭제, 수정 등 중요 비즈니스 로직에 대한 케이스를 작성하였다.
    * 데이터베이스의 값을 검증. Message Queue는 Mocking 처리
    * 테스트 데이터 삭제 전략 변경 : @Transactional 롤백 테스트 → @BeforeEach 구문에서 명시적 롤백 진행
    * 변경 결과 : 이벤트 발행시 Transaction 전이가 되지 않아 실질적인 비즈니스 코드가 테스트 되지 않는 현상을 해결하였다.
  * 단위 테스트
    * `Mockito`를 사용해 외부 의존성 격리
    * 비즈니스 로직 1개를 수행하는 것이 최소 테스트 단위

<br>

## 테스트 커버리지 

97% 이상의 라인 커버리지 달성

![api](https://github.com/Kakao-X-Gachon-KakaoBean/summer-corporate-practice-backend/assets/76802855/b1b2cc4c-97ee-4664-b191-95caff2ae0dd)

<br>

![core](https://github.com/Kakao-X-Gachon-KakaoBean/KakaoBean-Backend/assets/76802855/7b5f9ceb-dba6-4a15-b11a-bd8b79f75b34)

