package com.CptFranck.SportsPeak.controller.IntegrationTest.DGS;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
public class AuthControllerDGSIntTest {

//    @Autowired
//    private DgsQueryExecutor dgsQueryExecutor;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    private UserEntity user;
//
//    private String rawPassword;
//
//    private LinkedHashMap<String, Object> variables;
//
//    @BeforeEach
//    void setUp() {
//        user = createTestUser(null);
//        rawPassword = user.getPassword();
//        user = authService.register(user);
//        roleRepository.save(createTestRole(null, 0));
//        variables = new LinkedHashMap<>();
//    }
//
//    @AfterEach
//    public void afterEach() {
//        userRepository.deleteAll();
//        roleRepository.deleteAll();
//    }
//
//    @Test
//    public void AuthController_Login_UnsuccessfulEmailUnknown() {
//        variables.put("inputCredentials", objectMapper.convertValue(
//                        new InputCredentials("user.getEmail()", rawPassword),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(loginQuery, "data.login", variables));
//    }
//
//    @Test
//    public void AuthController_Login_UnsuccessfulIncorrectPass() {
//        variables.put("inputCredentials", objectMapper.convertValue(
//                        new InputCredentials(user.getEmail(), "rawPassword"),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(loginQuery, "data.login", variables));
//    }
//
//    @Test
//    public void AuthController_Login_ReturnAuthDto() {
//        variables.put("inputCredentials", objectMapper.convertValue(
//                        new InputCredentials(user.getEmail(), rawPassword),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//
//        LinkedHashMap<String, Object> authDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(loginQuery, "data.login", variables);
//
//        Assertions.assertNotNull(authDto);
//    }
//
//    @Test
//    public void AuthController_Register_UnsuccessfulEmailAlreadyUsed() {
//        variables.put("inputRegisterNewUser", objectMapper.convertValue(
//                        new InputRegisterNewUser(
//                                user.getEmail(),
//                                user.getFirstName(),
//                                user.getLastName(),
//                                user.getLastName(),
//                                user.getPassword()
//                        ),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables));
//    }
//
//    @Test
//    public void AuthController_Register_UnsuccessfulUsernameExists() {
//        UserEntity userBis = createTestUserBis(null);
//        variables.put("inputRegisterNewUser", objectMapper.convertValue(
//                        new InputRegisterNewUser(
//                                userBis.getEmail(),
//                                userBis.getFirstName(),
//                                userBis.getLastName(),
//                                user.getUsername(),
//                                userBis.getPassword()
//                        ),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//
//        Assertions.assertThrows(QueryException.class,
//                () -> dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables));
//    }
//
//    @Test
//    public void AuthController_Register_ReturnAuthDto() {
//        UserEntity userBis = createTestUserBis(null);
//        variables.put("inputRegisterNewUser", objectMapper.convertValue(
//                        new InputRegisterNewUser(
//                                userBis.getEmail(),
//                                userBis.getFirstName(),
//                                userBis.getLastName(),
//                                userBis.getLastName(),
//                                userBis.getPassword()
//                        ),
//                        new TypeReference<LinkedHashMap<String, Object>>() {
//                        }
//                )
//        );
//
//        LinkedHashMap<String, Object> authDto =
//                dgsQueryExecutor.executeAndExtractJsonPath(registerQuery, "data.register", variables);
//
//        Assertions.assertNotNull(authDto);
//    }
}
