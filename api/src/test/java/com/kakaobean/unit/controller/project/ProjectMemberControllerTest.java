package com.kakaobean.unit.controller.project;


import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.project.dto.request.InviteProjectMemberRequest;
import com.kakaobean.project.dto.request.ModifyProjectMemberRoleRequest;
import com.kakaobean.project.dto.request.ModifyProjectMembersRolesRequest;
import com.kakaobean.project.dto.request.RegisterProjectMemberRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static com.kakaobean.unit.controller.factory.project.FindProjectMemberResponseDtoFactory.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectMemberControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 프로젝트_멤버_참여_api_테스트() throws Exception {

        //given
        RegisterProjectMemberRequest request = new RegisterProjectMemberRequest("dd361ee9-2381-44be-af12-5b23f265d199");
        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/projects/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_project_member",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("projectSecretKey").type(STRING).description("프로젝트 참여 비밀 키")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("성공 메시지")
                )
        ));
    }


    @Test
    @WithMockUser
    void 프로젝트_멤버_초대_이메일_발송_api_테스트() throws Exception {

        //given
        InviteProjectMemberRequest request = new InviteProjectMemberRequest(List.of("example@gmail.com", "test@gmail.com"));
        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/projects/{projectId}/invitation", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("send_project_member_invitation_mail",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("invitedMemberEmails").type(ARRAY).description("프로젝트에 초대할 멤버 이메일")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("성공 메시지")
                )
        ));
    }

    @Test
    @WithMockUser
    void 프로젝트_멤버_조회_api_테스트() throws Exception {

        //given
        given(projectQueryRepository.findProjectMembers(Mockito.anyLong()))
                .willReturn(createList());

        //when
        ResultActions perform = mockMvc.perform(get("/projects/{projectId}/members", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_project_members",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("projectId").description("조회할 프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("[].projectMemberId").type(NUMBER).description("프로젝트 멤버 id"),
                        fieldWithPath("[].projectMemberName").type(STRING).description("프로젝트 멤버 이름"),
                        fieldWithPath("[].projectMemberEmail").type(STRING).description("프로젝트 멤버 이메일"),
                        fieldWithPath("[].projectMemberRole").type(STRING).description("프로젝트 멤버 역할")
                )
        ));
    }


    @Test
    @WithMockUser
    void 프로젝트_멤버_역할_수정_api_테스트() throws Exception {

        //given
        ModifyProjectMembersRolesRequest request = new ModifyProjectMembersRolesRequest(
                List.of(
                        new ModifyProjectMemberRoleRequest(3L, ProjectRole.ADMIN),
                        new ModifyProjectMemberRoleRequest(4L, ProjectRole.MEMBER),
                        new ModifyProjectMemberRoleRequest(5L, ProjectRole.LEFT_MEMBER)
                )
        );
        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/projects/{projectId}/members", 4L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("modify_project_members",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("projectId").description("조회할 프로젝트 id")
                ),
                requestFields(
                        fieldWithPath("modifyProjectMemberRole").type(ARRAY).description("프로젝트 멤버 역할 변경 정보 배열"),
                        fieldWithPath("modifyProjectMemberRole[].modifyProjectMemberId").type(NUMBER).description("변경할 프로젝트 멤버 id"),
                        fieldWithPath("modifyProjectMemberRole[].projectRole").type(STRING).description("변경할 프로젝트 멤버 권한")

                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("성공 메시지")
                )
        ));
    }
}
