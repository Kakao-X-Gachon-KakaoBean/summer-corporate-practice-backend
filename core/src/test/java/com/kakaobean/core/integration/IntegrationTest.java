package com.kakaobean.core.integration;

import com.kakaobean.core.notification.config.EmailProperties;
import com.kakaobean.independentlysystem.amqp.AmqpService;
import com.kakaobean.independentlysystem.config.ses.AwsSesConfig;
import com.kakaobean.independentlysystem.email.EmailSender;
import com.kakaobean.independentlysystem.image.ImageService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.Transactional;


@Import({
        EmailSender.class,
        AwsSesConfig.class,
        ImageService.class,
        AmqpService.class,
        EmailProperties.class
})

@SpringBootTest
@EnableJpaAuditing
public abstract class IntegrationTest {

    @MockBean
    protected AmqpService amqpService;

    @MockBean
    protected EmailSender emailSender;
}
