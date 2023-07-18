package com.kakaobean.unit.controller.releasenote;


import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptResponseDto;
import com.kakaobean.releasenote.dto.request.RegisterManuscriptRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ManuscriptControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 릴리즈_노트_원고_작성() throws Exception {

        //given
        RegisterManuscriptRequest request = new RegisterManuscriptRequest(
                "코코노트 1.1",
                "hello",
                "1.1",
                2L
        );
        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/manuscripts")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_release_note_manuscript",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("title").type(STRING).description("릴리즈 노트 원고 제목"),
                        fieldWithPath("content").type(STRING).description("릴리즈 노트 원고 본문"),
                        fieldWithPath("version").type(STRING).description("릴리즈 노트 원고 버전"),
                        fieldWithPath("projectId").type(NUMBER).description("릴리즈 노트 원고를 포함한 프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("성공 메시지")
                )
        ));
    }


    @Test
    @WithMockUser
    void 릴리즈_노트_원고_단건_조회() throws Exception {

        given(manuscriptQueryRepository.findById(Mockito.anyLong())).willReturn(
                Optional.of(new FindManuscriptResponseDto(
                        "마지막 작성자",
                        1L,
                        "1.1V 릴리즈 노트",
                        "내용..",
                        "1.1"
                )));

        //when
        ResultActions perform = mockMvc.perform(get("/manuscripts/{manuscriptId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_release_note_manuscript",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("manuscriptId").description("조회할 릴리즈 노트 원고 id")
                ),
                responseFields(
                        fieldWithPath("lastEditedMemberName").type(STRING).description("마지막에 수정한 멤버 이름"),
                        fieldWithPath("manuscriptId").type(NUMBER).description("릴리즈 노트 원고 id"),
                        fieldWithPath("manuscriptTitle").type(STRING).description("릴리즈 노트 원고 제목"),
                        fieldWithPath("manuscriptContent").type(STRING).description("릴리즈 노트 원고 내용"),
                        fieldWithPath("manuscriptVersion").type(STRING).description("릴리즈 노트 원고 버전")
                )
        ));
    }
}
