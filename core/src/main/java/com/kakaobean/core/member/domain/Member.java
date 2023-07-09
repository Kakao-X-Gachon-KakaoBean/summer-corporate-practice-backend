package com.kakaobean.core.member.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.member.domain.service.ModifyMemberService;
import com.kakaobean.core.member.domain.service.VerifiedEmailService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.net.PasswordAuthentication;
import java.time.LocalDate;

@Getter
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE member SET status = INACTIVE WHERE id = ?")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Auth auth;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;


    /**
     * 프로덕션 생성자
     */
    public Member(String name,
                  String email,
                  Role role,
                  String password,
                  AuthProvider authProvider,
                  BaseStatus baseStatus) {
        super(baseStatus);
        this.name = name;
        this.auth = new Auth(email, password);
        this.role = role;
        this.authProvider = authProvider;
    }

    /**
     * 테스트 코드를 위한 생성자.
     */
    @Builder
    public Member(Long id,
                  String name,
                  Auth auth,
                  Role role,
                  AuthProvider authProvider
    ) {
        super(BaseStatus.ACTIVE);
        this.id = id;
        this.name = name;
        this.auth = auth;
        this.role = role;
        this.authProvider = authProvider;
    }

    public void validate(MemberValidator memberValidator) {
        memberValidator.validate(auth.getEmail());
    }

    public void verifyEmail(VerifiedEmailService memberVerifiedEmailService,
                            String authKey){
        memberVerifiedEmailService.verifyAuthEmailKey(auth.getEmail(), authKey);
    }

    public void modifyPassword(ModifyMemberService modifyMemberService,
                               String passwordToChange,
                               String checkPasswordToChange) {
        modifyMemberService.modifyPassword(this, passwordToChange, checkPasswordToChange);
    }

    public void updatePassword(String newPassword) {
        this.auth = new Auth(this.auth.getEmail(), newPassword);
    }
}
