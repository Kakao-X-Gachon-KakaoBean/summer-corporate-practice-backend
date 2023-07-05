package com.kakaobean.unit.controller.project;

import com.kakaobean.core.project.application.dto.request.RegisterProjectRequestDto;
import com.kakaobean.core.project.application.dto.response.RegisterProjectResponseDto;
import com.kakaobean.project.dto.request.RegisterProjectRequest;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
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

    @Test
    @WithMockUser
    void 프로젝트_생성_api_테스트() throws Exception{
        // given
        RegisterProjectRequest request = new RegisterProjectRequest("프로젝트 이름", "프로젝트 설명");
        String requestBody = objectMapper.writeValueAsString(request);
        given(projectService.registerProject(Mockito.any(RegisterProjectRequestDto.class)))
                .willReturn(new RegisterProjectResponseDto(1L));

        // when
        ResultActions perform = mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        // then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_projects",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("projectId").type(NUMBER).description("생성된 프로젝트 id")
                )
        ));

    }
}
