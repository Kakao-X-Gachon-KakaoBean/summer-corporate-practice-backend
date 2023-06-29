package com.kakaobean.member.dto;

import com.kakaobean.core.member.application.dto.request.RegisterMemberRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RegisterMemberRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String checkPassword;

    @NotBlank
    private String emailAuthKey;

    public RegisterMemberRequestDto toServiceDto(PasswordEncoder passwordEncoder){
        if(isSamePassword()){
            throw new RuntimeException("비밀번호가 다릅니다");
        }
        return new RegisterMemberRequestDto(
                name,
                email,
                passwordEncoder.encode(password),
                emailAuthKey
        );
    }

    private boolean isSamePassword() {
        return !password.equals(checkPassword);
    }

    @Builder
    public RegisterMemberRequest(String name,
                                 String email,
                                 String password,
                                 String checkPassword,
                                 String emailAuthKey
    ) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.checkPassword = checkPassword;
        this.emailAuthKey = emailAuthKey;
    }
}
