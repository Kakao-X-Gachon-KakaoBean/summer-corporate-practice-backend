package com.kakaobean.member;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.member.application.MemberProvider;
import com.kakaobean.core.member.application.MemberService;
import com.kakaobean.core.member.application.dto.response.FindEmailResponseDto;
import com.kakaobean.core.member.application.dto.response.FindMemberInfoResponseDto;
import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;
import com.kakaobean.member.dto.FindEmailRequest;
import com.kakaobean.member.dto.ModifyMemberPasswordRequest;
import com.kakaobean.member.dto.RegisterMemberRequest;

import com.kakaobean.member.dto.SendVerifiedEmailRequest;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Timed("api.member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberProvider memberProvider;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/members")
    public ResponseEntity<RegisterMemberResponseDto> registerMember(@Validated @RequestBody RegisterMemberRequest request){
        log.info("회원 가입 api 호출");
        memberService.registerMember(request.toServiceDto(passwordEncoder));
        log.info("회원 가입 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("회원가입에 성공했습니다."), OK);
    }

    @PostMapping("/emails")
    public ResponseEntity sendVerificationEmail(@RequestBody @Validated SendVerifiedEmailRequest request){
        log.info("인증 이메일 발송 api 호출");
        memberService.sendVerificationEmail(request.getEmail());
        log.info("인증 이메일 발송 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("인증 이메일 발송을 성공했습니다."), OK);
    }

    @GetMapping("/members/info")
    public ResponseEntity findMemberInfo(@AuthenticationPrincipal Long memberId) {
        log.info("멤버 정보 조회 api 호출");
        FindMemberInfoResponseDto res = memberProvider.findMemberInfoByMemberId(memberId);
        log.info("멤버 정보 조회 api 종료");
        return new ResponseEntity(res, OK);
    }

    @PatchMapping("/members/password")
    public ResponseEntity modifyMemberPassword(@RequestBody @Validated ModifyMemberPasswordRequest request){
        log.info("비밀번호 수정 api 호출");
        memberService.modifyMemberPassword(request.toServiceDto());
        log.info("비밀번호 수정 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("비밀번호 변경에 성공하셨습니다."), OK);
    }
}
