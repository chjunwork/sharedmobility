# Shared Mobility(공유이동수단 서비스)

# 개요
 자동차나 자전거 등 이동수단에 대해 사용자가 app으로 신청한 후 발급 받은 key 값으로 이용 가능한 서비스

# 서비스 시나리오

기능적 요구사항
 1. 고객은 사용하려는 이동수단과 이용시간을 입력하여 신청한다.
 2. 이용시간을 계산하여 금액을 산출하고 결제한다.
 3. 결제가 완료되면 고객에게 4자리 Key를 발급한다.
 4. 고객은 사용 신청을 취소할 수 있다.
 5. 신청이 취소 되면 발급 된 key는 초기화 된다.
 6. Key가 초기화 되면 결제를 취소한다.
 7. 고객은 신청 상태를 mypage를 통해 조회 가능하다.

비기능적 요구사항
 1. Transaction
   a. 결제가 완료 되지 않으면 신청이 완료 되지 않는다. (Sync)
   b. 취소 시 발급 된 key를 우선 무효화 시키고 결제를 취소한다. (보상 transaction)
 2. 장애 격리
   a. key 발급에 문제가 생겨도 신청은 받을 수 있어야 한다. (Async event-driven)
   b. 결제에 문제가 발생 할 경우 잠시 후에 신청을 받을 수 있도록 동작해야 된다. (circuitbreak)
 3. 성능
   a. 고객은 신청 상태를 확인 할 수 있어야 한다. (CQRS)
 
# 분석 설계
# Event Storming
![sharedmobility_event](https://user-images.githubusercontent.com/76153097/108788758-174a9f80-75bc-11eb-8d6f-a3e606b0f33a.png)

# 헥사고날 아키텍처
![hexago](https://user-images.githubusercontent.com/76153097/109088813-77c01500-7753-11eb-9cf7-731fe8727a89.png)

# 구현

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트와 파이선으로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8081 ~ 808n 이다)

```
cd mobility
mvn spring-boot:run

cd payment
mvn spring-boot:run 

cd issue
mvn spring-boot:run  

cd mypage
mvn spring-boot:run
```

## DDD 의 적용
각 서비스내에 도출된 핵심 Aggregate Root 객체를 Entity 로 선언하였다

```
package sharedmobility;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Mobility_table")
public class Mobility {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String mobilityId;
    private Integer useHour;
    private String status;

    // 중략...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getMobilityId() {
        return mobilityId;
    }

    public void setMobilityId(String mobilityId) {
        this.mobilityId = mobilityId;
    }
    public Integer getUseHour() {
        return useHour;
    }

    public void setUseHour(Integer useHour) {
        this.useHour = useHour;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

```
### API Gateway 적용
API Gateway를 통하여 동일 진입점으로 진입하여 각 마이크로 서비스를 접근할 수 있다. 외부에서 접근을 위하여 Gateway의 Service는 LoadBalancer Type으로 생성했다

```
# Application.yml

spring:
  profiles: docker
  cloud:
    gateway:
      routes:
        - id: mobility
          uri: http://mobility:8080
          predicates:
            - Path=/mobilities/** 
        - id: issue
          uri: http://issue:8080
          predicates:
            - Path=/issues/** 
        - id: payment
          uri: http://payment:8080
          predicates:
            - Path=/payments/** 
        - id: mypage
          uri: http://mypage:8080
          predicates:
            - Path= /mypages/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true

server:
  port: 8080
```
<img width="1054" alt="gateway" src="https://user-images.githubusercontent.com/76153097/109091817-cf14b400-7758-11eb-8e52-96263a3c955a.png">


### REST API 적용 테스트 및 SAGA Pattern, CQRS, Correlation

![access](https://user-images.githubusercontent.com/76153097/109089498-b5716d80-7754-11eb-96ef-a354af2dfa7d.png)

![access_kafka](https://user-images.githubusercontent.com/76153097/109089760-1b5df500-7755-11eb-8755-de0c56a061f7.png)

![access_mypage](https://user-images.githubusercontent.com/76153097/109089769-2022a900-7755-11eb-9ed6-592459530ec7.png)

![cancel](https://user-images.githubusercontent.com/76153097/109089777-23b63000-7755-11eb-957e-6880949b58da.png)

![cancel_kafka](https://user-images.githubusercontent.com/76153097/109089784-26b12080-7755-11eb-92be-30b1611f0df7.png)

![cancel_mypage](https://user-images.githubusercontent.com/76153097/109089794-29137a80-7755-11eb-94da-9fcf742bc575.png)

### Polyglot 

Mypage는 hsqldb 를 사용하여도 조회가 가능하다.

![polyglot_hsqldb](https://user-images.githubusercontent.com/76153097/109091427-1e0e1980-7758-11eb-840f-34b58b21f46a.png)
![access_mypage](https://user-images.githubusercontent.com/76153097/109089769-2022a900-7755-11eb-9ed6-592459530ec7.png)

### REQ/RES
동기식 호출 서비스로 결제 서비스에 이상이 있을 경우 신청이 되지 않는다.

![payment_down_sync](https://user-images.githubusercontent.com/76153097/109089998-9f17e180-7755-11eb-9724-a9f0bec387ec.png)
![payment_down_confirm](https://user-images.githubusercontent.com/76153097/109090003-a17a3b80-7755-11eb-9edf-3820d475d946.png)
![sync_failed](https://user-images.githubusercontent.com/76153097/109090014-a50dc280-7755-11eb-8374-15dfd5f9fd9d.png)

### Async Event-Driven 장애 격리
Key 발급 서비스가 내려가 있더라도 신청과 결제는 정상적으로 서비스를 한다.

![issue_down_async](https://user-images.githubusercontent.com/76153097/109090236-046bd280-7756-11eb-815f-6f3170720a3f.png)
![service_ok_async](https://user-images.githubusercontent.com/76153097/109090247-0766c300-7756-11eb-87e5-fe9e988e42fb.png)
![after_issue_up_async](https://user-images.githubusercontent.com/76153097/109090251-09c91d00-7756-11eb-9758-0ea92f9915b8.png)

### CI/CD

AWS CodeBuild 를 사용하였으며, buildspec.yml를 사용하여 pipeline 을 구성한다.

![codebuild1](https://user-images.githubusercontent.com/76153097/109091970-1ef37b00-7759-11eb-96cf-11712a7ee93f.png)
![codebuild2](https://user-images.githubusercontent.com/76153097/109091981-2155d500-7759-11eb-91e2-f6d5863bc29d.png)
![codebuild3](https://user-images.githubusercontent.com/76153097/109091989-231f9880-7759-11eb-89fe-24090a062029.png)
![rolebinding](https://user-images.githubusercontent.com/76153097/109092061-3fbbd080-7759-11eb-9f2f-b1d5d5efd936.png)
![token4cicd](https://user-images.githubusercontent.com/76153097/109092068-421e2a80-7759-11eb-9696-3b1eac312535.png)
![codebuild4](https://user-images.githubusercontent.com/76153097/109091994-2581f280-7759-11eb-80fc-3a17b1ade347.png)
![codebuild5](https://user-images.githubusercontent.com/76153097/109092003-29157980-7759-11eb-8924-84b524c01a88.png)
![codebuild_result](https://user-images.githubusercontent.com/76153097/109092012-2b77d380-7759-11eb-8139-c652c7d3c47a.png)



