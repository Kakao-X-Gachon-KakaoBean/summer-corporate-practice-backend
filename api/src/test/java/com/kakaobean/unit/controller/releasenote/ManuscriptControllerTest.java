package com.kakaobean.unit.controller.releasenote;


import com.kakaobean.core.releasenote.application.dto.response.ManuscriptResponseDto;
import com.kakaobean.core.releasenote.domain.ManuscriptStatus;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindManuscriptsResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindPagingManuscriptsResponseDto;
import com.kakaobean.releasenote.dto.request.ModifyManuscriptRequest;
import com.kakaobean.releasenote.dto.request.RegisterManuscriptRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
                        "1.1",
                        "23. 8. 3. 오후 12:17",
                        ManuscriptStatus.Modifiable
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
                        fieldWithPath("manuscriptVersion").type(STRING).description("릴리즈 노트 원고 버전"),
                        fieldWithPath("createdAt").type(STRING).description("릴리즈 노트 생성 날짜"),
                        fieldWithPath("manuscriptStatus").type(STRING).description("릴리즈 노트 수정 상태")
                )
        ));
    }

    @Test
    @WithMockUser
    void 페이징을_사용한_릴리즈_노트_원고_조회() throws Exception {

        given(manuscriptQueryRepository.findByProjectIdWithPaging(Mockito.anyLong(), Mockito.anyInt()))
                .willReturn(
                        new FindPagingManuscriptsResponseDto(
                                true,
                                List.of(
                                        new FindPagingManuscriptsResponseDto.ManuscriptDto(1L, "1.1V 릴리즈 노트", "1.1V"),
                                        new FindPagingManuscriptsResponseDto.ManuscriptDto(2L, "1.12V 릴리즈 노트", "1.2V")
                                        )
                        )
                );

        //when
        ResultActions perform = mockMvc.perform(get("/manuscripts/page")
                .param("projectId", "3")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_release_note_manuscripts_with_paging",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("projectId").description("릴리즈 노트 원고를 조회할 프로젝트 id"),
                        parameterWithName("page").description("페이징 커서")
                ),
                responseFields(
                        fieldWithPath("finalPage").type(BOOLEAN).description("마지막 페이지인가"),
                        fieldWithPath("manuscripts").type(ARRAY).description("릴리즈 노트 원고 리스트"),
                        fieldWithPath("manuscripts[].id").type(NUMBER).description("릴리즈 노트 원고 id"),
                        fieldWithPath("manuscripts[].title").type(STRING).description("릴리즈 노트 원고 제목"),
                        fieldWithPath("manuscripts[].version").type(STRING).description("릴리즈 노트 원고 버전")
                )
        ));
    }

    @Test
    @WithMockUser
    void 릴리즈_노트_원고_수정_권한_획득() throws Exception {

        given(manuscriptService.hasRightToModifyManuscript(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(new ManuscriptResponseDto(22L, "1.1V manuscript Title", "content..", "1.1V"));


        //when
        ResultActions perform = mockMvc.perform(patch("/manuscripts/{manuscriptId}/access-status", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("has_right_to_modify_manuscript",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("manuscriptId").description("수정 권한을 얻을 릴리즈 노트 원고 id")
                ),
                responseFields(
                        fieldWithPath("manuscriptId").type(NUMBER).description("릴리즈 노트 원고 id"),
                        fieldWithPath("manuscriptTitle").type(STRING).description("릴리즈 노트 원고 제목"),
                        fieldWithPath("manuscriptContent").type(STRING).description("릴리즈 노트 원고 내용"),
                        fieldWithPath("manuscriptVersion").type(STRING).description("릴리즈 노트 원고 버전")
                )
        ));
    }


    @Test
    @WithMockUser
    void 릴리즈_노트_원고_수정() throws Exception {

        ModifyManuscriptRequest request = new ModifyManuscriptRequest("1.9V 코코노트 초기 릴리즈 노트", "수정된 배포 내용", "1.9V");
        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(patch("/manuscripts/{manuscriptId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("modify_manuscript",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("manuscriptId").description("수정 권한을 얻을 릴리즈 노트 원고 id")
                ),
                requestFields(
                        fieldWithPath("title").type(STRING).description("수정할 제목"),
                        fieldWithPath("content").type(STRING).description("수정할 내용"),
                        fieldWithPath("version").type(STRING).description("수정할 버전")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("성공 메시지")
                )
        ));
    }


    @Test
    @WithMockUser
    void 릴리즈_노트_원고_삭제() throws Exception {

        //when
        ResultActions perform = mockMvc.perform(delete("/manuscripts/{manuscriptId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("delete_release_note_manuscript",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("manuscriptId").description("삭제할 릴리즈 노트 원고 id")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("성공 메시지")
                )
        ));
    }


    @Test
    @WithMockUser
    void 릴리즈_노트_원고_전체_조회() throws Exception {

        given(manuscriptQueryRepository.findAllByProjectId(Mockito.anyLong()))
                .willReturn(
                        new FindManuscriptsResponseDto(
                                List.of(
                                        new FindManuscriptsResponseDto.ManuscriptDto(1L, "1.1V 릴리즈 노트", "1.1V"),
                                        new FindManuscriptsResponseDto.ManuscriptDto(2L, "1.12V 릴리즈 노트", "1.2V")
                                )
                        )
                );

        //when
        ResultActions perform = mockMvc.perform(get("/manuscripts")
                .param("projectId", "3")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_release_note_manuscripts",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("projectId").description("릴리즈 노트 원고를 조회할 프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("manuscripts").type(ARRAY).description("릴리즈 노트 원고 리스트"),
                        fieldWithPath("manuscripts[].id").type(NUMBER).description("릴리즈 노트 원고 id"),
                        fieldWithPath("manuscripts[].title").type(STRING).description("릴리즈 노트 원고 제목"),
                        fieldWithPath("manuscripts[].version").type(STRING).description("릴리즈 노트 원고 버전")
                )
        ));
    }
}
