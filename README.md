# [MBTI] 실시간 업무 협업 플랫폼

```
✅ 실시간 업무 협업 플랫폼입니다. 핵심 서비스는 일정 관리, 출퇴근 관리, 협업 프로젝트 관리, 실시간 알림 및 채팅 등이 있습니다. 
```

---

![MBTI](https://github.com/zilyun/Final_Project/assets/40315922/e5f6b111-d665-4fa6-8680-5ff5408f0a13)

---

- [💡 서비스 배경 및 목표](#💡-서비스-배경-및-목표)
- [🛠️ 기술 스택](#🛠️-기술-스택)
- [🗺️ 서버 구조](#🗺️-서버-구조)
- [🗂️ 패키지 구조](#🗂️-패키지-구조)
- [🔥 기술적 개선 및 고려](#🔥-기술적-개선-및-고려)

---

## 💡 서비스 배경 및 목표

IT 취업 시장의 기준이 점점 높아지고 있으며, 이러한 흐름 속에서 실무적인 역량이 더욱 중요해지고 있습니다.</br>
신뢰할 수 있는 현업자의 직,간접적 경험 공유에 대한 필요성과 수요를 확인했습니다.

Anchor 서비스의 목표는 다음과 같습니다.<br>

- 목표 1. 재직중 혹은 재직했던 회사의 이메일 인증을 통해 신뢰성 있는 멘토를 만날 수 있도록 합니다.</br>
- 목표 2. 실시간 알림, 정산과 같은 기능을 통해 서비스 이용에 대한 사용자 편의성을 확보합니다.</br>
- 목표 3. 원활한 서비스 운영을 위한 기술적 요소를 적용하고 개선합니다. (ex. DB 이중화)

## 🛠️ 기술 스택

| 분류       | 기술명                                                                        |
|----------|----------------------------------------------------------------------------|
| BackEnd  | Java, Spring (Boot, Security, JPA), QueryDsl, Junit, Mockito, Redis, MySql |
| FrontEnd | HTML, Javascript, Thymeleaf                                                |
| DevOps   | nGrinder, Jmeter, EC2, RDS, S3, CodeDeploy, GithubAction                   |
| Tools    | IntelliJ, Gradle, Maven                                                    |

## 🗺️ 서버 구조

![CICD](https://github.com/zilyun/Final_Project/assets/40315922/cd2532f4-a1df-440b-860a-792e963a53c4)

## 💾 DB 구조

![DB구조](https://github.com/zilyun/Final_Project/assets/40315922/61cb8933-8e20-41c8-b8bd-9c60da76f08f)

## 🗂️ 패키지 구조

```
- com.anchor
    - domain
        - api
            - controller
              - request
            - service
              - response
        - domain
            - (Entity, Type Object)
            - repository
    - global
        - config
        - exception
        - util
        - (...)
```

## 🔥 기술적 개선 및 고려

```
다음과 같은 구조로 작성돼 있으니 참고부탁드립니다. 🙇

- 상황 및 목표 [link]
    - 목표 달성을 위한 행동
        - 결과 및 추가사항
```

### 홈페이지 조회 성능 개선 [[적용 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/domain/mentoring/api/service/MentoringService.java#L281-L292) / [설정 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/global/config/CacheConfig.java#L19C1-L39C4)]

- `Caffeine Cache`를 도입해 10개의 인기 멘토링과 태그 조회 시 캐싱 처리

  <details>
  <summary>홈페이지 조회에 대한 부하테스트 결과, 캐싱 미적용 대비 약 80%의 TPS 성능 향상</summary>
  <div>
      <h4>[Ngrinder]</h4>
      <span>Cache 미적용</span>
      <img src="readme/image/cache/ngrinder_nocache.png">
      <span>Cache 적용</span>
      <img src="readme/image/cache/ngrinder_cache.png">
  </div>
  <div>
      <h4>[Jmeter]</h4>
      <span>Cache 미적용</span>
      <img src="readme/image/cache/jmeter_nocache_1.png">
      <img src="readme/image/cache/jmeter_nocache_2.png">
      <span>Cache 적용</span>
      <img src="readme/image/cache/jmeter_cache_1.png">
      <img src="readme/image/cache/jmeter_cache_2.png">
  </div>
  </details>

### 멘토 이메일 발송 응답속도 개선 [[적용 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/global/mail/AsyncMailSender.java#L25C1-L37C4) / [설정 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/cfe5d2ff253192c0d2cbe4a01d12be677f6ca6f5/src/main/java/com/anchor/global/config/AsyncConfig.java#L22C1-L32C4)]

- 멀티 스레딩을 이용한 `@Async 비동기` 처리
  <details>
  <summary>테스트 결과, 클라이언트 기준 응답시간을 약 3s에서 100ms로 단축</summary>
      <h4>[비동기 처리 전]</h4>
      <img src="readme/image/async/sync_rt.png">
      <img src="readme/image/async/async_rt.png">
      <h4>[비동기 처리 후]</h4>
      <img width="388" alt="다운로드" src="https://github.com/Team-RecruTe/Anchor-Service/assets/58262954/9c9eba96-11d0-4d0d-9a62-09bf1f744b74">
  </details>
- 비동기 작업 스레드의 로깅을 위해 `@Around`를 적용
  <details>
  <summary>테스트 결과, 비동기 작업에 대한 로그 확인</summary>
      <img src="readme/image/async/mail_log_1.png">
      <img src="readme/image/async/mail_log_2.png">
  </details>

### 멘토링 검색 정확도, 정밀도 개선 [[적용 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/domain/mentoring/domain/repository/custom/QMentoringRepositoryImpl.java#L190C1-L223C2) / [설정 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/global/config/CustomFunctionContributor.java#L9C1-L21C2)]

- `Full Text Index(ngram parser)`를 적용해 키워드가 일치하는 정도를 수치화
    - like + wildcard 검색 대비 정확하고 정밀한 검색 결과 반환
- `ngram_token_size` 옵션 값을 3(default)에서 2로 조정
    - 2글자 이상부터 검색 가능하도록 변경

### DB에 대한 부하 분산 [[설정 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/global/db/DataSourceConfig.java#L28C1-L125C2) / [구성 패키지](https://github.com/Team-RecruTe/Anchor-Service/tree/develop/src/main/java/com/anchor/global/db)]

- 로컬/배포 환경에서 `DB 서버 이중화` 구성

    - 로컬 환경: MySQL DB 이중화 (Binary Log 기반)
    - 배포 환경: Aurora DB 이중화 (Page 기반)

- Master-Slave DB 간의 `Write/Read 쿼리 분산` 적용

    - 옵션1. @Transactional의 readOnly 속성을 이용한 쿼리 분산
    - 옵션2. @RouteDataSource의 dataSourceType 속성을 이용한 쿼리 분산
    - (@Transactional: 스프링 어노테이션 / @RouteDataSource: 커스텀 어노테이션)

- 추가 고려사항. 고가용성 확보를 위해서 Master DB 장애에 대한 대비책 필요

### Fetch Join / Pagination 동시 수행 시 메모리 누수 개선 [[적용 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/domain/mentoring/domain/repository/custom/QMentoringRepositoryImpl.java#L62C1-L92C1)]

- Fetch Join과 Pagination을 동시에 수행하던 단일 쿼리를 `이중 쿼리`로 분리
    - Fetch Join과 Pagination을 별도로 수행해 메모리 누수 문제 해결

### 실시간 알림 기능 구현 [[적용 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/domain/notification/api/service/NotificationService.java#L40C1-L109C4) / [구성 패키지](https://github.com/Team-RecruTe/Anchor-Service/tree/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/global/redis/message)]

- 네트워크 자원을 고려해 `Server Sent Event` 스펙으로 클라이언트에게 실시간 알림 전송
    - 웹 페이지 체류시간을 고려해 SSE Timeout 값을 60s로 지정
- 서버 분산을 고려해 `Redis Pub/Sub`을 이용해 실시간 알림 이벤트 발행 및 수신 처리
    - 이벤트 발행자와 수신자가 연결된 서버가 다르더라도 알림 수신 가능

### 멘토링 신청자 수 동시성 제어 [[적용 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/global/redis/lock/RedisLockFacade.java#L20C1-L41C4) / [구성 패키지](https://github.com/Team-RecruTe/Anchor-Service/tree/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/global/redis/lock)]

- 서버 분산을 고려해 `Facade 패턴`과 `Redis Lock`을 이용해 동시성 제어 구현
    - pub/sub을 이용해 Lock 획득을 시도하는 RedissonClient를 통해 CPU 낭비 방지

### 멘토링 신청시간 중복선택 방지 [[적용코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/global/redis/lock/RedisLockFacade.java#L43C1-L61C4) / [구성패키지](https://github.com/Team-RecruTe/Anchor-Service/tree/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/global/redis/lock)]

- 서버 분산을 고려해 `Facade 패턴`과 `Redis Lock`을 이용해 동시성 제어 구현
    - pub/sub을 이용해 Lock 획득을 시도하는 RedissonClient를 통해 CPU 낭비 방지
- 신청시간 잠금 이후 서버를 이탈하는 로직으로 락 소유권에 대한 추적이 어려울 것이라 판단, [Key,Value] 타입의 데이터로 신청시간 잠금 진행
    - 커서 기반 검색명령어 `scan`을 사용해 Redis 서버의 작업을 중단시키지 않도록 구현

### S3 저장소 내 불필요한 이미지 삭제 자동화 [[적용 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/domain/image/api/service/ImageService.java#L33C3-L40C4)]

- 트래픽이 적은 시간을 고려해 `@Scheduled`를 이용해 이미지 삭제를 요청하는 스케줄링 구현
    - 매일 새벽 3시에 이미지 삭제 요청 스케줄링 동작

### 이미지 파일의 효율적인 관리 [[적용 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/java/com/anchor/global/valid/CustomValidatorRegistry.java#L13C1-L44C2) / [구성 패키지](https://github.com/Team-RecruTe/Anchor-Service/tree/develop/src/main/java/com/anchor/global/valid)]

- `@ValidFile` 커스텀 어노테이션을 통해 빈 이미지 파일 요청에 대한 검증 처리
    - ConstraintValidator 클래스를 구현해 Custom Validator 직접 정의
- [이미지 요청 시 파일당 10MB 용량 제한](https://github.com/Team-RecruTe/Anchor-Service/blob/fe37c7b7a98d0511150b2ba4dd09574adfb07e82/src/main/resources/application.yml#L22C1-L25C29)

### 로그 메세지 최적화 [[설정 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/develop/src/main/resources/log4j2/log4j2.yml)]

- 속도가 빠른 `AsyncLogger` 옵션을 제공하는 Log4j2 라이브러리를 도입해 로깅 처리
    - Appender: console-appender와 rolling-file-appender 적용
    - Logger: rolling-file-appender에 대해서 AsyncLogger 부분 적용

### 정산프로세스 및 멘토링 완료 자동화 [[적용코드1](https://github.com/Team-RecruTe/Anchor-Service/blob/f4bb891e3ac535e991525b07b98eb2f89ddcf167/src/main/java/com/anchor/domain/mentoring/api/service/MentoringScheduler.java#L19C1-L27C4) / [적용코드2](https://github.com/Team-RecruTe/Anchor-Service/blob/f4bb891e3ac535e991525b07b98eb2f89ddcf167/src/main/java/com/anchor/domain/payment/api/service/PayupScheduler.java#L18C1-L21C4)]

- 트래픽이 적은 시간을 고려해 `@Scheduled`를 이용해 정산 및 멘토링 완료 스케줄링 구현
    - 매월 1일 새벽 3시에 정산 스케줄링 동작
    - 매일 새벽 2시 일주일이 지난 멘토링 자동 완료 스케줄링 동작

### 병렬처리를 통한 작업시간 개선[[적용코드](https://github.com/Team-RecruTe/Anchor-Service/blob/085d35a338599f374cc3a3d6bab54ab4bb0b54ac/src/main/java/com/anchor/domain/payment/api/service/PayupService.java#L53)]

- `ParallelStream`을 적용해 병렬처리로 정산프로세스 작업시간 개선
  <details>
  <summary>테스트 결과, 순차처리 대비 작업시간 90% 개선</summary>
      <img src="readme/image/stream/stream_elapsed_time.png">
      <img src="readme/image/stream/parallel_stream_elapsed_time.png">
  </details>
- `ParallelStream`간 **Thread DeadLock** 방지를 위해 커스텀 `ForkJoinPool` 설정

### CI/CD 환경 구축 [[설정 코드](https://github.com/Team-RecruTe/Anchor-Service/blob/develop/.github/workflows/cicd.yml)]

- `Github Actions`, `AWS CodeDeploy`, `S3`를 이용해 테스트-빌드-배포 자동화
    - 빌드 서버는 Github Runner 서버 사용

### 회원 정보 관리 포인트 최소화 [[구성 패키지](https://github.com/Team-RecruTe/Anchor-Service/tree/develop/src/main/java/com/anchor/global/auth)]

- `OAuth 2.0 & OpenID Connect`를 이용해 Naver, Google 인증 기능 구현
    - 인증 회원의 정보는 Redis 분산 세션에 SessionUser 객체를 저장

### RestClient 추가 설정 및 에러핸들링[[적용코드](https://github.com/Team-RecruTe/Anchor-Service/blob/f4bb891e3ac535e991525b07b98eb2f89ddcf167/src/main/java/com/anchor/global/util/PaymentClient.java#L125C1-L136C4) / [설정코드](https://github.com/Team-RecruTe/Anchor-Service/blob/f4bb891e3ac535e991525b07b98eb2f89ddcf167/src/main/java/com/anchor/global/config/RestClientConfig.java#L19)]

- Connection Pool 설정으로 외부 API 호출시 멀티 스레딩 및 가용 스레드 수 제한
- Timeout 설정으로 가용 스레드 확보
- `exchange`메서드를 사용해 응답코드 별 에러 핸들링
- `@Retryable`으로 재시도 로직 추가
- 추가 고려사항. API 서버 장애로 인한 에러 발생시 서킷브레이커 패턴 도입 필요

### 예외 정의 및 예외발생 로그 관심사 분리[[구성패키지](https://github.com/Team-RecruTe/Anchor-Service/tree/develop/src/main/java/com/anchor/global/exception) / [설정코드](https://github.com/Team-RecruTe/Anchor-Service/blob/f4bb891e3ac535e991525b07b98eb2f89ddcf167/src/main/java/com/anchor/global/log/ExceptionLoggingAspect.java#L14C1-L18C1)]

- 어플리케이션에서 발생하는 예외를 새로 정의해 비즈니스 로직에서 발생하는 예외 가독성 향상
- 예외 추상화를 통한 에러 핸들링 유연성 확보
- AOP를 통한 예외 발생 로깅 코드 재사용성 증가

### Insert 쿼리 성능 개선

- `JPQL Bulk Insert`를 적용
  <details>
  <summary>1만건의 데이터 삽입에 대해, Insert 쿼리 대비 약 60%의 성능 개선</summary>
      <img src="readme/image/insert/image.png">
      <img src="readme/image/insert/image-1.png">
  </details>
