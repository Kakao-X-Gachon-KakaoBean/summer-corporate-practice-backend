package com.kakaobean.core.member.application;

import com.kakaobean.core.member.application.dto.request.ModifyMemberPasswordRequestDto;
import com.kakaobean.core.member.application.dto.request.ModifyMemberRequestDto;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.MemberValidator;
import com.kakaobean.core.member.application.dto.request.RegisterMemberRequestDto;
import com.kakaobean.core.member.domain.service.ModifyMemberService;
import com.kakaobean.core.member.domain.service.VerifiedEmailService;

import com.kakaobean.core.member.exception.member.NotExistsMemberException;
import com.kakaobean.independentlysystem.image.ImageService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator;
    private final VerifiedEmailService memberVerifiedEmailService;
    private final ModifyMemberService modifyMemberService;
    private final ImageService imageService;

    @Transactional(readOnly = false)
    public void registerMember(RegisterMemberRequestDto dto){
        Member member = dto.toEntity();
        member.validate(memberValidator);
        member.verifyEmail(memberVerifiedEmailService, dto.getEmailAuthKey());
        memberRepository.save(member);
    }

    public void sendVerificationEmail(String email, String authKey) {
        memberVerifiedEmailService.sendVerificationEmail(email, authKey);
    }

    @Transactional(readOnly = false)
    public void modifyMemberPassword(ModifyMemberPasswordRequestDto dto){
        Member member = memberRepository.findMemberByEmail(dto.getEmail()).orElseThrow(NotExistsMemberException::new);
        member.verifyEmail(memberVerifiedEmailService, dto.getEmailAuthKey());
        member.modifyPassword(modifyMemberService, dto.getPasswordToChange(), dto.getCheckPasswordToChange());
    }

    @Transactional
    public void modifyMemberInfo(ModifyMemberRequestDto dto){
        Member member = memberRepository.findMemberById(dto.getMemberId()).orElseThrow(NotExistsMemberException::new);
        member.modifyName(modifyMemberService, dto.getNameToChange());
    }

    public void uploadProfileImages(Long id,
                                    MultipartFile profileImg,
                                    MultipartFile thumbnailImg) throws IOException {
        String profileImgUrl = imageService.upload(profileImg);
        String thumbnailImgUrl = imageService.upload(thumbnailImg);
        Member member = memberRepository.findById(id).orElseThrow(NotExistsMemberException::new);
        member.modifyProfileImg(profileImgUrl, thumbnailImgUrl);
        memberRepository.save(member);
    }
}
