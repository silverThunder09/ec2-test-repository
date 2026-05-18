package com.ec2test;

import com.ec2test.service.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class Ec2TestApplicationTests {

    @MockitoBean
    private S3Service s3Service;

    @Test
    void contextLoads() {
    }

}
