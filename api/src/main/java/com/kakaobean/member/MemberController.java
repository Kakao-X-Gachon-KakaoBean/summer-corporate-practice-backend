package com.kakaobean.member;

import com.kakaobean.common.dto.CommandSuccessResponse;
import com.kakaobean.core.member.application.MemberProvider;
import com.kakaobean.core.member.application.MemberService;
import com.kakaobean.core.member.application.dto.response.FindEmailResponseDto;
import com.kakaobean.core.member.application.dto.response.FindMemberInfoResponseDto;
import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;
import com.kakaobean.member.dto.*;

import com.kakaobean.security.UserPrincipal;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

//@Timed("api.member")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberProvider memberProvider;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/members")
    public ResponseEntity<RegisterMemberResponseDto> registerMember(@Validated @RequestBody RegisterMemberRequest request){
        memberService.registerMember(request.toServiceDto(passwordEncoder));
        return new ResponseEntity(CommandSuccessResponse.from("회원가입에 성공했습니다."), OK);
    }

    @PostMapping("/emails")
    public ResponseEntity sendVerificationEmail(@RequestBody @Validated SendVerifiedEmailRequest request){
        memberService.sendVerificationEmail(request.getEmail());
        return new ResponseEntity(CommandSuccessResponse.from("인증 이메일 발송을 성공했습니다."), OK);
    }

    @GetMapping("/members/info")
    public ResponseEntity findMemberInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        FindMemberInfoResponseDto res = memberProvider.findMemberInfoByMemberId(userPrincipal.getId());
        return new ResponseEntity(res, OK);
    }

    @PatchMapping("/members/password")
    public ResponseEntity modifyMemberPassword(@RequestBody @Validated ModifyMemberPasswordRequest request){
        memberService.modifyMemberPassword(request.toServiceDto());
        return new ResponseEntity(CommandSuccessResponse.from("비밀번호 변경에 성공하셨습니다."), OK);
    }

    //TODO:
    // public ResponseEntity modifyMemberName(){}
    @PatchMapping("/members/name")
    public ResponseEntity modifyMemberName(@RequestBody @Validated ModifyMemberRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal){
        memberService.modifyMemberName(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("멤버 이름 변경에 성공하셨습니다."), OK);
    }
}
