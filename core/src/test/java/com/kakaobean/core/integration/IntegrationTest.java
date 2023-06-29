package com.kakaobean.core.integration;

import com.kakaobean.core.config.chatbot.ChatbotProperties;
import com.kakaobean.independentlysystem.config.ses.AwsSesConfig;
import com.kakaobean.independentlysystem.email.EmailSender;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;

@Import({EmailSender.class, AwsSesConfig.class, ChatbotProperties.class})
@SpringBootTest
@Transactional
@EnableJpaAuditing
public abstract class IntegrationTest {
}
