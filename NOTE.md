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

#### PostServiceTest 를 직접 작성하며 느낀점
> 테스트 하려는 메서드 로직 이해 후 검증하는데 집중해야겠다.  

### 테스트 추가하기: mockmvc 를 이용한 controller 테스트
> 컨트롤러 테스트를 위해 mockMvc 라는걸 사용하는데 api 테스트를 사용하는데 많이 사용합니다.  
> 그래서 스프링부트 테스트를 추가하고 mockMvc를 위한 자동설정을 몇개 추가합니다.  

```java
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class HealthCheckTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 헬스체크응답이200으로내려온다() throws Exception{
        mockMvc.perform(get("/health_check.html"))
                .andExpect(status().isOk());
    }

    @Test
    void 사용자는내정보를수정할수있다() throws Exception {
        // given
        UserUpdateDto userUpdate = UserUpdateDto.builder()
                .nickname("doydoit-n")
                .address("pangyo")
                .build();

        // when
        // then
        mockMvc.perform(
                        put("/api/users/me")
                                .header("EMAIL", "doydoit@gmail.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("doydoit@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("doydoit-n"))
                .andExpect(jsonPath("$.address").value("pangyo"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
```

#### 남은 과제 (테스트가 보내는 신호들)
1. UUID  
2. private 메서드인데 테스트해봐야할 것 같은 메서드  
   - 사용자한테 가는 메세지가 정상인지 궁금  
3. 마지막 로그인 시간  
4. 굉장히 느린 테스트 시간  
5. H2를 사용하면서 발생한 동시성 문제  


## 섹션 4
### 방향성 탐색 : 레이어드 아키텍처의 문제점과 해결책
![01](./document/resources/note/section_4_1/01.jpg)
![02](./document/resources/note/section_4_1/02.jpg)
![03](./document/resources/note/section_4_1/03.jpg)
![04](./document/resources/note/section_4_1/04.jpg)
![05](./document/resources/note/section_4_1/05.jpg)
![06](./document/resources/note/section_4_1/06.jpg)
![07](./document/resources/note/section_4_1/07.jpg)
![08](./document/resources/note/section_4_1/08.jpg)
![09](./document/resources/note/section_4_1/09.jpg)
![10](./document/resources/note/section_4_1/10.jpg)
![11](./document/resources/note/section_4_1/11.jpg)
![12](./document/resources/note/section_4_1/12.jpg)
![13](./document/resources/note/section_4_1/13.jpg)
![14](./document/resources/note/section_4_1/14.jpg)
![15](./document/resources/note/section_4_1/15.jpg)
![16](./document/resources/note/section_4_1/16.jpg)
![17](./document/resources/note/section_4_1/17.jpg)
![18](./document/resources/note/section_4_1/18.jpg)
![19](./document/resources/note/section_4_1/19.jpg)
![20](./document/resources/note/section_4_1/20.jpg)
![21](./document/resources/note/section_4_1/21.jpg)
![22](./document/resources/note/section_4_1/22.jpg)
![23](./document/resources/note/section_4_1/23.jpg)
![24](./document/resources/note/section_4_1/24.jpg)
![25](./document/resources/note/section_4_1/25.jpg)
![26](./document/resources/note/section_4_1/26.jpg)
![27](./document/resources/note/section_4_1/27.jpg)
![28](./document/resources/note/section_4_1/28.jpg)
![29](./document/resources/note/section_4_1/29.jpg)
![30](./document/resources/note/section_4_1/30.jpg)
![31](./document/resources/note/section_4_1/31.jpg)
![32](./document/resources/note/section_4_1/32.jpg)
![33](./document/resources/note/section_4_1/33.jpg)
![34](./document/resources/note/section_4_1/34.jpg)
![35](./document/resources/note/section_4_1/35.jpg)
![36](./document/resources/note/section_4_1/36.jpg)

### 방향성 탐색 : 어떻게 변경할 것인가?
![00](./document/resources/note/section_4_2/00.jpg)
![01](./document/resources/note/section_4_2/01.jpg)
![02](./document/resources/note/section_4_2/02.jpg)
![03](./document/resources/note/section_4_2/03.jpg)
![04](./document/resources/note/section_4_2/04.jpg)
![05](./document/resources/note/section_4_2/05.jpg)
![06](./document/resources/note/section_4_2/06.jpg)
![07](./document/resources/note/section_4_2/07.jpg)
![08](./document/resources/note/section_4_2/08.jpg)
![09](./document/resources/note/section_4_2/09.jpg)
![10](./document/resources/note/section_4_2/10.jpg)
![11](./document/resources/note/section_4_2/11.jpg)
![12](./document/resources/note/section_4_2/12.jpg)
![13](./document/resources/note/section_4_2/13.jpg)
![14](./document/resources/note/section_4_2/14.jpg)
![15](./document/resources/note/section_4_2/15.jpg)
![16](./document/resources/note/section_4_2/16.jpg)
![17](./document/resources/note/section_4_2/17.jpg)
![18](./document/resources/note/section_4_2/18.jpg)
![19](./document/resources/note/section_4_2/19.jpg)
![20](./document/resources/note/section_4_2/20.jpg)
![21](./document/resources/note/section_4_2/21.jpg)
![22](./document/resources/note/section_4_2/22.jpg)
![23](./document/resources/note/section_4_2/23.jpg)
![24](./document/resources/note/section_4_2/24.jpg)
![25](./document/resources/note/section_4_2/25.jpg)
![26](./document/resources/note/section_4_2/26.jpg)
![27](./document/resources/note/section_4_2/27.jpg)
![28](./document/resources/note/section_4_2/28.jpg)
![29](./document/resources/note/section_4_2/29.jpg)
![30](./document/resources/note/section_4_2/30.jpg)
![31](./document/resources/note/section_4_2/31.jpg)
![32](./document/resources/note/section_4_2/32.jpg)
![33](./document/resources/note/section_4_2/33.jpg)
![34](./document/resources/note/section_4_2/34.jpg)
![35](./document/resources/note/section_4_2/35.jpg)
![36](./document/resources/note/section_4_2/36.jpg)
![37](./document/resources/note/section_4_2/37.jpg)
![38](./document/resources/note/section_4_2/38.jpg)
![39](./document/resources/note/section_4_2/39.jpg)
![40](./document/resources/note/section_4_2/40.jpg)
![41](./document/resources/note/section_4_2/41.jpg)
![42](./document/resources/note/section_4_2/42.jpg)
![43](./document/resources/note/section_4_2/43.jpg)
![44](./document/resources/note/section_4_2/44.jpg)
![45](./document/resources/note/section_4_2/45.jpg)
![46](./document/resources/note/section_4_2/46.jpg)
![47](./document/resources/note/section_4_2/47.jpg)
![48](./document/resources/note/section_4_2/48.jpg)
![49](./document/resources/note/section_4_2/49.jpg)

## 섹션 5 - 2부: 실기 수업 - 구조적 변화를 주는 테스트 코드
### 패키지 구조 개선
> 패키지 구조 개선 방식에 대해선 정답은 없지만 권위자의 방식을 따르고 싶다면  
> 헥사고날 아키텍처 방식을 따라하면 됩니다.

### 외부 연동을 다루는 방법
#### 의존성 역전하기
> 의존성 역전을 위해 interface 를 만듭니다.  
> UserRepository 를 infrastructure 에 두면 상위모듈인 service 에서 infrastructure 인 패키지에 의존하는 그림이 되면 안되기 때문에 service 쪽으로 옮겼습니다.  
> service 쪽에 외부연동을 담당하는 port 패키지를 두고 service 에서 사용하는 인터페이스들을 여기에 전부 몰아넣습니다.  

> 인증 이메일을 보내는 서비스를 따로 분리했습니다. - CertificationService  
> 이유는 User 도메인의 역할과 책임에 따라 분리한 것으로 보여집니다.  

#### 도메인의 역할과 책임
```text
Java OOP(객체지향 프로그래밍)에서 도메인의 역할과 책임에 따라 서비스를 분리하는 이유는 객체지향 설계 원칙을 충실히 따르고, 코드의 유지보수성, 확장성, 가독성을 향상시키기 위해서입니다. 이를 좀 더 구체적으로 설명하자면:

### 1. **응집도(Cohesion)와 결합도(Coupling) 관리**
   - **응집도**는 클래스나 모듈 내부에서 기능들이 얼마나 밀접하게 연관되어 있는지를 나타냅니다. 역할과 책임이 명확히 분리된 도메인 모델은 각 클래스나 모듈이 특정한 기능에 집중할 수 있도록 합니다.
   - **결합도**는 클래스나 모듈 간의 의존성을 의미합니다. 서비스와 도메인이 역할에 따라 잘 분리되면 모듈 간의 결합도가 낮아지고, 변경 사항이 다른 모듈에 미치는 영향을 줄일 수 있습니다.

### 2. **SRP (Single Responsibility Principle, 단일 책임 원칙)**
   - SRP는 클래스는 오직 하나의 책임만 가져야 한다는 원칙입니다. 도메인의 역할에 따라 서비스를 분리하면, 각 서비스는 하나의 명확한 책임만을 가지게 됩니다. 이는 시스템이 커지거나 복잡해질 때 변경에 유연하게 대처할 수 있게 해 줍니다.
   
### 3. **도메인 지식의 명확한 표현**
   - 도메인을 분리하는 것은 비즈니스 로직과 기술적인 세부 사항을 분리하는 데 중요합니다. 도메인 모델은 실제 비즈니스 로직을 담고 있어야 하고, 기술적 구현이나 외부 시스템과의 통신은 별도의 서비스 계층에 맡겨야 합니다. 이를 통해 도메인 로직이 비즈니스에 맞게 명확하게 표현되고, 기술적 세부 사항에 의해 오염되지 않게 됩니다.

### 4. **확장성과 유지보수성 향상**
   - 역할과 책임에 따라 서비스가 분리되어 있으면, 각 서비스나 모듈을 독립적으로 수정하거나 확장하기가 용이해집니다. 새로운 요구사항이 추가되더라도 기존 도메인 모델에 영향을 덜 주면서 기능을 추가할 수 있게 됩니다.
   - 또한, 코드의 중복을 줄이고, 특정 도메인 로직을 중앙 집중화하여 유지보수성도 개선됩니다.

### 5. **테스트 용이성**
   - 각 서비스가 역할에 따라 분리되어 있으면, 단위 테스트를 작성하기 쉬워집니다. 하나의 서비스나 모듈은 독립적인 책임을 가지기 때문에 해당 부분만 테스트할 수 있으며, 다른 서비스와 독립적으로 동작하는지 확인할 수 있습니다.

### 6. **DDD (Domain-Driven Design, 도메인 주도 설계)와의 연계**
   - 도메인의 역할과 책임을 기준으로 서비스를 분리하는 것은 DDD의 철학과 맞닿아 있습니다. DDD는 복잡한 비즈니스 로직을 다루는 시스템에서 특히 유용하며, 도메인 모델이 비즈니스 규칙을 명확하게 표현하도록 돕습니다. 이를 통해 도메인 전문가와 개발자가 같은 언어를 사용할 수 있게 되어, 의사소통이 원활해지고 시스템이 도메인에 맞게 설계됩니다.

### 결론
Java OOP에서 도메인의 역할과 책임에 따라 서비스를 분리하는 이유는 코드의 품질을 높이고, 유지보수와 확장이 용이한 시스템을 만들기 위해서입니다. 이를 통해 변화하는 요구사항에 더 잘 대응하고, 객체지향 원칙을 지키는 구조를 유지할 수 있습니다.
```

### 도메인과 영속성 객체 구분하기
> 도메인과 영속성객체를 구분하고 우리의 시스템을 외부의 시스템과 독립적이게 구성  

> 도메인은 인프라레이어의 정보를 모르는게 좋다.  
```text
user.toEntity()        -> X
userEntity.fromModel() -> O
```

> 테스트 코드가 있음으로써 큰 규모의 리팩토링에서도 놓치는 부분없이 변경이 가능하다는 것을 알게되었다.

#### 추가 작업 사항
- 도메인에 책임이 생겨 테스트가 필요하다. ex)User.from();  
- 불변객체의 반환은 새로운 인스턴스  
- 의존성 역전원칙을 고려하지 않고 우선 현재 시간을 사용 ex) Clock.systemUTC().millis()  

### 도메인에 테스트 추가하기
> mockito, h2, springboot 없이 테스트하는 방법  
> 소형 테스트를 작성하던 중  
> Controller, Service, Repository 를 중형 테스트이니 패키지를 분리한다.  
> 테스트 픽스쳐 : 테스트 전에 userService 를 미리 만들어서 사용

### 서비스를 소형 테스트로 만들기
> 중형보다 소형의 속도가 비교가 안되도록 빠르다.    
> 소형으로 작성하다보면 의존성에 대한 필요에 따라 리팩토링 요소가 발생한다.
> 테스트를 짜는게 귀찮으면 의존성을 줄이라는 신호라고 생각해 볼 수 있다.  
```java
public class PostService {

   private final PostRepository postRepository;
//   private final UserService userService;     // asis
   private final UserRepository userRepository; // tobe 
   
   // ...
   
}
```

> 이번 강의에서는 H2, mockito 를 사용하지 않고 테스트를 작성했는데  
> 이말은 굳이 자바가 아니라 다른 언어를 사용해도 테스트 라이브러리 없이 테스트를 작성할 수 있다는걸 배웠다는 것이다.  
> 이게 가능했던 점은 의존성 역전과 의존성 주입을 잘 적용했기 때문에 설계가 나아졌기 때문이다.  

### 컨트롤러를 소형 테스트로 만들기
> 우선 서비스를 추상화합니다.  
> 추상화에 의존하게 하는 것에만 집중한 나머지 하위 패키지인 service가 상위 패키지인 controller에 의존하는 그림이 나왔다. 주의하자.  
> UserService를 mock이나 fake로 구현하는건 힘듦 그래서 인터페이스를 분리함  
```text
UserService -> UserCreateService, UserReadService, UserUpdateService, AuthenticationService
```
> stub 은 선호하지 않는다 결과를 정해놓으면 구현을 제한하기 때문이다. 책임을 위임하고 구현은 알아서해라...  
> 대신 TestContainer를 만들어서 스프링의 IoC 컨테이너를 흉내 내는 코드를 작성합니다.  
> CertificationService는 controller 같은 외부 호출이 없으니 추상화하지 않아도 된다.  

### 마지막 리팩토링
> 서비스인데 상위패키지인 컨트롤러(controller.port)의 인터페이스를 잘못 참조하고 있다.