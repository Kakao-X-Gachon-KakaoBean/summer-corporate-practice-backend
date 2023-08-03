package com.kakaobean.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaobean.common.ImageController;
import com.kakaobean.config.AppProperties;
import com.kakaobean.config.SecurityConfig;
import com.kakaobean.config.WebMvcConfig;
import com.kakaobean.core.member.application.MemberProvider;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.application.MemberService;
import com.kakaobean.core.project.application.ProjectMemberFacade;
import com.kakaobean.core.project.application.ProjectMemberService;
import com.kakaobean.core.project.application.ProjectService;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.core.releasenote.application.ManuscriptService;
import com.kakaobean.core.releasenote.application.ReleaseNoteService;
import com.kakaobean.core.releasenote.domain.repository.query.ManuscriptQueryRepository;
import com.kakaobean.core.releasenote.domain.repository.query.ReleaseNoteQueryRepository;
import com.kakaobean.core.sprint.application.SprintService;
import com.kakaobean.core.sprint.application.TaskService;
import com.kakaobean.core.sprint.domain.repository.query.SprintQueryRepository;
import com.kakaobean.core.sprint.domain.repository.query.TaskQueryRepository;
import com.kakaobean.independentlysystem.image.ImageService;
import com.kakaobean.member.MemberController;
import com.kakaobean.project.ProjectController;
import com.kakaobean.project.ProjectMemberController;
import com.kakaobean.releasenote.ManuscriptController;
import com.kakaobean.releasenote.ReleaseNoteController;
import com.kakaobean.security.token.RefreshTokenRepository;
import com.kakaobean.security.token.TokenProvider;

import com.kakaobean.sprint.SprintController;
import com.kakaobean.sprint.TaskController;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@Import({
        SecurityConfig.class,
        WebMvcConfig.class,
        ImageService.class
})
@WebMvcTest(controllers = {
        MemberController.class,
        ProjectController.class,
        ProjectMemberController.class,
        ReleaseNoteController.class,
        ManuscriptController.class,
        ImageController.class,
        SprintController.class,
        TaskController.class
})
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected PasswordEncoder passwordEncoder;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected MemberRepository memberRepository;

    @MockBean
    protected AppProperties appProperties;

    @MockBean
    protected MemberProvider memberProvider;

    @MockBean
    protected ProjectMemberService projectMemberService;

    @MockBean
    protected ProjectMemberFacade projectMemberFacade;

    @MockBean
    protected ProjectService projectService;

    @MockBean
    protected ProjectQueryRepository projectQueryRepository;

    @MockBean
    protected RefreshTokenRepository refreshTokenRepository;

    @MockBean
    protected ReleaseNoteService releaseNoteService;

    @MockBean
    protected ImageService imageService;

    @MockBean
    protected ManuscriptService manuscriptService;

    @MockBean
    protected ManuscriptQueryRepository manuscriptQueryRepository;

    @MockBean
    protected ReleaseNoteQueryRepository releaseNoteQueryRepository;

    @MockBean
    protected SprintService sprintService;

    @MockBean
    protected TaskService taskService;

    @MockBean
    protected SprintQueryRepository sprintQueryRepository;

    @MockBean
    protected TaskQueryRepository taskQueryRepository;
}