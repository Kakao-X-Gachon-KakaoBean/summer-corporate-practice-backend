package com.kakaobean.core.member.application;

import com.kakaobean.core.member.application.dto.response.FindEmailResponseDto;
import com.kakaobean.core.member.application.dto.response.FindMemberInfoResponseDto;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.exception.member.NotExistsMemberException;
import com.kakaobean.core.member.exception.member.NotExistsMembersInfoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberProvider {

    private final MemberRepository memberRepository;


    public FindMemberInfoResponseDto findMemberInfoByMemberId(Long memberId){
        Member member = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new NotExistsMembersInfoException());
        return FindMemberInfoResponseDto.returnInfo(member);
    }

}
