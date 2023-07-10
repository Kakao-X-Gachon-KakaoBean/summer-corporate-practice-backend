package com.kakaobean.unit.controller.releasenote;


import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.releasenote.RegisterReleaseNoteRequestFactory;
import com.kakaobean.unit.controller.security.WithMockUser;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;


import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReleaseNoteControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 릴리즈_노트_작성() throws Exception {

        //given
        DeployReleaseNoteRequest request = RegisterReleaseNoteRequestFactory.create();
        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/release-note")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("deploy_release_note",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("title").type(STRING).description("릴리즈 노트 제목"),
                        fieldWithPath("content").type(STRING).description("릴리즈 노트 본문"),
                        fieldWithPath("version").type(NUMBER).description("릴리즈 노트 버전"),
                        fieldWithPath("projectId").type(NUMBER).description("릴리즈 노트를 포함한 프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("성공 메시지")
                )
        ));
    }
}
