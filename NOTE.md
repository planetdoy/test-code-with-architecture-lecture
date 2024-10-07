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