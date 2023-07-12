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
import java.util.List;

import static com.kakaobean.common.RandomUtils.*;
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
        memberService.sendVerificationEmail(request.getEmail(), creatRandomKey());
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

    @PatchMapping("/members/name")
    public ResponseEntity modifyMember(@RequestBody @Validated ModifyMemberRequest request, @AuthenticationPrincipal UserPrincipal userPrincipal){
        memberService.modifyMember(request.toServiceDto(userPrincipal.getId()));
        return new ResponseEntity(CommandSuccessResponse.from("멤버 이름 변경에 성공하셨습니다."), OK);
    }

    @PostMapping("/members/images")
    public ResponseEntity updateProfileImages(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                              @RequestParam MultipartFile profileImg,
                                              @RequestParam MultipartFile thumbnailImg) throws IOException {
        memberService.uploadProfileImages(userPrincipal.getId(), profileImg, thumbnailImg);
        return new ResponseEntity(CommandSuccessResponse.from("프로필 이미지 업로드를 성공하셨습니다."), OK);
    }
}
