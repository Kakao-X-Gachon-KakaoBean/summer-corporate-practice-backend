package com.kakaobean.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.kakaobean.member.MemberController;
import com.kakaobean.project.ProjectController;
import com.kakaobean.project.ProjectMemberController;
import com.kakaobean.security.TokenProvider;

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
        WebMvcConfig.class
})
@WebMvcTest(controllers = {
        MemberController.class,
        ProjectController.class,
        ProjectMemberController.class
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

}