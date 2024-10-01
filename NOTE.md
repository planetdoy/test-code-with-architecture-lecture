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
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .nickname("doydoit-n")
                .address("pangyo")
                .build();

        // when
        // then
        mockMvc.perform(
                        put("/api/users/me")
                                .header("EMAIL", "doydoit@gmail.com")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("doydoit@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("doydoit-n"))
                .andExpect(jsonPath("$.address").value("pangyo"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
```

