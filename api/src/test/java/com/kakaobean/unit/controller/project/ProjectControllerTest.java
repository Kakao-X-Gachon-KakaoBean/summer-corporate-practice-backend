package com.kakaobean.unit.controller.project;

import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.project.FindProjectResponseDtoFactory;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;


import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 내가_참여한_프로젝트_조회_api_테스트() throws Exception {

        //given
        given(projectQueryRepository.findProjects(Mockito.anyLong()))
                .willReturn(FindProjectResponseDtoFactory.createList());

        //when
        ResultActions perform = mockMvc.perform(get("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_projects",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("[].projectId").type(NUMBER).description("프로젝트 id"),
                        fieldWithPath("[].projectTitle").type(STRING).description("프로젝트 이름"),
                        fieldWithPath("[].projectContent").type(STRING).description("프로젝트 설명")
                )
        ));
    }
}
