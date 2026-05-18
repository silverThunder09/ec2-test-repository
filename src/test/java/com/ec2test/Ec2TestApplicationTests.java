package com.ec2test;

import com.ec2test.support.TestS3Config;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestS3Config.class)
class Ec2TestApplicationTests {

    @Test
    void contextLoads() {
    }

}
