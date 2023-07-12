package com.kakaobean.core.member.infrastructure;

import com.kakaobean.core.member.domain.AuthProvider;
import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.service.ModifyMemberService;
import com.kakaobean.core.member.exception.member.*;
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

    private boolean passwordChangeValidationFailed(String passwordToChange,
                                                   String checkPasswordToChange) {
        return !passwordToChange.equals(checkPasswordToChange);
    }

    @Override
    public void modify(Member member, String newName){
        //로컬 로그인 유저만 이름 변경 가능
        if(member.getAuthProvider() != AuthProvider.local ){
            throw new OAuthMemberCanNotChangeNameException();
        }

        //기존 이름과 같은 이름으로 변경하려 할 때 exception
        if(nameChangeValidationFailed(member.getName(), newName)){
            throw new ChangingNameToSameNameException();
        }

        member.updateMemberName(newName);
    }

    //currentName 기존, newName이 신규
    private boolean nameChangeValidationFailed(String currentName, String newName) {
        return currentName.equals(newName);
    }
}
