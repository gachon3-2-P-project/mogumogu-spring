#  1️⃣ 프로젝트 소개

사용자 페이지

![image](https://github.com/gachon3-2-P-project/mogumogu-spring/assets/109260733/f424ee49-df3f-493d-b46e-6f52ca899f42)</br>
![image](https://github.com/gachon3-2-P-project/mogumogu-spring/assets/109260733/f56b70d2-98d2-48ba-bbb5-115a21ff9a2e)</br>
![image](https://github.com/gachon3-2-P-project/mogumogu-spring/assets/109260733/1cf11b07-ff3b-4f87-ae3c-e3067bc17e88)</br>
<br></br>

관리자 페이지

![image](https://github.com/gachon3-2-P-project/mogumogu-spring/assets/109260733/9070fbcd-17f3-4e59-ab71-d0f82eaee2fb)</br>
![image](https://github.com/gachon3-2-P-project/mogumogu-spring/assets/109260733/0c8ad8cf-14e0-4a6f-b8d0-fab72676dbf2)

일상 생활에서 한 번쯤은 가져봤을 고민인 <u> "편의점 맥주 4캔 10000원을 4명이서 나눠 사면 얼마나 좋을까..🤔"</u> 에서 시작된 프로젝트입니다. </br>
혼자 사기엔 부담스럽고, 누군가와 공동구매 하고싶을 때 공동구매를 매칭해주는 웹 서비스입니다.</br>
이메일 인증을 통한 가천대학교 학생 인증을 통해 보다 안전하게 거래가 진행될 수 있도록 기획하였습니다.

<br></br>
#  2️⃣ 프로젝트 사용 기술
- Spring Boot
- Java 17
- MySQL
- SMTP
- Spring Security
- AWS EC2, RDS
- Docker
- Gradle
  

<br></br>
#  3️⃣ 서비스 차별화 전략
✅ 이메일 인증(Google SMTP)을 통한 가천대 학생간의 안전한 거래 및 소통  
✅ 공동구매 ‘매칭' 및 관리자가 아닌 사용자의 물품 등록  
✅ 1인 가구 및 기숙사생 주 타겟 대상

<br></br>
#  4️⃣ 인프라 스트럭처 설계

<br></br>
마이크로서비스 아키텍처 설계  

![image](https://github.com/gachon3-2-P-project/mogumogu-spring/assets/109260733/48c96d3e-7ad0-48d0-ad3a-1adfb7aac926)


- Gateway Service
  - 사용자가 설정한 라우팅 설정에 따라서 각각 엔드포인트로 클라이언트 대신해서 요청하고 응답을 받으면 다시 클라이언트에 전달해주는 프록시 역할을 한다.

- Eureka Service
  - 동적으로 할당되는 서비스 인스턴스 네트워크의 정보 ( IP 주소 및 포트 번호 ) 를 등록하고 검색할 수 있는 서비스이다.
  - 각각의 마이크로 서비스들이 이 곳에 위치 정보를 등록하면, Gateway Service로 요청이 왔을 때 그에 맞는 서비스를 찾아 정보를 넘겨준다.


- Join Service
  - 로그인, 회원가입 API
- User Service
  - User 도메인 API
- Admin 도메인 API
  - Article Service
- Article 도메인 API
  - Message Service
- Message 도메인 API
  - Message 도메인 API
 
<br></br>
컨테이너 구성

![image](https://github.com/gachon3-2-P-project/mogumogu-spring/assets/109260733/ae979a79-0261-4747-9ad8-455b11e56fda)


<br></br>
#  5️⃣ 데이터베이스 설계

DB : mogumoguDB
Table : UserEntity, ArticleEntity, MessageEntity, AdminEntity

![image](https://github.com/gachon3-2-P-project/mogumogu-spring/assets/109260733/75cde96d-c97d-4cbf-9c21-fc5c2f76a643)


<br></br>
#  6️⃣ 프로젝트 issue
- Security 적용 및 토큰 발급 과정 문서화</br>
https://github.com/gachon3-2-P-project/mogumogu-web/issues/10

