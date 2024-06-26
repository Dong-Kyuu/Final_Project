# [MBTI] 실시간 업무 협업 플랫폼

```
✅ 실시간 업무 협업 플랫폼입니다. 핵심 서비스는 일정 관리, 출퇴근 관리, 채팅 등이 있습니다. 
```

---

<img width="1000" alt="logo" src="https://github.com/zilyun/Final_Project/assets/40315922/e5f6b111-d665-4fa6-8680-5ff5408f0a13">

---

- [💡 서비스 배경 및 목표](#💡-서비스-배경-및-목표)
- [🛠️ 기술 스택](#🛠️-기술-스택)
- [🗺️ 서버 구조](#🗺️-서버-구조)
- [🔥 기술적 개선 및 고려](#🔥-기술적-개선-및-고려)
- [📗 기능 살펴보기](#📗-기능-살펴보기)
- [🎥 시연영상](#🎥-시연영상)
---

## 💡 서비스 배경 및 목표

IT 인프라가 많이 보급되고 있으며, 이러한 흐름 속에서 업무의 생산성을 증대시키는 것이 더욱 중요해지고 있습니다.</br>
신뢰할 수 있는 실시간 업무 협업 플랫폼에 대한 필요성과 수요를 확인했습니다.

MBTI 서비스의 목표는 다음과 같습니다.<br>

- 목표 1. 실시간 업무 공유 및 협업을 원하는 기업과 팀을 위해 설계된 웹사이트를 구현합니다.</br>
- 목표 2. 실시간 알림, 채팅과 같은 기능을 통해 서비스 이용에 대한 사용자 편의성을 확보합니다.</br>
- 목표 3. 원활한 서비스 운영을 위한 기술적 요소를 적용하고 개선합니다. (ex. Redis 캐싱)

## 🛠️ 기술 스택

| 분류       | 기술명                                                                   |
|----------|----------------------------------------------------------------------------|
| BackEnd  | Java, Spring (Boot, Security), Junit, Redis, MySql, rabbitMQ               |
| FrontEnd | HTML, Javascript, Thymeleaf                                                |
| DevOps   | nGrinder, Scouter, EC2, RDS, S3, Docker, Jenkins                           |   
| Tools    | IntelliJ, Gradle                                                           |

## 🗺️ 서버 구조

![CICD](https://github.com/zilyun/Final_Project/assets/40315922/cd2532f4-a1df-440b-860a-792e963a53c4)

## 💾 DB 구조

![DB구조](https://github.com/zilyun/Final_Project/assets/40315922/61cb8933-8e20-41c8-b8bd-9c60da76f08f)

## 🔥 기술적 개선 및 고려

```
다음과 같은 구조로 작성돼 있으니 참고부탁드립니다. 🙇

- 상황 및 목표 [link]
    - 목표 달성을 위한 행동
        - 결과 및 추가사항
```

### 채팅방 조회 성능 개선 [[적용 코드]() / [설정 코드]() / [결과](https://velog.io/@ziru/28.Redis-%EC%84%B1%EB%8A%A5-%ED%85%8C%EC%8A%A4%ED%8A%B8)]

- `Redis`를 도입해 채팅방 입장 시 캐싱 처리

  <details>
  <summary>부하테스트 결과, 캐싱 미적용 대비 약 3.5배의 TPS 성능 향상</summary>
  <div>
      <h4>[Ngrinder]</h4>
      <span>Cache 미적용</span>
      <img src="https://github.com/zilyun/Final_Project/assets/40315922/b2da3659-9498-4caa-b082-dbf88e68dbbe">
      <span>Cache 적용</span>
      <img src="https://github.com/zilyun/Final_Project/assets/40315922/bc27e31a-37c9-452d-903a-9cee8b56e53e">
  </div>
  <div>
      <h4>[Scouter]</h4>
      <span>Cache 미적용</span>
      <img src="https://github.com/zilyun/Final_Project/assets/40315922/ebd08602-df42-4936-89be-8767645791e1">
      <span>Cache 적용</span>
      <img src="https://github.com/zilyun/Final_Project/assets/40315922/d1d6c828-c932-4c78-910c-56b78430ab72">
  </div>
  </details>

### 메시지 대규모 트래픽 발송 응답속도 개선 [[적용 코드]() / [설정 코드]()]

- `RabbitMQ`를 도입하여 대규모 메시지를 효율적으로 처리
  <details>
  <summary>RabbitMQ의 메시지 브로커로 메시지를 비동기 처리하여 대규모 트래픽을 처리</summary>
      <h4>[RabbitMQ]</h4>
      <img src="https://github.com/zilyun/Final_Project/assets/40315922/13a2cfdf-acae-4fc3-b1b8-5c2e5eaac56d">
  </details>

### 채팅방 실시간 알림 기능 구현 [[적용 코드]() / [설정 코드]()]

- 네트워크 자원을 고려해 `Server Sent Event` 스펙으로 클라이언트에게 채팅방 실시간 알림 전송
    - 채팅 메시지 생성, 채팅방(멀티,1대1) 만들기, 채팅방 나가기 기능 사용 시 업데이트 실시간 알림

### 채팅 기록 검색 정확도, 정밀도 개선 [[적용 코드]()]

- `Full Text Index(ngram parser)`를 적용해 키워드가 일치하는 정도를 수치화
    - like + wildcard 검색 대비 정확하고 정밀한 검색 결과 반환

### S3 저장소 파일 업로드 / 다운로드 [[적용 코드]() / [설정 코드]()]

- 채팅 기능에서 파일 업로드 시 `S3` 저장소 사용

### 문의 리스트 조회 성능 개선 [[적용 코드]() / [결과](https://velog.io/@ziru/26.%EB%B0%B1%EC%97%94%EB%93%9C-%EA%B0%9C%EB%B0%9C%EC%9E%90-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0-%EC%B4%88%EC%84%9D-%EB%8B%A4%EC%A7%80%EA%B8%B0)]

- `Caffeine Cache`를 도입해 문의 리스트를 조회 시 캐싱 처리

  <details>
  <summary>부하테스트 결과, 캐싱 미적용 대비 약 2배의 TPS 성능 향상</summary>
  <div>
      <h4>[Ngrinder]</h4>
      <span>Cache 미적용</span><br>
      <img alt="미적용" src="https://github.com/zilyun/Final_Project/assets/40315922/4c713b8a-6f0e-41a8-9eff-048813461577"><br>
      <span>Cache 적용</span><br>
      <img alt="적용" src="https://github.com/zilyun/Final_Project/assets/40315922/3893c591-8fab-48e5-baed-519efe1ffd3e">
  </div>
  </details>

### CI/CD 환경 구축 [[설정 코드]()]

- `Jenkins`, `Docker`, `Amazon EC2, S3, RDS`를 이용해 테스트-빌드-배포 자동화
    - 빌드 서버는 Amanzon EC2 서버 사용




## 기능 살펴보기 📗 

### 채팅창 이동

<img src="https://github.com/zilyun/Final_Project/assets/40315922/aed93ddb-57e9-415e-aeaa-647ba733f74c" width="1000" height="600"/> 

### 사원 목록 & 사원 검색

<img src="https://github.com/zilyun/Final_Project/assets/40315922/242ab80e-93b9-443f-8604-71941adbafaa" width="350" height="600"/> 

### 채팅방 목록

<img src="https://github.com/zilyun/Final_Project/assets/40315922/18d3ffb5-d682-403f-83be-b6b76bd6dbea" width="350" height="600"/> <img src="https://github.com/zilyun/Final_Project/assets/40315922/0c898cc9-a210-4058-a477-9cc9be49d84d" width="350" height="600"/>

### 채팅방 생성 - 1대1 채팅방

<img src="https://github.com/zilyun/Final_Project/assets/40315922/c8380227-4445-4257-bbe2-bf68659226a0" width="350" height="600"/>  <img src="https://github.com/zilyun/Final_Project/assets/40315922/8cc33c01-ef52-4611-a284-dd7a2417dbd5" width="350" height="600"/> 

### 채팅방 생성 - 그룹 채팅방

<img src="https://github.com/zilyun/Final_Project/assets/40315922/b043aff7-2f5e-4b70-be43-40d75c21d65e" width="350" height="600"/>  <img src="https://github.com/zilyun/Final_Project/assets/40315922/7f31c4c9-f264-4344-8585-c4eb1350b1da" width="350" height="600"/> 

### 채팅방 - 이모티콘

<img src="https://github.com/zilyun/Final_Project/assets/40315922/d752bd4b-edf7-45e9-afd4-6045b80db570" width="350" height="600"/>

### 채팅방 - 파일전송

<img src="https://github.com/zilyun/Final_Project/assets/40315922/07950189-4400-4f45-876c-da60259b7ba0" width="350" height="600"/>

### 채팅방 - 긴급공지

<img src="https://github.com/zilyun/Final_Project/assets/40315922/071cc8c4-2fb1-484b-b17b-002f4429a309" width="300" height="600"/>  <img src="https://github.com/zilyun/Final_Project/assets/40315922/95905233-2e24-4ed0-b1a0-3f06f1b771be" width="300" height="600"/> <img src="https://github.com/zilyun/Final_Project/assets/40315922/0564f937-2ab9-4697-953c-9e5a52d8efd2" width="300" height="600"/> 

### 채팅 UI

<img src="https://github.com/zilyun/Final_Project/assets/40315922/4924e46e-365d-4ae9-8772-1e8650eebbbe" width="350" height="600"/> <img src="https://github.com/zilyun/Final_Project/assets/40315922/7fab4a61-f401-4800-97af-28d3c6d6624b" width="350" height="600"/> 

### 채팅 기록 검색

<img src="https://github.com/zilyun/Final_Project/assets/40315922/69daedf8-3ec7-4ece-9422-1a6cffc7c9a3" width="350" height="600"/> 

### 채팅 1일 데이터 불러오기

<img src="https://github.com/zilyun/Final_Project/assets/40315922/d0b15a12-5303-4245-878b-8e0e567a8ec2" width="350" height="600"/> 

### 채팅방 메뉴 

<img src="https://github.com/zilyun/Final_Project/assets/40315922/4c2044c9-34fc-4bc0-a463-3f95ad8b3366" width="350" height="600"/> 

### 채팅 인원 추가 & 삭제

<img src="https://github.com/zilyun/Final_Project/assets/40315922/a31ec9ce-0b93-436f-b92c-8b8ba2213ec9" width="350" height="600"/> <img src="https://github.com/zilyun/Final_Project/assets/40315922/b221a359-99be-4196-b104-9ca331c01969" width="350" height="600"/> 

## 시연영상 🎥 
- [시연 영상 - 채팅](https://www.youtube.com/watch?v=6PVh-9BGTZ0)
- [시연 영상 - 문의](https://www.youtube.com/watch?v=kz-Ty1NQr24)
- [시연 영상 - 성능테스트](https://www.youtube.com/watch?v=Ut8ohkSXVN4)
