package com.kakaobean.unit.controller.issue;

import com.kakaobean.core.issue.application.dto.request.RegisterCommentRequestDto;
import com.kakaobean.core.issue.application.dto.request.RegisterIssueRequestDto;
import com.kakaobean.core.issue.application.dto.response.RegisterCommentResponseDto;
import com.kakaobean.core.issue.application.dto.response.RegisterIssueResponseDto;
import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.issue.dto.RegisterIssueRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest extends ControllerTest {
    @Test
    @WithMockUser
    void 댓글_생성_api_테스트() throws Exception{
        // given
        RegisterCommentRequest request = new RegisterCommentRequest("댓글 내용");
        String requestBody = objectMapper.writeValueAsString(request);
        given(commentService.registerComment(Mockito.any(RegisterCommentRequestDto.class)))
                .willReturn(new RegisterCommentResponseDto(1L));

        // when
        ResultActions perform = mockMvc.perform(post("/issues/{issueId}/comments", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        // then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_comment",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("content").type(STRING).description("댓글 내용")
                ),
                responseFields(
                        fieldWithPath("commentId").type(NUMBER).description("생성된 댓글 id")
                )
        ));
    }
}
