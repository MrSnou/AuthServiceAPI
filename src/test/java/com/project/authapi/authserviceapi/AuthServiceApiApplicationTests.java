package com.project.authapi.authserviceapi;

import com.project.authapi.authserviceapi.service.AuthServiceTest;
import com.project.authapi.authserviceapi.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceApiApplicationTests {
    AuthServiceTest authServiceTest = new AuthServiceTest();
    JwtService jwtService = new JwtService();
    @Test
    void contextLoads() {

    }

}
