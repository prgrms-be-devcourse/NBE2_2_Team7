
## 훈민정음 2.0
외국인 대상 한국어 학습 및 모임 서비스
훈민정음 2.0은 세종대왕의 애민정신과 훈민정음 창제의 의미를 이어받아, 한국어를 널리 전파하고자 하는 마음으로 개발된 한국어 교육 서비스입니다. 이 프로젝트는 한국어 학습자들에게 효율적이고 즐거운 학습 경험을 제공하기 위해 다양한 기능을 제공합니다.

## 기능 목록



- **한국어 단어 학습**: 다양한 주제와 난이도의 단어를 학습하며 어휘력을 향상시킬 수 있습니다.
- **위치 기반 모임 찾기**: 주변에서 진행되는 한국어 학습 모임을 쉽게 찾아 참여할 수 있습니다.
- **자유로운 1:1 채팅 기능**: 다른 학습자나 튜터와 자유롭게 대화하며 실시간으로 한국어 실력을 향상시킬 수 있습니다.

## 기능 사용



프론트 영상

## 기술 스택

### Backend <br/>
![Java](https://img.shields.io/badge/Java%2017-007396?style=flat-square&logo=java&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=flat-square&logo=spring&logoColor=white)
<img src="https://img.shields.io/badge/Spring%20Boot%203.3.4 -6DB33F?style=flat-square&logo=Spring%20Boot&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring%20Data%20JPA%203.3.2-6DB33F?style=flat-square&logo=&logoColor=white"/>
<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=Spring Security&logoColor=white">
<img src="https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=JUnit5&logoColor=white">
<img src="https://img.shields.io/badge/Lombok-FF0000?style=flat-square&logo=Lombok&logoColor=white">
<img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=JSON-Web-Tokens&logoColor=white">
<img src="https://img.shields.io/badge/Gradle-0?style=flat-square&logo=gradle&logoColor=white&color=%2302303A">

### DB / Infra
<img src="https://img.shields.io/badge/MySQL%208.0.39-4479A1?style=flat-square&logo=MySQL&logoColor=white">  <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=Redis&logoColor=white"> <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=flat-square&logo=docker&logoColor=white">


### 성능테스트
<img src="https://img.shields.io/badge/JMeter-D22128?style=flat-square&logo=Apache-JMeter&logoColor=white">

### 문서/협업툴
<img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=notion&logoColor=white"> <img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white">
<img src="https://img.shields.io/badge/IntelliJ IDEA-4A154B?style=flat-square&logo=intellijidea&logoColor=white">
<img src="https://img.shields.io/badge/Swagger-0?style=flat-square&logo=Swagger&logoColor=white&color=%2385EA2D">

## 구조
전체 시스템 구조
![image](https://github.com/user-attachments/assets/1181647e-2c4f-4a40-81f0-3cc22a8c3922)

채팅 시스템 구조
![image](https://github.com/user-attachments/assets/e2844e3c-09db-4d9a-b6b2-04b132e37f21)
<details>
<summary>채팅 기능 구현 설명(클릭하여 펼치기/접기)</summary>

## 1. 주로 실시간 채팅에 사용되는 기술소개

### 1. **WebSocket**

= 양방향 통신을 위한 기능, 실시간으로 데이터를 전송하기 위한 기본적인 기술

- **장점**:
    - 비교적 간단 구현
- **단점**:
    - 단일 서버연결
    - 사용자 세션관리에 대한 불편함

### 2. **WebSocket + STOMP**

= 웹소켓 기술에 STOMP 기술을 곁들여 보다 편리한 메세지 전송을 구현하게 함. PUB/SUB 을 붙인 엔드포인트로 간단히 수신자/송신자를 구별. Stomp의 경우 다른 브로커들과 (RebbitMQ)  연동이 쉬워 유연성이 높다.

- **장점**:
    - 채팅방 등의 Pub/Sub 모델을 쉽게 구현.

### 3. **WebSocket + Redis Pub/Sub**

= Redis의 Pub/Sub 기능을 이용하여 채팅방과 같은 실시간 메시지 전송 기능을 구현..

- **장점**:
    - Redis를 이용한 빠른 메시지 처리.
    - 서버간 데이터 전송으로 서버 확장성을 기여
- **단점**:
    - 메시지 전송 실패 시 메시지 유실

### 4. **WebSocket + Kafka**

= Apache Kafka를 메시지 브로커로 사용하여 대규모 분산 시스템에서 실시간 메시지 스트리밍을 구현.

Redis와 마찬가지로 서버간 데이터 전달 가능.

- **장점**:
    - 대용량 메시지 처리를 위한 높은 확장성과 내구성.
    - 메시지 실패해도 영구적 저장
- **단점**:
    - Kafka 구현 시 설정관리 및 운영이 복잡하다.

### 5. **WebSocket + RabbitMQ**

= AMQP(Advanced Message Queuing Protocol) 메시지 브로커로, WebSocket과 결합하여 실시간 채팅 구현.

- **장점**:
    - 설정이 상대적으로 간단하다.
    - 서버간 데이터 전송 가능
- **단점**:
    - Kafka에 비해 대규모 메시지 처리에서는 덜 효율적일 수 있음.

결론 = Websocket+ stomp는 다른 제품들에 비해 단순한 텍스트 기반 프로토콜로, 빠르고 경량화된 메시지 전송을 지원하여 선택하였다. 그리고 redis의 경우 채팅 메세지를 인메모리 영역에서 저장하여 빠른 데이터 처리가 가능하고, 다중 서버 운영 시 서버 확장에도 대비할 수 있어 유리하다.

하지만, 카프카와 같이 메시지 단위를 넘어 이벤트 단위로 넘어가거나, 레빗엠큐와 같이 메시지 처리 기능을 구현하고 싶다면 stomp 기술을 넘어 위 2가지 기술을 사용할 것을 고려 해야한다. 

---

## 2. 구현 기능에 대한 정보

<참고 이미지>

![최종1](https://github.com/user-attachments/assets/9c99f7df-208b-4dae-ac1f-8bae2fb44bf5)

---

### 1. WebSocket

<aside>
🌋

**=webSocket의 경우 사용자와 서버 사이 양방향 통신을 TCP 계층에서 가능하게 하는 역할을 한다.**

- **순서: 클라이언트는 헤더에 upgrade? 라는 요청을 보내고 서버는 사용자를 확인하고 upgrade 허가! 라는 응답을 보내준다. 그 이후 클라이언트의 upgrade된 상태를 헤더에 담아 다시 서버에 보내면 서버는 연결을 허락한다. 기본 http연결 3 hand shake 방식과 유사하다.**
</aside>

- 

![최종2](https://github.com/user-attachments/assets/3218299b-43b9-432d-b023-87811fd43db9)

![최종3](https://github.com/user-attachments/assets/73d08e81-a4c2-4a63-8d61-1b821534dde5)

### 2. Stomp

<aside>
🌋

**= Stomp는 Controller의 @MessageMapping 메서드에 한해서 /pub을(커스텀 가능) 붙여주는 기능을 담당한다. 이 엔드포인트 들이 붙은 데이터는 /sub을 받아 들이는 사용자에게 전송된다.**

**= connect 시 (방입장시) subscribe를 자동을 하도록 프론트에서 구현 필요**

</aside>

- <pub>-프론트 구현부(chatRoomComponent.js)

![최종4png](https://github.com/user-attachments/assets/55b18637-22d3-4a37-95d6-c2de13ca7e4e)

- <sub>-프론트 구현부(chatRoomComponent.js)

![최종5](https://github.com/user-attachments/assets/a6f2261e-c9c2-42a5-b283-4483ee3c223f)

### 3. Redis

<aside>
🌋

**= Redis의 경우 Topic을 설정하여 메시지들을 한 곳으로 모으는 역할을 한다. publisher는 pub/…/topic쪽으로 메세지를 보내고 그 이후 모인 메세지를 sub/…/topic 쪽으로 구독한 분들에게 메세지를 전달한다.**

</aside>

- Topic설정- 백엔드 구현부(ChatMessageService)

![최종6png](https://github.com/user-attachments/assets/f921e1e8-80af-484a-af2c-165098e2f7d4)

= 약간의 설명을 덧붙이자면, Topic은 개발자가 채널을 식별할 수 있도록 주면 된다. 나는 ChatRoomId를 Topic의 식별자로 두었고 Topic마다 다른 채널로 가게 두었다. 이렇게 하여 1채녈은 /1 ,  2채널은 /2 이렇게 Topic = 1, Topic = 2 로 되는곳으로 나뉘어 졌다. 단순히 ChatRoomId 만을 식별하기 위해 그리고 String으로 변환 시키기 위해 “” + chatRoomId 로 Topic을 설정하였다. (보안수준은 낮다.)

- Topic된 메세지들을 구독자들에게 전송 - 백엔드 구현부 (RedisSubScriber)

![최종7png](https://github.com/user-attachments/assets/fa11d08d-06b2-4050-bca8-9c6394d9633b)

= ChatRoomId가 붙은 (Topic) 메세지를 구독자들에게 전송하는 부분이다.

- 메세지 도착 / 수신 - 프론트 구현부(chatRoomComponent.js)

= 위 메세지 전송 부분과 수신 부분의 엔드 포인트가 같은 것을 확인할 수 있다.

![최종8png](https://github.com/user-attachments/assets/67fee430-fc75-4b29-8a74-d6d7edb23e06)

---

이 모든 과정을 거쳐 

![최종9png](https://github.com/user-attachments/assets/7de9f44a-910d-4cd9-8992-c6cdca928692)

![최종10png](https://github.com/user-attachments/assets/11774823-8be6-4068-95de-6911e7b4d669)

콘솔창에서 진행되는 것이다.

## 결론

= WebSocket+Stomp+Redis를 이용하여 실시간 채팅을 구현하였다. 가벼운 메세지 데이터 통신을 양방향으로 구현하였고, Stomp를 통해 편리하게 Sender/Receiver를 특정할 수 있었다. 그리고 Redis를 통해 여러 서버간 실시간 통신을 가능하게 했으며 pub/sub과 Topic/Channel 개념으로 사용자들에게 정확히 원하는 곳으로 메세지를 전송하고 수신하게 하였다.

- WebSocket+Stomp 조합은 스프링에 권장하는 방법이다. WebSocket의 경우 스프링 자체 성능이지만, Stomp는 오픈소스 프로토콜이다.
</details>

<details>
  <summary>📦 패키지 구조 (클릭하여 펼치기/접기)</summary>

├─ src<br/>
│  ├─ main <br/>
│  │  ├─ java <br/>
│  │  │  └─ com <br/>
│  │  │     └─ hunmin <br/>
│  │  │        └─ domain <br/>
│  │  │           ├─ config <br/>
│  │  │           ├─ controller <br/>
│  │  │           │  └─ advice     <br/>
│  │  │           ├─ dto<br/>
│  │  │           │  ├─ board <br/>
│  │  │           │  ├─ chat  <br/>
│  │  │           │  ├─ comment <br/>
│  │  │           │  ├─ member <br/>
│  │  │           │  ├─ notice <br/>
│  │  │           │  ├─ notification <br/>
│  │  │           │  ├─ page<br/>
│  │  │           │  └─ word<br/>
│  │  │           ├─ entity<br/>
│  │  │           ├─ exception<br/>
│  │  │           ├─ handler<br/>
│  │  │           ├─ json<br/>
│  │  │           ├─ jwt<br/>
│  │  │           ├─ pubsub<br/>
│  │  │           ├─ repository<br/>
│  │  │           │  └─ search<br/>
│  │  │           └─ service<br/>
│  │  └─ resources<br/>
│  │     ├─ static<br/>
│  │     │  ├─ images<br/>
│  │     └─ templates<br/>
│  │        └─ message<br/>
│  └─ test<br/>
│     └─ java<br/>
│        └─ com<br/>
│           └─ hunmin<br/>
│              └─ domain<br/>
│                 ├─ repository<br/>
│                 └─ service<br/>
└─ uploads

</details>

<details>
<summary>로그인 (클릭하여 펼치기/접기)</summary>
  
![image](https://github.com/user-attachments/assets/4e3c2e4c-2deb-4515-83b6-f901742ac990)


</details>

<details>
<summary>게시판 (클릭하여 펼치기/접기)</summary>

![image](https://github.com/user-attachments/assets/6e2df253-3810-477b-aaab-e1f70ac1bddf)


</details>

<details>
<summary>댓글 (클릭하여 펼치기/접기)</summary>

![image](https://github.com/user-attachments/assets/f18c3e47-5c45-47e5-8b5f-87c2766771cb)

</details>

<details>
<summary>단어학습 (클릭하여 펼치기/접기)</summary>

![image](https://github.com/user-attachments/assets/6a3f9cac-728a-4b52-8af5-8fc29396fc00)

</details>

<details>
<summary>공지사항 (클릭하여 펼치기/접기)</summary>

![image](https://github.com/user-attachments/assets/2346b9c0-8591-43e4-a4dc-6fa9c0058733)

</details>

<details>
<summary>알림 (클릭하여 펼치기/접기)</summary>

![image](https://github.com/user-attachments/assets/69e09a9f-e7a7-4d0f-a57b-80c7cc63c513)

</details>



## 다이어그램
### ERD
![image](https://github.com/user-attachments/assets/7fd35a00-f121-44e2-9c33-302baefb44bd)


###
<details>
<summary>클래스 다이어그램 (클릭하여 펼치기/접기)</summary>

![hunmin](https://github.com/user-attachments/assets/566f6dd3-dcaf-4e60-ab9e-0138137f2aa6)

</details>

<details>
<summary>유스케이스 다이어그램 (클릭하여 펼치기/접기)</summary>
  
![USECASE  ](https://github.com/user-attachments/assets/61339a9e-571f-4370-8dbf-33f72ad329d0)

</details>



## 팀원 소개

<table>
  <tr>
    <td>
        <a href="https://github.com/kang-ye-jin">
            <img src="https://avatars.githubusercontent.com/u/143896003?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/iam52">
            <img src="https://avatars.githubusercontent.com/u/131854898?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/Dom1046">
            <img src="https://avatars.githubusercontent.com/u/173169283?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/HanJae-Jae">
            <img src="https://avatars.githubusercontent.com/u/177859651?v=4" width="100px" />
        </a>
    </td>
    <td>
        <a href="https://github.com/DongWooKim4343">
            <img src="https://avatars.githubusercontent.com/u/106728608?v=4" width="100px" />
        </a>
    </td>
  </tr>
  <tr>
    <td><b>강예진</b></td>
    <td><b>오익수</b></td>
    <td><b>김동현</b></td>
    <td><b>한재재</b></td>
    <td><b>김동우</b></td>
  </tr>
  <tr>
    <td><b></b></td>
    <td><b></b></td>
    <td><b></b></td>
    <td><b></b></td>
    <td><b></b></td>
  </tr>
</table>


## 프로젝트 협업 규칙

<details>
<summary>Convention (클릭하여 펼치기/접기)</summary>
<aside>
💡

이슈 생성 → 브랜치 생성 → 해당 브랜치로 이동 → develop pull → 작업 중간중간 커밋 → 해당 이슈에 대한 작업이 다 완료되면 pr 생성

🚨 커밋 메시지도 템플릿을 지켜주세요 (커밋 메시지 push 전까지는 수정할 수 있어요)

🚨 헷갈리면 push를 멈춰 주세요

🚨 merge 시 충돌을 주의해 주세요 ❗️

🚨 main은 배포중인 브랜치이므로 pr은 develop 으로 부탁드려요

🚨 궁금한 점이 있다면 언제든 같이 해결해요 😊

</aside>

### [type]

- feat : 새로운 기능 구현
- mod : 코드 및 내부 파일 수정
- add : feat 이외의 부수적인 코드, 파일, 라이브러리 추가
- del : 불필요한 코드나 파일 삭제
- fix : 버그 및 오류 해결
- ui : UI 관련 작업
- chore : 버전 코드, 패키지 구조, 함수 및 변수명 변경 등의 작은 작업
- hotfix : 배포된 버전에 이슈 발생 시, 긴급하게 수정 작업
- rename : 파일이나 폴더명 수정
- docs : README나 Wiki 등의 문서 작업
- refactor : 코드 리팩토링
- merge : 서로 다른 브랜치 간의 병합
- comment : 필요한 주석 추가 및 변경

---

### issue

- 제목
    
    ```java
    [type] 작업 내용 간단히
    ```
    

### branch

<aside>
  
💡issue 안에서 바로 branch를 만들어주세요 ❗️

</aside>

```java
feature/#(이슈번호 앞에 붙여주세요!

ex) feature/#1-add-ipa-엔티티-설계
```

### commit message

```java
[type] 작업 내용 간단히

ex) 
[feat] ~~~한 기능 구현 
```

### PR

- 제목
    
    ```java
    [type] 작업 내용 간단히
    ```

### 패키지명

- 소문자

### 패키지 구조

com>도메인 명>www>

- config
- controller>advice
- dto
- entity
- exception
- repository>search
- security
- service
- jwt

= <Test 패키지는 main과 대칭>

- ctrl+shift+t
- 자바 컨벤션 준수

### 기타

- if 중괄호 필수
    
    ```java
    if (condition) {
        // 한 줄이라도
    }
    ```
    
- 클래스, 메서드 주석
    
    ```java
    //간단한 설명
    ```
    

- 로그 : 필요 시 규칙없이 작성
</details>

