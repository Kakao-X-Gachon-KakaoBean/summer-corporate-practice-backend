package com.kakaobean.unit.controller.releasenote;


import com.kakaobean.core.releasenote.domain.repository.query.FindReleaseNoteResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindPagingReleaseNotesResponseDto;
import com.kakaobean.core.releasenote.domain.repository.query.FindReleaseNotesResponseDto;
import com.kakaobean.releasenote.dto.request.DeployReleaseNoteRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.fixture.releasenote.RegisterReleaseNoteRequestFactory;
import com.kakaobean.unit.controller.security.WithMockUser;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
        ResultActions perform = mockMvc.perform(post("/release-notes")
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
                        fieldWithPath("version").type(STRING).description("릴리즈 노트 버전"),
                        fieldWithPath("projectId").type(NUMBER).description("릴리즈 노트를 포함한 프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("id").type(NUMBER).description("배포한 릴리즈 노트 id"),
                        fieldWithPath("message").type(STRING).description("성공 메시지")
                )
        ));
    }

    @Test
    @WithMockUser
    void 릴리즈_노트_페이징을_사용해서_조회() throws Exception {

        given(releaseNoteQueryRepository.findByProjectId(Mockito.anyLong(), Mockito.anyInt()))
                .willReturn(new FindPagingReleaseNotesResponseDto(
                                true,
                                List.of(new FindPagingReleaseNotesResponseDto.ReleaseNoteDto(
                                                1L,
                                                "1.1V releaseNote title",
                                                "1.1V",
                                                "Contents...",
                                                "23. 8. 16. 오전 3:07"),
                                        new FindPagingReleaseNotesResponseDto.ReleaseNoteDto(
                                                2L,
                                                "1.2V releaseNote title",
                                                "1.2V",
                                                "Contents..",
                                                "23. 8. 16. 오전 3:07")
                                )
                        )
                );


        //when
        ResultActions perform = mockMvc.perform(get("/release-notes/page")
                .param("projectId", "0")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_release_notes_with_paging",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("projectId").description("릴리즈 노트 원고를 조회할 프로젝트 id"),
                        parameterWithName("page").description("페이징 커서")
                ),
                responseFields(
                        fieldWithPath("finalPage").type(BOOLEAN).description("마지막 페이지인가"),
                        fieldWithPath("releaseNotes").type(ARRAY).description("릴리즈 노트 리스트"),
                        fieldWithPath("releaseNotes[].id").type(NUMBER).description("릴리즈 노트 id"),
                        fieldWithPath("releaseNotes[].title").type(STRING).description("릴리즈 노트 제목"),
                        fieldWithPath("releaseNotes[].version").type(STRING).description("릴리즈 노트 버전"),
                        fieldWithPath("releaseNotes[].content").type(STRING).description("릴리즈 노트 내용"),
                        fieldWithPath("releaseNotes[].createdAt").type(STRING).description("릴리즈 노트 배포 날짜")
                )
        ));
    }


    @Test
    @WithMockUser
    void 릴리즈_노트_단_건_조회() throws Exception {

        given(releaseNoteQueryRepository.findById(Mockito.anyLong()))
                .willReturn(Optional.of(new FindReleaseNoteResponseDto(
                                1L,
                                "1.2V title",
                                "content..",
                                "1.2V",
                                "23. 7. 20. 오전 10:52"
                        ))
                );


        //when
        ResultActions perform = mockMvc.perform(get("/release-notes/{releaseNoteId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_release_note",
                getDocumentRequest(),
                getDocumentResponse(),
                pathParameters(
                        parameterWithName("releaseNoteId").description("조회할 릴리즈 노트의 id")
                ),
                responseFields(
                        fieldWithPath("releaseNoteId").type(NUMBER).description("릴리즈 노트 리스트"),
                        fieldWithPath("releaseNoteTitle").type(STRING).description("릴리즈 노트 id"),
                        fieldWithPath("releaseNoteContent").type(STRING).description("릴리즈 노트 제목"),
                        fieldWithPath("releaseNoteVersion").type(STRING).description("릴리즈 노트 버전"),
                        fieldWithPath("createdAt").type(STRING).description("릴리즈 노트 생성 날짜")
                )
        ));
    }


    @Test
    @WithMockUser
    void 릴리즈_노트_전체_조회() throws Exception {

        given(releaseNoteQueryRepository.findAllByProjectId(Mockito.anyLong()))
                .willReturn(new FindReleaseNotesResponseDto(
                                List.of(new FindReleaseNotesResponseDto.ReleaseNoteDto(
                                                1L,
                                                "1.1V releaseNote title",
                                                "1.1V"),
                                        new FindReleaseNotesResponseDto.ReleaseNoteDto(
                                                2L,
                                                "1.2V releaseNote title",
                                                "1.2V")
                                )
                        )
                );


        //when
        ResultActions perform = mockMvc.perform(get("/release-notes")
                .param("projectId", "0")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_release_notes",
                getDocumentRequest(),
                getDocumentResponse(),
                requestParameters(
                        parameterWithName("projectId").description("릴리즈 노트 원고를 조회할 프로젝트 id")
                ),
                responseFields(
                        fieldWithPath("releaseNotes").type(ARRAY).description("릴리즈 노트 리스트"),
                        fieldWithPath("releaseNotes[].id").type(NUMBER).description("릴리즈 노트 id"),
                        fieldWithPath("releaseNotes[].title").type(STRING).description("릴리즈 노트 제목"),
                        fieldWithPath("releaseNotes[].version").type(STRING).description("릴리즈 노트 버전"))
        ));
    }

}
