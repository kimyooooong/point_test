## 프로젝트 소개

JAVA / SPRING BOOT / JPA 기반 포인트 적립 / 사용 API 구현

## 사용언어 및 프레임 워크

언어 : 
* Java 11 / Spring Boot 2.7.4

DB : 
* H2 ( JPA )

기타 :
* 기본적으로 테스트 할 수 있는 멤버 생성 ( 멤버 아이디 - 1 ).
* 기본 포트 : 8000 으로 설정.
* 사용된 REST-API 는 SWAGGER 로 문서 구현하여 확인 가능. 
  <br>( http://localhost:8000/swagger-ui/index.html )
* H2 console 접속하여 데이터 확인 가능.
  <br>( http://localhost:8000/h2-console )
  - Driver Class : org.h2.Driver
  - JDBC URL : jdbc:h2:mem:test
  - User Name : sa
  <br> 입력 후 실행.
  
---

## 구동 방법 ( linux 기준 )

### Java - jdk 설치 ( 11 )
```
yum install java-11-openjdk-devel.x86_64
```

### git clone
```
git clone https://github.com/kimyooooong/point_test.git
```

### /result 폴더에 point_test.jar 실행
```
cd result
java -jar point_test.jar
```
