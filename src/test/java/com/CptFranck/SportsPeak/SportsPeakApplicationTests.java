package com.CptFranck.SportsPeak;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.config.additional-location=classpath:application-test.yml")
class SportsPeakApplicationTests {

    @Test
    void contextLoads() {
    }

}
