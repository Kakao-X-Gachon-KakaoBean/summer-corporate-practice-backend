package com.kakaobean.core.member.application;

import com.kakaobean.core.member.application.dto.request.ModifyMemberPasswordRequestDto;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.MemberValidator;
import com.kakaobean.core.member.application.dto.request.RegisterMemberRequestDto;
import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;
import com.kakaobean.core.member.domain.service.ModifyMemberService;
import com.kakaobean.core.member.domain.service.VerifiedEmailService;

import com.kakaobean.core.member.exception.member.NotExistsMemberException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final VerifiedEmailService memberVerifiedEmailService;
    private final ModifyMemberService modifyMemberService;

    @Transactional(readOnly = false)
    public RegisterMemberResponseDto registerMember(RegisterMemberRequestDto dto){
        Member member = dto.toEntity();
        member.validate(memberValidator);
        member.verifyEmail(memberVerifiedEmailService, dto.getEmailAuthKey());
        Member savedMember = memberRepository.save(member);
        return new RegisterMemberResponseDto(savedMember.getId());
    }

    public void sendVerificationEmail(String email) {
        memberVerifiedEmailService.sendVerificationEmail(email);
    }

    @Transactional(readOnly = false)
    public void modifyMemberPassword(ModifyMemberPasswordRequestDto dto){
        Member member = memberRepository.findMemberByEmail(dto.getEmail()).orElseThrow(NotExistsMemberException::new);
        member.verifyEmail(memberVerifiedEmailService, dto.getEmailAuthKey());
        member.modifyPassword(modifyMemberService, dto.getPasswordToChange(), dto.getCheckPasswordToChange());
    }
}
