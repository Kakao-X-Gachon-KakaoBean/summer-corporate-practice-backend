package com.kakaobean.security;


import com.kakaobean.core.member.exception.member.NotExistsEmailException;
import com.kakaobean.core.member.exception.member.NotExistsMemberException;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(
            String email
    ) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByEmail(email)
                .orElseThrow(NotExistsEmailException::new);

        return UserPrincipal.create(member);
    }

    @Transactional
    public UserDetails loadUserById(
            Long id
    ) {
        Member member = memberRepository.findMemberById(id).orElseThrow(NotExistsMemberException::new);
        return UserPrincipal.create(member);
    }
}