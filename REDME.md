목적
* user-service, recommend-service로 오는 요청을 분배한다.
* token에서 email을 추출해 service로 보내준다.
* refreshToken은 캐싱한다.

lib
* spring-boot-starter-data-redis
* spring-cloud-starter-gateway
* kotlin-logging-jvm
* jjwt-api
* jjwt-impl
* jjwt-jackson

infra
* redis

filters
* TokenRevokeFilter
*

todo list
* swagger 연결
* refreshToken을 인증하는 filter 개발
* api gateway exception error message 미노출 수정
* redis(refreshToken, email) 저장
* feign 연동
 