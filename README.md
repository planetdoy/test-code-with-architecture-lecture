# [Java/Spring 테스트를 추가하고 싶은 개발자들의 오답노트](https://www.inflearn.com/course/%EC%9E%90%EB%B0%94-%EC%8A%A4%ED%94%84%EB%A7%81-%ED%85%8C%EC%8A%A4%ED%8A%B8-%EA%B0%9C%EB%B0%9C%EC%9E%90-%EC%98%A4%EB%8B%B5%EB%85%B8%ED%8A%B8/dashboard)
> Spring에 테스트를 넣는 방법을 알려드립니다! 더 나아가 자연스러운 테스트를 할 수 있게 스프링 설계를 변경하는 방법을 배웁니다.  

## 섹션 1 - 오리엔테이션
### [내가 하는 TDD는 왜 실패하는가?](./document/resources/note/section_1_1.pdf)

## 섹션 2 - 이론 수업
### [테스트에 대한 개요와 개발자가 해야할 고민](./document/resources/note/section_2_2.pdf)
### [테스트의 필요성과 테스트 3분류](./document/resources/note/section_2_3.pdf)
### [테스트에 필요한 개념](./document/resources/note/section_2_4.pdf)
### [의존성과 Testbility(1)](./document/resources/note/section_2_5.pdf)
### [실기 전 사전 탐색](./document/resources/note/section_2_7.pdf)

## 섹션 3 - 1부 실기 수업 - 어쨌든 테스트 코드  
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


## 섹션 4 방향성 탐색
### [레이어드 아키텍처의 문제점과 해결책](./document/resources/note/section_4_1.pdf)

### [어떻게 변경할 것인가?](./document/resources/note/section_4_2.pdf)

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

## 섹션 6 - 진화하는 아키텍처
### [헥사고날 아키텍처 (1)](./document/resources/note/section_6_1.pdf)
> - 테스트는 품질보증을 위한 도구이기도 하지만 설계를 위한 도구  
> - Testability를 높여야 설계를 위한 도구로서 온전히 활용할 수 있는 것이고 가치를 올릴 수 있는 것  
> - 테스트와 설계는 상호 보완적  
> - "테스트하기 쉬운 코드가 좋은 코드일 확률이 높다."  
> - 아키텍처는 꼭 써야하는 이유를 우선 파악하고 구성원 모두가 공감을 해야 합니다. 공감이 없으면 장애물이 될 뿐이다.    
> - 의존성 역전은 경계이고 관심사를 분리하고 싶다는 것, 고립시키고 싶다 서로 영향을 주고 싶지 않다.  

### [헥사고날 아키텍처 (2)](./document/resources/note/section_6_2.pdf)
> - 레이어드 아키텍처는 DB 위주의 사고를 하게 된다.  
> - 상향식은 JPA를 하향식은 프레임워크를 먼저 생각하게 된다.  
> - 중요한건 정책이고 문제를 파악하는 요구사항 명제!!
> - 좋은 개발자가 되기 위해서는 본질과 험블을 구분할 수 있어야 하고 도메인을 설계하는 능력을 갖춰야한다.

### [헥사고날에 대한 강의자의 사견](./document/resources/note/section_6_3.pdf)
> - 비즈니스 집중 : DDD  
> - 비즈니스 잘 구현 : 테스트  
> - 비즈니스와 기술을 분리 : 클린아키텍처  
> - 비즈니스와 기수을 분리하는 구체적 방법 : 헥사고날 아키텍처  
> - [DDD 이벤트 스토밍 추천 강의](https://www.youtube.com/watch?v=QUMERCN3rZs)  

### [마무리와 부록](./document/resources/note/section_6_4.pdf)
> - 테스트는 어디까지? - 릴리즈 할 때 확신을 주면 된다.
> - 테스트는 무엇을 해야하나? Right-BICEP 를 적용해보자 (굳이?라는 부분도 있다)
> - 성능이나 부하 테스트는 보통 ngrinder 라는 툴을 많이 이용한다.
> - 테스트 팁
> - 테스트는 가시성 가독성 표현력이 중요하다.
> - 사람이 이해할 수 있는 코드는 명확해야 하고 의도가 드러난 설계를 가져야 하고 의도가 드러나는 테스트가 있으면 제일 완벽하다.  
> - 테스트는 그 자체로 문서이다.  