package com.kakaobean.acceptance;


import com.kakaobean.core.member.domain.AuthProvider;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.kakaobean.acceptance.TestMember.*;
import static com.kakaobean.core.common.domain.BaseStatus.*;
import static com.kakaobean.core.member.domain.Role.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    DatabaseCleaner databaseConfigurator;

    @Autowired
    protected EmailRepository emailRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach(){
        RestAssured.port = port;
        databaseConfigurator.execute();;
        memberRepository.save(new Member(
                "TESTER",
                TESTER.getEmail(),
                ROLE_USER,
                passwordEncoder.encode(TESTER.getPassword()),
                AuthProvider.local, ACTIVE
                )
        );
    }
}
