package com.kakaobean.acceptance;


import com.kakaobean.core.member.domain.repository.EmailRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleaner databaseConfigurator;

    @Autowired
    protected EmailRepository emailRepository;

    @BeforeEach
    void beforeEach(){

//        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
//            RestAssured.port = 12312;
//            databaseConfigurator.afterPropertiesSet();
//        }

        RestAssured.port = port;
        databaseConfigurator.execute();;
    }
}
