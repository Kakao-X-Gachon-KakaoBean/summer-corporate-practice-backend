package com.kakaobean.core.notification.config;

import com.kakaobean.core.notification.domain.NotificationRepository;
import com.kakaobean.core.notification.domain.service.register.RegisterNotificationService;
import com.kakaobean.core.notification.domain.service.register.strategy.RegisterDeploymentReleaseNoteNotificationStrategy;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationService;
import com.kakaobean.core.notification.domain.service.send.message.SendMessageNotificationStrategy;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DomainServiceConfig {

    private final ReleaseNoteRepository releaseNoteRepository;
    private final NotificationRepository notificationRepository;
    private final ProjectQueryRepository projectQueryRepository;
    private final ProjectRepository projectRepository;

    @Bean
    RegisterNotificationService saveNotificationService(){
        return new RegisterNotificationService(
                List.of(
                        new RegisterDeploymentReleaseNoteNotificationStrategy(
                                releaseNoteRepository,
                                notificationRepository,
                                projectQueryRepository,
                                projectRepository
                        )
                )
        );
    }
}
