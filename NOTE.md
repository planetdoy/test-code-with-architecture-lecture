# NOTE

## 섹션 3
### 테스트 추가하기: h2를 이용한 repository 테스트
####  비결정적인 테스트
> 전체 테스트를 돌리면 실패하지만 단독으로는 성공하는 테스트  
> 테스트 메서드가 병렬로 처리되는데 동시성 제어가 안되는 문제

> 해결방법 :  테스트 시 준비 된 정보를 사용하도록 한다. @Sql("/sql/스크립트.sql")

### 테스트 추가하기: h2를 이용한 service 테스트
#### 네이밍 컨벤션
1. get은 데이터가 없으면 throw 를 던진다는 의미로 사용된다.
2. find는 Optional 을 응답한다는 의미로 사용된다.

#### Mockito
> 이메일을 발송하는 JavaMailSender 를 대체하기 위해 사용
```java
    // @Autowired
    // java bean 으로 등록된 객체를 Mock으로 선언된 객체로 덮어쓰기
    // 이러면 테스트를 실행할 때 MockBean 값이 주입되어 실행됩니다.
    @MockBean
    private JavaMailSender javaMailSender;

        ...
        
    BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
```