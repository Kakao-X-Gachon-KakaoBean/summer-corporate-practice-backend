package com.kakaobean.core.member.infrastructure;

import com.kakaobean.core.member.domain.AuthProvider;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.service.ModifyMemberService;
import com.kakaobean.core.member.exception.member.OAuthMemberCanNotChangePasswordException;
import com.kakaobean.core.member.exception.member.PasswordAndCheckPasswordNotSameException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class ModifyMemberServiceImpl implements ModifyMemberService {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void modifyPassword(Member member, 
                               String passwordToChange,
                               String checkPasswordToChange) {

        if(member.getAuthProvider() != AuthProvider.local ){
            throw new OAuthMemberCanNotChangePasswordException();
        }

        if(passwordChangeValidationFailed(passwordToChange, checkPasswordToChange)){
            throw new PasswordAndCheckPasswordNotSameException();
        }

        member.updatePassword(passwordEncoder.encode(passwordToChange));
    }

    private boolean passwordChangeValidationFailed(String passwordToChange, String checkPasswordToChange) {
        return !passwordToChange.equals(checkPasswordToChange);
    }
}
