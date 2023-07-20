package com.kakaobean.unit.controller.issue;

import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import com.kakaobean.core.issue.application.dto.response.RegisterIssueResponseDto;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IssueControllerTest extends ControllerTest {

    public
    @Test
    @WithMockUser
    void 이슈_생성_api_테스트() throws Exception{
        // given
        RegisterIssueRequest request = new RegisterIssueRequest("이슈 이름", "이슈 설명");
        String requestBody = objectMapper.writeValueAsString(request);
        given(issueService.registerIssue(Mockito.any(RegisterIssueRequestDto.class)))
                .willReturn(new RegisterIssueResponseDto(1L));

        // when
        ResultActions perform = mockMvc.perform(post("/projects/{projectId}/issue", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        // then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_issue",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("title").type(STRING).description("이슈 이름"),
                        fieldWithPath("content").type(STRING).description("이슈 설명")
                ),
                responseFields(
                        fieldWithPath("issueId").type(NUMBER).description("생성된 이슈 id")
                )
        ));
    }

//    @Test
//    @WithMockUser
//    void 이슈_정보수정_api_테스트() throws Exception{
//        // given
//        ModifyProjectInfoRequest request = new ModifyProjectInfoRequest("새로운 프로젝트 제목", "새로운 프로젝트 설명");
//        String requestBody = objectMapper.writeValueAsString(request);
//
//        // when
//        ResultActions perform = mockMvc.perform(patch("/projects/{projectId}", 1L)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(requestBody)
//        );
//
//        // then
//        perform.andDo(print());
//        perform.andExpect(status().is2xxSuccessful());
//        perform.andDo(document("modify_projects_info",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                pathParameters(
//                        parameterWithName("projectId").description("수정할 프로젝트 id")
//                ),
//                requestFields(
//                        fieldWithPath("newTitle").type(STRING).description("수정할 프로젝트 이름"),
//                        fieldWithPath("newContent").type(STRING).description("수정할 프로젝트 설명")
//                ),
//                responseFields(
//                        fieldWithPath("message").type(STRING).description("프로젝트 정보가 변경 되었습니다.")
//                )
//        ));
//    }
//
//    @Test
//    @WithMockUser
//    void 프로젝트_삭제_api_테스트() throws Exception{
//
//        // when
//        ResultActions perform = mockMvc.perform(delete("/projects/{projectId}", 1L)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//        );
//
//        // then
//        perform.andDo(print());
//        perform.andExpect(status().is2xxSuccessful());
//        perform.andDo(document("remove_projects",
//                getDocumentRequest(),
//                getDocumentResponse(),
//                pathParameters(
//                        parameterWithName("projectId").description("삭제할 프로젝트 id")
//                ),
//                responseFields(
//                        fieldWithPath("message").type(STRING).description("프로젝트가 삭제 되었습니다.")
//                )
//        ));
//    }
}
