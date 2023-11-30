package com.kakaobean.member;

import com.kakaobean.common.RandomUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


import static com.kakaobean.common.RandomUtils.*;
import static org.springframework.http.HttpStatus.*;

@Timed("api.member")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberProvider memberProvider;

    private final PasswordEncoder passwordEncoder;

    @PostMapping("/members")
    public ResponseEntity<RegisterMemberResponseDto> registerMember(@Validated @RequestBody RegisterMemberRequest request) {
        log.info("멤버 등록 api 시작");
        Long memberId = memberService.registerMember(request.toServiceDto(passwordEncoder));
        log.info("멤버 등록 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from(memberId, "회원가입에 성공했습니다."), OK);
    }

    @PostMapping("/emails")
    public ResponseEntity sendVerificationEmail(@RequestBody @Validated SendVerifiedEmailRequest request) {
        log.info("인증 이메일 발송 api 시작");
        memberService.sendVerificationEmail(request.getEmail(), creatRandomKey());
        log.info("인증 이메일 발송 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("인증 이메일 발송을 성공했습니다."), OK);
    }

    @GetMapping("/members/info")
    public ResponseEntity findMemberInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("멤버 정보 조회 api 시작");
        FindMemberInfoResponseDto res = memberProvider.findMemberInfoByMemberId(userPrincipal.getId());
        log.info("멤버 정보 조회 api 종료");
        return new ResponseEntity(res, OK);
    }

    @PatchMapping("/members/password")
    public ResponseEntity modifyMemberPassword(@RequestBody @Validated ModifyMemberPasswordRequest request) {
        log.info("비밀번호 수정 api 시작");
        memberService.modifyMemberPassword(request.toServiceDto());
        log.info("비밀번호 수정 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("비밀번호 변경에 성공하셨습니다."), OK);
    }

    @PatchMapping("/members/name")
    public ResponseEntity modifyMember(@RequestBody @Validated ModifyMemberRequest request,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("멤버 수정 api 시작");
        memberService.modifyMember(request.toServiceDto(userPrincipal.getId()));
        log.info("멤버 수정 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("멤버 이름 변경에 성공하셨습니다."), OK);
    }

    @PostMapping("/members/images")
    public ResponseEntity updateProfileImages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @RequestParam MultipartFile profileImg,
                                              @RequestParam MultipartFile thumbnailImg) throws IOException {
        log.info("멤버 프로필 이미지 업로드 api 시작");
        memberService.uploadProfileImages(userPrincipal.getId(), profileImg, thumbnailImg);
        log.info("멤버 프로필 이미지 업로드 api 종료");
        return new ResponseEntity(CommandSuccessResponse.from("프로필 이미지 업로드를 성공하셨습니다."), OK);
    }
}
