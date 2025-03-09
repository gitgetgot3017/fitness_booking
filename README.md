# 요가 및 필라테스 수강신청 프로젝트

### ✔ 기술 스택
- Java 17
- SpringBoot 3.3.4
- JPA
- MySQL
- react
- AWS EC2, RDS, S3
- github actions
<br>

### 🔧 ERD 설계
![image](https://github.com/user-attachments/assets/3471256e-bdfd-4bad-b1d4-41651349690f)
<hr>

### 기술 STORY
* <b>테스트 코드 작성</b><br>
UI를 통해 수동으로 테스트하는 방식의 문제점은 시간이 오래 걸리고, 병렬 테스트가 불가능하며, 문서화 부족으로 관리가 어렵다는 점입니다.
presentation layer, service layer, persistence layer 별로 테스트 코드를 작성하여 총 63개의 테스트 코드를 작성였습니다.
덕분에 @Query에서 예상치 못한 쿼리 오류를 발견할 수 있었고, 이를 통해 빠른 시간 내에 보다 정확한 쿼리를 작성할 수 있었습니다.
* <b>비관적 락을 통한 동시성 문제 해결</b><br>
처음에는 멀티 쓰레드 환경을 고려하지 않고 코드를 작성했습니다.
JMeter를 활용한 동시성 테스트 결과, 6명 정원인 수업에 9명이 수강 신청에 성공하게 되었습니다.
이에 비관적 락(pessimistic lock)을 도입하게 되었고, 그 결과 JMeter에서 6명 정원에 해당하는 인원만 수강신청에 성공하는 상황을 확인할 수 있었습니다.
synchronized, ReentrantLock, CAS 연산과 같은 애플리케이션 단에서의 해결 방법과 낙관적 락, 비관적 락과 같은 DB 단에서의 해결 방법 등 동시성 이슈를 해결할 수 있는 다양한 방법에 대해 배울 수 있었습니다.
* <b>스프링 시큐리티 없이 JWT 직접 구현</b><br>
쿠키와 세션 기반의 회원가입 및 로그인은 보안 및 서버에 부하를 준다는 문제점이 있습니다.
 이에 JWT를 도입하게 되었고, 내부 동작을 깊이 있게 이해하고자 스프링 시큐리티를 사용하지 않고 직접 구현하게 되었습니다.
그러나 JWT는 토큰 자체적으로 데이터를 저장하고 있기에 제 3자에 의해 탈취될 수 있다는 문제점이 있었고, 이에 따라 토큰의 유효 기간을 15분으로 짧게 설정하였습니다.
그러나 토큰의 유효 기간을 짧게 설정한 만큼 사용자는 로그인을 더 자주 해야 한다는 문제점이 있었고, 이에 토큰을 access token과 refresh token으로 이중화하여 보안을 강화하고 사용자도 재로그인 없이 서비스를 지속적으로 이용할 수 있도록 하였습니다.
* <b>API 호출 횟수를 줄여 서버의 응답 최적화</b><br>
k6를 통한 부하 테스트에서 메인 페이지 조회 시에 throughput이 1790 TPS, 최대 latency가 306ms임을 확인했습니다.
사용자가 메인 페이지를 조회할 때, 수강권 조회, 수강 기록 조회, 수강 목록 조회라는 3번의 API 호출이 발생했었고, 이를 한 번의 API 호출로 메인 페이지에 필요한 모든 데이터를 받아오는 것으로 로직을 수정하였습니다.
그 결과 throughput이 2180 TPS, 최대 latency가 219ms로 성능이 향상되었음을 확인할 수 있었습니다.
API 응답 설계를 고민하며 어떤 데이터를 미리 가공해서 제공해야 프론트에서 추가적인 요청이 없을지를 고려하게 되었습니다.
* 기타: google Gemini API를 활용한 수업 추천, coolSMS API를 활용한 문자 전송, SSE 기반 실시간 알림
<hr>

### 구현 결과
![image](https://github.com/user-attachments/assets/092e7656-a421-44cc-861f-6a9dbed86093)
![image](https://github.com/user-attachments/assets/3517c969-8a01-491d-8703-a54f380c5efa)
![image](https://github.com/user-attachments/assets/a2a10a0a-638b-414e-ae70-dcc641422252)
![image](https://github.com/user-attachments/assets/86ff5d3b-4c99-4877-bacc-9fe0bedac7be)
![image](https://github.com/user-attachments/assets/c90dcde7-7131-4163-813a-d554d4267e44)
![image](https://github.com/user-attachments/assets/435d0d2a-6d46-4b4d-9f00-01c2ab8b60cb)
