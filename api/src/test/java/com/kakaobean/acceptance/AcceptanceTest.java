package com.kakaobean.acceptance;


import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.member.domain.AuthProvider;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.notification.domain.repository.NotificationRepository;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationService;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.QueueInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.kakaobean.acceptance.TestMember.*;
import static com.kakaobean.core.common.domain.BaseStatus.*;
import static com.kakaobean.core.member.domain.Role.*;

//@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @Autowired
    protected EmailRepository emailRepository;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected MQCleaner mqCleaner;

    @Autowired
    protected AmqpAdmin amqpAdmin;

    @Autowired
    protected ProjectRepository projectRepository;

    @Autowired
    protected IssueRepository issueRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected NotificationRepository notificationRepository;

    @Autowired
    protected ProjectMemberRepository projectMemberRepository;

    @Autowired
    protected ReleaseNoteRepository releaseNoteRepository;

    @Autowired
    protected SprintRepository sprintRepository;

    @Autowired
    protected TaskRepository taskRepository;

    @MockBean
    protected SendEmailNotificationService sendEmailNotificationService;

    @BeforeEach
    void beforeEach(){
        RestAssured.port = port;

        mqCleaner.resetRabbitMq();
        databaseCleaner.execute();

        createMember("ADMIN", ADMIN);
        createMember("MEMBER", MEMBER);

    }

    protected void createMember(String name, TestMember member) {
        memberRepository.save(new Member(
                name,
                member.getEmail(),
                ROLE_USER,
                passwordEncoder.encode(member.getPassword()),
                AuthProvider.local, ACTIVE
                )
        );
    }


}
