package com.kakaobean.core.member.domain.service;

import com.kakaobean.core.member.domain.Member;

public interface ModifyMemberService {

    void modifyPassword(Member member,
                        String passwordToChange,
                        String checkPasswordToChange);

    void modifyName(Member member, String newName);
}
