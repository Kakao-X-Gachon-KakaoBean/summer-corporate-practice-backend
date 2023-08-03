package com.kakaobean.unit.controller.sprint;

import com.kakaobean.sprint.dto.request.ChangeWorkStatusRequest;
import com.kakaobean.sprint.dto.request.ModifyTaskRequest;
import com.kakaobean.sprint.dto.request.RegisterTaskRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.sprint.FindTaskResponseDtoFactory;
import com.kakaobean.unit.controller.factory.sprint.ModifyTaskRequestFactory;
import com.kakaobean.unit.controller.factory.sprint.RegisterTaskRequestFactory;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static com.kakaobean.unit.controller.factory.sprint.FindAllSprintResponseDtoFactory.create;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskControllerTest extends ControllerTest {

    @Test
    @WithMockUser
    void 테스크_생성() throws Exception {
        // given
        RegisterTaskRequest request = RegisterTaskRequestFactory.create();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_tasks",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("taskTitle").type(STRING).description("테스크 제목"),
                        fieldWithPath("taskContent").type(STRING).description("테스크 본문"),
                        fieldWithPath("sprintId").type(NUMBER).description("테스크의 스프린트 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("테스크가 생성 되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 테스크_수정() throws Exception {
        // given
        ModifyTaskRequest request = ModifyTaskRequestFactory.createRequest();
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(patch("/tasks/{taskId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("modify_tasks",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("taskId").description("수정할 테스크의 id")
                ),
                requestFields(
                        fieldWithPath("taskTitle").type(STRING).description("수정된 테스크 제목"),
                        fieldWithPath("taskContent").type(STRING).description("수정된 테스크 본문"),
                        fieldWithPath("sprintId").type(NUMBER).description("수정된 테스크의 스프린트 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("테스크가 수정 되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 테스크_삭제() throws Exception {

        // when
        ResultActions perform = mockMvc.perform(delete("/tasks/{taskId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("remove_tasks",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("taskId").description("삭제할 테스크의 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("테스크가 삭제 되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 테스크_할당() throws Exception {

        // when
        ResultActions perform = mockMvc.perform(patch("/tasks/{taskId}/assignment/{memberId}",1L, 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("assign_tasks",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("taskId").description("할당할 테스크의 id"),
                        parameterWithName("memberId").description("할당할 멤버 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("테스크가 할당 되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 테스크_작업상태_변경() throws Exception {
        // given
        ChangeWorkStatusRequest request = new ChangeWorkStatusRequest("complete");
        String requestBody = objectMapper.writeValueAsString(request);

        // when
        ResultActions perform = mockMvc.perform(patch("/tasks/{taskId}/work-status",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("change_work_status",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("taskId").description("작업 상태를 변경할 테스크의 id")
                ),
                requestFields(
                        fieldWithPath("workStatus").type(STRING).description("변경할 작업 상태명")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("작업 상태가 변경되었습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    void 테스크_개별_조회() throws Exception {
        //given
        given(taskQueryRepository.findTask(Mockito.anyLong())).willReturn(FindTaskResponseDtoFactory.create());

        // when
        ResultActions perform = mockMvc.perform(get("/tasks/{taskId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_task",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("taskId").description("조회할 테스크 id")
                ),
                responseFields(
                        fieldWithPath("taskTitle").type(STRING).description("테스크 제목"),
                        fieldWithPath("taskContent").type(STRING).description("테스크 설명"),
                        fieldWithPath("workStatus").type(STRING).description("테스크 작업 상태"),
                        fieldWithPath("workerId").type(NUMBER).description("테스크 작업자 id"),
                        fieldWithPath("workerName").type(STRING).description("테스크 작업자 이름"),
                        fieldWithPath("workerThumbnailImg").type(STRING).description("테스크 작업자 프로필 이미지")
                )
        ));
    }
}