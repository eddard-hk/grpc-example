grpc-example
============

## 개요
<https://eddard.tistory.com/3>

위 블로그에 "GRPC Java Example" 제목으로 게시된 글의 소스코드 입니다.

##Modules
* grpc-common
  * GRPC를 사용하기 위한 메시지 정의
* grpc-client
  * GRPC Client
* grpc-server
  * GRPC Server

## Build
```bash
mvn clean install
``` 

## Run
```bash
#grpc-server
java jar grpc-server-1.0-SNAPSHOT.jar

#grpc-client
java jar grpc-client-1.0-SNAPSHOT.jar
```
