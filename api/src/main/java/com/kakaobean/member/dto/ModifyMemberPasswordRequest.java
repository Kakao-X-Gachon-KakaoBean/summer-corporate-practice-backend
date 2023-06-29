package com.kakaobean.member.dto;

import com.kakaobean.core.member.application.dto.request.ModifyMemberPasswordRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyMemberPasswordRequest {

    private String email;
    private String emailAuthKey;
    private String passwordToChange;
    private String checkPasswordToChange;

    public ModifyMemberPasswordRequest(String email,
                                       String emailAuthKey,
                                       String passwordToChange,
                                       String checkPasswordToChange) {
        this.email = email;
        this.emailAuthKey = emailAuthKey;
        this.passwordToChange = passwordToChange;
        this.checkPasswordToChange = checkPasswordToChange;
    }

    public ModifyMemberPasswordRequestDto toServiceDto() {
        return new ModifyMemberPasswordRequestDto(email, emailAuthKey, passwordToChange, checkPasswordToChange);
    }
}
