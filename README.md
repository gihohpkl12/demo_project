# 데모 프로젝트

## 개요
KMDB Open API의 영화 정보를 기반으로 영화 정보 소개 사이트.

## 주의사항
개인정보 (aws access key, naver oauth 정보, kmdb api key 등등 )를 보호하기 위해 application.properties는 git에 올리지 않았습니다.<br/>
아래 정보를 입력해야 정상적으로 작동합니다. <br/>
프로젝트 구동 사진 및 상세 설명은 /detail을 참고하시기 바랍니다.

 - DB <br/>
spring.datasource.url=jdbc:postgresql: <br/>
spring.datasource.username= <br/>
spring.datasource.password= <br/>

 - jpa <br/>
spring.jpa.hibernate.ddl-auto= <br/>
spring.jpa.show-sql = true <br/>
spring.jpa.properties.hibernate.format_sql = true <br/>
logging.level.org.hibernate.SQL=debug <br/>
logging.level.org.hibernate.orm.jdbc.bind=trace <br/>

- redis <br/>
spring.redis.host= <br/>
spring.redis.port= <br/>

- jwt <br/>
jwt.secret.key = <br/>

- kmdb <br/>
kmdb.api.key= <br/>
kmdb.api.url= <br/>

- 네이버 <br/>
oauth.login-api.naver.authentication-request-url = <br/>
oauth.login-api.login-api.access-token-reques-url = <br/>
oauth.login-api.user-info-request-url = <br/>
oauth.login-api.authentication-redirectu-url = <br/>
oauth.login-api.user-info-redirect-url = <br/>
oauth.login-api.clientId = <br/>
oauth.login-api.secret = <br/>

- aws <br/>
cloud.aws.credentials.accessKey= <br/>
cloud.aws.credentials.secretKey= <br/>
cloud.aws.s3.bucket= <br/>
cloud.aws.region.static= <br/>
cloud.aws.stack.auto-=false

- mail <br/>
app.host=<br/>
spring.mail.host=<br/>
spring.mail.port=<br/>
spring.mail.username=<br/>
spring.mail.password=<br/>
spring.mail.properties.mail.smtp.auth=<br/>
spring.mail.properties.mail.smtp.timeout=<br/>
spring.mail.properties.mail.smtp.starttls.enable=<br/>

## 환경
 - java 17
 - spring boot 3.1.0
 - spring security 6.1.0

## 추가 Dependency 
 - spring security : 인증 인가
 - thymeleaf : 템플릿 엔진
 - redis : 인증 관련 토큰 저장용 NoSql
 - feign : rest api 편의성 목적
 - jwt : 인증 토큰
 - spring-validation : get, post 요청에 queryparameter, body 검증 목적
 - spring-cache : 자주 사용되는 데이터의 캐싱 목적
 - lombok : 편의성 목적
 - spring-mail : 인증 메일 전송
 - json : json 사용 목적 
 - postgresql : DB 데이터 저장
 - aws s3 : 이미지 저장 목적
 - jpa : DB 연동
