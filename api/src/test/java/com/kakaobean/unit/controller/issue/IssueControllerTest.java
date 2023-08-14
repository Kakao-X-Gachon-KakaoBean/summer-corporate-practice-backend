package com.kakaobean.unit.controller.issue;

import com.kakaobean.issue.dto.ModifyIssueRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.issue.FindIndividualIssueResponseDtoFactory;
import com.kakaobean.unit.controller.factory.issue.FindIssuesWithinPageResponseDtoFactory;
import com.kakaobean.unit.controller.factory.issue.ModifyIssueRequestFactory;
import com.kakaobean.unit.controller.factory.issue.RegisterIssueRequestFactory;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IssueControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 이슈_생성_테스트() throws Exception{
        // given
        RegisterIssueRequest request = RegisterIssueRequestFactory.create();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(post("/issues")
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
                        fieldWithPath("content").type(STRING).description("이슈 설명"),
                        fieldWithPath("projectId").type(NUMBER).description("프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("이슈가 생성되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 이슈_삭제() throws Exception {

        // when
        ResultActions perform = mockMvc.perform(delete("/issues/{issueId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("remove_issue",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("issueId").description("삭제할 이슈의 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("이슈가 삭제 되었습니다.")
                )
        ));
    }


    @Test
    @WithMockUser
    void 이슈_페이지_별_조회_테스트() throws Exception{

        // given
        given(issueQueryRepository.findByProjectId(Mockito.anyLong(), Mockito.anyInt()))
                .willReturn(FindIssuesWithinPageResponseDtoFactory.create());

        //when
        ResultActions perform = mockMvc.perform(get("/issues/page")
                .param("projectId", "1")
                .param("page", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_issues_with_paging",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("projectId").description("이슈를 조회할 프로젝트 id"),
                        parameterWithName("page").description("페이징 커서")
                ),
                responseFields(
                        fieldWithPath("finalPage").type(BOOLEAN).description("마지막 페이지인가"),
                        fieldWithPath("issues").type(ARRAY).description("이슈 리스트"),
                        fieldWithPath("issues[].id").type(NUMBER).description("이슈 id"),
                        fieldWithPath("issues[].title").type(STRING).description("이슈 제목"),
                        fieldWithPath("issues[].writerId").type(NUMBER).description("이슈 작성자"),
                        fieldWithPath("issues[].writerName").type(STRING).description("이슈 작성자 닉네임"),
                        fieldWithPath("issues[].writtenTime").type(STRING).description("이슈 작성 시간")
                )
        ));
    }

    @Test
    @WithMockUser
    void 이슈_개별_조회_테스트() throws Exception{

        // given
        given(issueQueryRepository.findByIssueId(Mockito.anyLong()))
                .willReturn(FindIndividualIssueResponseDtoFactory.create());

        //when
        ResultActions perform = mockMvc.perform(get("/issues/{issueId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_individual_issue",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("issueId").description("조회할 이슈 id")
                ),
                responseFields(
                        fieldWithPath("issue").type(OBJECT).description("이슈"),
                        fieldWithPath("issue.issueId").type(NUMBER).description("이슈 id"),
                        fieldWithPath("issue.title").type(STRING).description("이슈 제목"),
                        fieldWithPath("issue.content").type(STRING).description("이슈 내용"),
                        fieldWithPath("issue.writtenTime").type(STRING).description("이슈 작성 시간"),
                        fieldWithPath("issue.writerName").type(STRING).description("이슈 작성자 닉네임"),
                        fieldWithPath("issue.thumbnailImg").type(STRING).description("이슈 작성자의 프로필 섬네일 이미지"),
                        fieldWithPath("comments[].commentId").type(NUMBER).description("댓글 id"),
                        fieldWithPath("comments[].content").type(STRING).description("댓글 내용"),
                        fieldWithPath("comments[].writtenTime").type(STRING).description("댓글 작성 시간"),
                        fieldWithPath("comments[].writerName").type(STRING).description("댓글 작성자 닉네임"),
                        fieldWithPath("comments[].thumbnailImg").type(STRING).description("댓글 작성자의 프로필 섬네일 이미지")
                )
        ));
    }

    @Test
    @WithMockUser
    void 이슈_수정() throws Exception {
        // given
        ModifyIssueRequest request = ModifyIssueRequestFactory.createRequest();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(patch("/issues/{issueId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("modify_Issue",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("issueId").description("수정할 이슈의 id")
                ),
                requestFields(
                        fieldWithPath("title").type(STRING).description("수정된 이슈 제목"),
                        fieldWithPath("content").type(STRING).description("수정된 이슈 본문")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("이슈가 수정 되었습니다.")
                )
        ));
    }
}
