package com.kakaobean.core.member.application.dto.request;

import lombok.Getter;

@Getter
public class ModifyMemberPasswordRequestDto {

    private String email;
    private String emailAuthKey;
    private String passwordToChange;
    private String checkPasswordToChange;

    public ModifyMemberPasswordRequestDto(String email,
                                          String emailAuthKey,
                                          String passwordToChange,
                                          String checkPasswordToChange) {
        this.email = email;
        this.emailAuthKey = emailAuthKey;
        this.passwordToChange = passwordToChange;
        this.checkPasswordToChange = checkPasswordToChange;
    }
}
