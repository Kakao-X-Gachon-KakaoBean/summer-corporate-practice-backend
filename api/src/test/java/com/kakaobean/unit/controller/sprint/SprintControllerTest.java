package com.kakaobean.unit.controller.sprint;

import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptsResponseDto;
import com.kakaobean.core.sprint.domain.repository.query.FindAllSprintResponseDto;
import com.kakaobean.core.sprint.domain.repository.query.SprintsDto;
import com.kakaobean.core.sprint.domain.repository.query.TasksDto;
import com.kakaobean.sprint.dto.request.ModifySprintRequest;
import com.kakaobean.sprint.dto.request.RegisterSprintRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.sprint.FindAllSprintResponseDtoFactory;
import com.kakaobean.unit.controller.factory.sprint.ModifySprintRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterSprintRequestFactory;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static com.kakaobean.unit.controller.factory.sprint.FindAllSprintResponseDtoFactory.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SprintControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 스프린트_생성() throws Exception {
        // given
        RegisterSprintRequest request = RegisterSprintRequestFactory.createWithId();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(post("/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_sprint",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("title").type(STRING).description("스프린트 제목"),
                        fieldWithPath("description").type(STRING).description("스프린트 본문"),
                        fieldWithPath("projectId").type(NUMBER).description("스프린트 프로젝트 id"),
                        fieldWithPath("startDate").type(STRING).description("스프린트 시작 날짜"),
                        fieldWithPath("endDate").type(STRING).description("스프린트 마감 날짜")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("스프린트가 생성 되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 스프린트_수정() throws Exception {
        // given
        ModifySprintRequest request = ModifySprintRequestFactory.createRequest();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(patch("/sprints/{sprintId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("modify_sprint",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("sprintId").description("수정할 스프린트의 id")
                ),
                requestFields(
                        fieldWithPath("newTitle").type(STRING).description("수정된 스프린트 제목"),
                        fieldWithPath("newDescription").type(STRING).description("수정된 스프린트 본문"),
                        fieldWithPath("newStartDate").type(STRING).description("수정된 스프린트 시작 날짜"),
                        fieldWithPath("newEndDate").type(STRING).description("수정된 스프린트 마감 날짜")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("스프린트가 수정 되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 스프린트_삭제() throws Exception {

        // when
        ResultActions perform = mockMvc.perform(delete("/sprints/{sprintId}",1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("remove_sprint",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("sprintId").description("삭제할 스프린트의 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("스프린트가 삭제 되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 스프린트_전체_조회() throws Exception {
        //given
        given(sprintQueryRepository.findAllByProjectId(Mockito.anyLong())).willReturn(create());

        // when
        ResultActions perform = mockMvc.perform(get("/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .param("projectId", "1")
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_all_sprints",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("projectId").description("전체 스프린트를 조회할 프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("sprints").type(ARRAY).description("스프린트 리스트"),
                        fieldWithPath("sprints[].sprintId").type(NUMBER).description("스프린트 아이디"),
                        fieldWithPath("sprints[].sprintTitle").type(STRING).description("스프린트 이름"),
                        fieldWithPath("sprints[].startDate").type(STRING).description("스프린트 시작 날짜"),
                        fieldWithPath("sprints[].dueDate").type(STRING).description("스프린트 마감 날짜"),
                        fieldWithPath("sprints[].children").type(ARRAY).description("스프린트 테스크 리스트"),
                        fieldWithPath("sprints[].children[].taskId").type(NUMBER).description("테스크 아이디"),
                        fieldWithPath("sprints[].children[].taskTitle").type(STRING).description("테스크 이름")
                )
        ));
    }
}
