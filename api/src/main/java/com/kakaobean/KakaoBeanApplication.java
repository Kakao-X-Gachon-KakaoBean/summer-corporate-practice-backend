package com.kakaobean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan
public class KakaoBeanApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakaoBeanApplication.class, args);
    }

}
