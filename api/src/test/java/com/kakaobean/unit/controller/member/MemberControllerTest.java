package com.kakaobean.unit.controller.member;

import com.kakaobean.core.member.application.dto.response.FindEmailResponseDto;
import com.kakaobean.core.member.application.dto.response.FindMemberInfoResponseDto;
import com.kakaobean.member.dto.FindEmailRequest;
import com.kakaobean.member.dto.ModifyMemberPasswordRequest;
import com.kakaobean.member.dto.SendVerifiedEmailRequest;
import com.kakaobean.unit.controller.ControllerTest;
import com.kakaobean.unit.controller.factory.member.RegisterMemberRequestFactory;
import com.kakaobean.core.member.application.dto.request.RegisterMemberRequestDto;
import com.kakaobean.member.dto.RegisterMemberRequest;
import com.kakaobean.unit.controller.security.WithMockUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentRequest;
import static com.kakaobean.docs.SpringRestDocsUtils.getDocumentResponse;

import static org.mockito.BDDMockito.*;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends ControllerTest {

    @Test
    @DisplayName("멤버 등록 API 명세서 테스트")
    void registerMemberTest() throws Exception {

        //given
        RegisterMemberRequest request = RegisterMemberRequestFactory.createRequest();
        String requestBody = objectMapper.writeValueAsString(request);
        given(memberService.registerMember(Mockito.any(RegisterMemberRequestDto.class)))
                        .willReturn(RegisterMemberRequestFactory.createResponseDto());

        //when
        ResultActions perform = mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("register_member",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("name").type(STRING).description("이름"),
                        fieldWithPath("age").type(NUMBER).description("나이"),
                        fieldWithPath("gender").type(STRING).description("성별"),
                        fieldWithPath("email").type(STRING).description("이메일"),
                        fieldWithPath("emailAuthKey").type(STRING).description("이메일 인증 키"),
                        fieldWithPath("password").type(STRING).description("비밀번호"),
                        fieldWithPath("checkPassword").type(STRING).description("비밀번호 확인"),
                        fieldWithPath("birth").type(STRING).description("생일")
                ),
                responseFields(
                        fieldWithPath("memberId").type(NUMBER).description("등록한 회원 id")
                )
        ));
    }

    @Test
    @DisplayName("인증 메일 요청 API 명세서 테스트")
    void sendVerifiedEmail() throws Exception {

        //given
        SendVerifiedEmailRequest request = new SendVerifiedEmailRequest("gachon@gmail.com");
        String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions perform = mockMvc.perform(post("/emails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("send_verified_email",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("email").type(STRING).description("인증 키를 받을 이메일")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("요청을 성공하셨습니다.")
                )
        ));
    }

    @Test
    @WithMockUser
    @DisplayName("멤버 정보 API 명세서 테스트.")
    void findMemberInfo() throws Exception {

        //given
        given(memberProvider.findMemberInfoByMemberId(1L))
                .willReturn(new FindMemberInfoResponseDto("조연겸", "whdusrua@naver.com"));

        //when
        ResultActions perform = mockMvc.perform(get("/members/info")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("find_member_info",
                getDocumentResponse(),
                responseFields(
                        fieldWithPath("name").type(STRING).description("찾은 멤버 이름"),
                        fieldWithPath("age").type(NUMBER).description("찾은 멤버 나이"),
                        fieldWithPath("gender").type(STRING).description("찾은 멤버 성별"),
                        fieldWithPath("email").type(STRING).description("찾은 멤버 이메일"),
                        fieldWithPath("birth").type(STRING).description("찾은 멤버 생일")
                )
        ));
    }


    @Test
    @WithMockUser
    @DisplayName("멤버 비밀번호 수정")
    void modifyMemberPassword() throws Exception {

        ModifyMemberPasswordRequest request =
                new ModifyMemberPasswordRequest("123@gmail.com", "111336", "1q2w3e4r!", "1q2w3e4r!");
        String requestBody = objectMapper.writeValueAsString(request);


        //when
        ResultActions perform = mockMvc.perform(patch("/members/password")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );

        //then
        perform.andDo(print());
        perform.andExpect(status().is2xxSuccessful());
        perform.andDo(document("modify_member_password",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("email").type(STRING).description("인증할 본인 이메일"),
                        fieldWithPath("emailAuthKey").type(STRING).description("이메일 검증 키"),
                        fieldWithPath("passwordToChange").type(STRING).description("변경할 비밀번호"),
                        fieldWithPath("checkPasswordToChange").type(STRING).description("변경할 비밀번호를 체크할 비밀번호")
                ),
                responseFields(
                        fieldWithPath("message").type(STRING).description("성공메시지")
                )
        ));
    }
}
