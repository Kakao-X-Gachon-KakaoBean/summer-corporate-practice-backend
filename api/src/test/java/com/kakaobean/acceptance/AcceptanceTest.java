package com.kakaobean.acceptance;


import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.member.domain.Auth;
import com.kakaobean.core.member.domain.AuthProvider;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.notification.domain.repository.NotificationRepository;
import com.kakaobean.core.notification.domain.service.send.email.SendEmailNotificationService;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import com.kakaobean.fixture.member.MemberFactory;

import com.kakaobean.independentlysystem.email.EmailSender;
import io.restassured.RestAssured;
import lombok.Getter;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

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

    @Autowired
    protected ManuscriptRepository manuscriptRepository;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @MockBean
    protected SendEmailNotificationService sendEmailNotificationService;

    @MockBean
    protected EmailSender emailSender;

    public static ThreadLocal<MemberContext> memberContext = new ThreadLocal<>();

    public static ThreadLocal<String> adminTokenContext = new ThreadLocal<>();
    public static ThreadLocal<String> memberTokenContext = new ThreadLocal<>();


    @BeforeEach
    void beforeEach(){
        RestAssured.port = port;

        Member admin = createMember(MemberFactory.createAdminWithTempEmail());
        Member member = createMember(MemberFactory.createMemberWithTempEmail());

        setMemberContext(new MemberContext(admin, member));
    }

    /**
     * 메시징 큐를 사용하는 곳에서 직접 사용해야 한다.
     */
    protected void setMemberContext(MemberContext context) {
        memberContext.set(context);
    }

    @AfterEach
    void afterEach() {
        memberContext.remove();
        adminTokenContext.remove();
        memberTokenContext.remove();
    }

    protected Member createMember(Member member) {
        Member result = memberRepository.save(new Member(
                        member.getName(),
                        member.getAuth().getEmail(),
                        ROLE_USER,
                        passwordEncoder.encode(member.getAuth().getPassword()),
                        AuthProvider.local, ACTIVE
                )
        );
        return Member.builder()
                .id(result.getId())
                .name(member.getName())
                .auth(new Auth(member.getAuth().getEmail(), member.getAuth().getPassword()))
                .role(ROLE_USER)
                .authProvider(AuthProvider.local)
                .build();
    }

    @Getter
    public static class MemberContext {

        private final Member admin;
        private final Member member;

        public MemberContext(Member admin, Member member) {
            this.admin = admin;
            this.member = member;
        }
    }

    /**
     * 메시징 큐를 테스트하는 메서드에서 사용해야 한다.
     */
    protected MemberContext deleteMemberContext() {
        MemberContext context = AcceptanceTest.memberContext.get();

        memberRepository.deleteById(context.getMember().getId());
        memberRepository.deleteById(context.getAdmin().getId());

        memberContext.remove();
        adminTokenContext.remove();
        memberTokenContext.remove();
        return context;
    }
}
