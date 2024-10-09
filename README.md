
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

### 1. **WebSocket + STOMP + Redis Pub/Sub**

-  웹소켓 기술에 STOMP 기술을 곁들여 보다 편리한 메세지 전송을 구현하게 함. PUB/SUB 을 붙인 엔드포인트로 간단히 수신자/송신자를 구별.
-  서버 확장성을 위한 redis를 이용한 서버간 데이터 전송을 구현 + 인메모리 영역 저장소 활용으로 빠른 성능을 기대

- **장점**:
    - 양방향 통신으로 편리한 서비스 구현
    - 편리한 실시간 메세지 송수신 구현
    - Redis를 이용한 빠른 성능 구현
    - 서버간 데이터 전송으로 서버 확장성 구현
- **단점**:
    - 메시지 전송 실패 시 메시지 유실

## 2. 구현 기능에 대한 정보

<참고 이미지>

![최종1](https://github.com/user-attachments/assets/9c99f7df-208b-4dae-ac1f-8bae2fb44bf5)

---

### 1. 데이터의 흐름

 Start: 클라이언트 -> 웹소캣 -> 컨트롤러 -> 서비스 -> stomp handler -> security filter -> stomp.send -> redis -> server -> 클라이언트 

 기술의 흐름: Publisher -> 웹소캣 -> Stomp -> Redis -> data save -> Stomp -> 웹소캣 -> Subscriber

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

