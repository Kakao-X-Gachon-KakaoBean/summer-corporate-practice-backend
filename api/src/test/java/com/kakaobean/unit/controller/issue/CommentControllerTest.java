package com.kakaobean.unit.controller.issue;

import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.issue.RegisterCommentRequestFactory;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
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
    void 댓글_생성_테스트() throws Exception{
        // given
        RegisterCommentRequest request = RegisterCommentRequestFactory.create();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(post("/comments")
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
                        fieldWithPath("content").type(STRING).description("댓글 내용"),
                        fieldWithPath("issueId").type(NUMBER).description("이슈 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("댓글이 생성되었습니다.")
                )
        ));
    }
}
