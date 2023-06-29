package com.kakaobean.core.member.application.dto.request;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.member.domain.AuthProvider;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RegisterMemberRequestDto {

    private final String name;
    private final String email;
    private final String password;
    private final String emailAuthKey;

    @Builder
    public RegisterMemberRequestDto(String name,
                                    String email,
                                    String password,
                                    String emailAuthKey) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.emailAuthKey = emailAuthKey;
    }

    public Member toEntity(){
        return new Member(
                name,
                email,
                Role.ROLE_USER,
                password,
                AuthProvider.local,
                BaseStatus.ACTIVE
        );
    }
}
