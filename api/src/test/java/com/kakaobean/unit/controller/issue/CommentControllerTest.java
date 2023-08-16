package com.kakaobean.unit.controller.issue;

import com.kakaobean.issue.dto.ModifyCommentRequest;
import com.kakaobean.issue.dto.RegisterCommentRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.issue.ModifyCommentRequestFactory;
import com.kakaobean.unit.controller.factory.issue.RegisterCommentRequestFactory;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
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

    @Test
    @WithMockUser
    void 댓글_수정_테스트() throws Exception{
        //when
        ModifyCommentRequest request = ModifyCommentRequestFactory.createRequest();
        String requestBody = objectMapper.writeValueAsString(request);

        ResultActions perform = mockMvc.perform(delete("/comments/{commentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("modify_comment",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("commentId").description("수정할 댓글 id")
                ),
                requestFields(
                        fieldWithPath("content").type(STRING).description("수정된 댓글 내용")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("댓글이 수정되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 댓글_삭제_테스트() throws Exception{
        //when
        ResultActions perform = mockMvc.perform(delete("/comments/{commentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("remove_comment",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("commentId").description("삭제할 댓글 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("댓글이 삭제되었습니다.")
                )
        ));
    }
}
