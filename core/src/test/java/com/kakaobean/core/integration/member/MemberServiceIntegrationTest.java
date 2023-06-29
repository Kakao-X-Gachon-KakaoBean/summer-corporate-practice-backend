package com.kakaobean.core.integration.member;

import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.member.application.MemberProvider;
import com.kakaobean.core.member.application.dto.request.ModifyMemberPasswordRequestDto;
import com.kakaobean.core.member.application.dto.response.FindEmailResponseDto;
import com.kakaobean.core.member.application.dto.response.FindMemberInfoResponseDto;
import com.kakaobean.core.member.domain.*;
import com.kakaobean.core.member.domain.Email;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.exception.member.*;
import com.kakaobean.core.factory.member.RegisterMemberServiceDtoFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.member.application.MemberService;
import com.kakaobean.core.member.application.dto.request.RegisterMemberRequestDto;
import com.kakaobean.core.member.application.dto.response.RegisterMemberResponseDto;
import com.kakaobean.independentlysystem.email.EmailSender;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class MemberServiceIntegrationTest extends IntegrationTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EmailRepository emailRepository;

    @MockBean
    EmailSender mailSender;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    MemberProvider memberProvider;

    PasswordEncoder passwordEncoder;


    @BeforeEach
    void beforeEach(){
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.flushDb();
            return null;
        });
        this.passwordEncoder = new BCryptPasswordEncoder(); //임시 처리
    }

    @DisplayName("멤버를 등록한다.")
    @Test
    void registerMember(){
        //given
        RegisterMemberRequestDto dto = RegisterMemberServiceDtoFactory.createSuccessCaseRequestDto();
        emailRepository.save(new Email(dto.getEmail(), dto.getEmailAuthKey()));

        //when
        RegisterMemberResponseDto res = memberService.registerMember(dto);

        //then
        assertThat(res.getMemberId()).isNotNull();
    }

    @DisplayName("이미 등록된 이메일이면 멤버를 등록할 수 없다.")
    @Test
    void failRegisterMember(){
        //given
        RegisterMemberRequestDto dto = RegisterMemberServiceDtoFactory.createSuccessCaseRequestDto();
        memberRepository.save(dto.toEntity());

        //when, then
        assertThatThrownBy(() -> {
            memberService.registerMember(dto);
        })
                .isInstanceOf(AlreadyExistsEmailException.class)
                .hasMessage("이미 존재하는 유저의 이메일입니다.");
    }

    @DisplayName("인증 이메일을 성공적으로 보낼 수 있고, 인증 값은 레디스에 저장된다.")
    @Test
    void sendVerifiedEmail(){
        //given
        String email = "example@gmail.com";
        String authKey = "113336";
        given(mailSender.sendVerificationEmail(Mockito.any(String.class))).willReturn(authKey);

        //when
        memberService.sendVerificationEmail(email);

        //then
        Email result = emailRepository.getEmailCertification(new Email(email, authKey));
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getAuthKey()).isEqualTo(authKey);
    }

    @DisplayName("재인증 이메일을 성공적으로 보낼 수 있고, 새로운 인증 키도 레디스에 저장된다.")
    @Test
    void sendVerifiedEmailAgain(){
        //given
        String email = "example@gmail.com";
        String authKey = "113336";
        String authKey2 = "112233";
        given(mailSender.sendVerificationEmail(Mockito.any(String.class))).willReturn(authKey, authKey2);
        memberService.sendVerificationEmail(email);

        //when
        memberService.sendVerificationEmail(email);

        //then
        Email result = emailRepository.getEmailCertification(new Email(email, authKey2));
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getAuthKey()).isEqualTo(authKey2);
    }

    @DisplayName("레디스에 저장된 이메일과는 다른 이메일로 회원 가입을 진행한다.")
    @Test
    void failRegisterMemberCase1(){
        //given
        RegisterMemberRequestDto dto = RegisterMemberServiceDtoFactory.createSuccessCaseRequestDto();
        given(mailSender.sendVerificationEmail(Mockito.any(String.class))).willReturn(dto.getEmailAuthKey());
        memberService.sendVerificationEmail("123@gmail.com");

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            memberService.registerMember(dto);
        });

        //then
        result.isInstanceOf(NotExistsEmailException.class);
        result.hasMessage("존재하지 않는 유저의 이메일입니다.");

    }


    @DisplayName("이메일로 온 인증번호와는 다른 번호로 회원 가입을 진행한다.")
    @Test
    void failRegisterMemberCase2(){
        //given
        RegisterMemberRequestDto dto = RegisterMemberServiceDtoFactory.createSuccessCaseRequestDto();
        given(mailSender.sendVerificationEmail(Mockito.any(String.class))).willReturn("000000");
        memberService.sendVerificationEmail(dto.getEmail());

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            memberService.registerMember(dto);
        });

        //then
        result.isInstanceOf(WrongEmailAuthKeyException.class);
        result.hasMessage("이메일 인증번호가 틀립니다.");
    }

    @DisplayName("이메일을 찾을 수 있어야 한다.")
    @Test
    void findEmail(){
        //given
        String name = "bean";
        String email = "123@gmail.com";
        LocalDate birth = LocalDate.of(1999, 6, 27);
        Member member = Member.builder()
                .name(name)
                .auth(new Auth(email, "pwd"))
                .birth(birth)
                .build();
        memberRepository.save(member);

        //when
        FindEmailResponseDto res = memberProvider.findEmailByBirthAndName(name, birth);

        //then
        assertThat(res.getEmail()).isEqualTo(email);
    }

    @DisplayName("계정과 다른 이름을 입력하므로 이메일을 찾을 수 없다.")
    @Test
    void failFindEmail(){
        //given
        String name = "bean";
        String email = "123@gmail.com";
        LocalDate birth = LocalDate.of(1999, 6, 27);
        Member member = Member.builder()
                .name(name)
                .auth(new Auth(email, "pwd"))
                .birth(birth)
                .build();
        memberRepository.save(member);

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            memberProvider.findEmailByBirthAndName("xxx", birth);
        });

        //then
        result.isInstanceOf(NotExistsMemberException.class);
    }

    @DisplayName("멤버 정보를 찾을 수 있어야 한다.")
    @Test
    void findMemberInfo() {
        //given
        Member member = memberRepository.save(MemberFactory.create());

        //when
        FindMemberInfoResponseDto res = memberProvider.findMemberInfoByMemberId(member.getId());

        //then
        assertThat(res.getName()).isEqualTo(member.getName());
        assertThat(res.getAge()).isEqualTo(member.getAge());
        assertThat(res.getGender()).isEqualTo(member.getGender());
        assertThat(res.getEmail()).isEqualTo(member.getAuth().getEmail());
        assertThat(res.getBirth()).isEqualTo(member.getBirth());
    }

    @DisplayName("저장된 멤버 ID와는 다른 ID로 멤버 정보를 호출한다.")
    @Test
    void failFindMemberInfo() {
        //given
        Member member = MemberFactory.create();
        memberRepository.save(member);

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            memberProvider.findMemberInfoByMemberId(0L);
        });

        //then
        result.isInstanceOf(NotExistsMembersInfoException.class);
        result.hasMessage("멤버 정보가 없습니다.");
    }

    @DisplayName("비밀번호 변경을 성공한다.")
    @Test
    void successModifyMemberPassword(){

        //given
        String newPwd = "1q2w3e4r!";
        String newCheckPwd = "1q2w3e4r!";
        String authKey = "111336";
        Member member = MemberFactory.create();
        memberRepository.save(member);
        emailRepository.save(new Email(member.getAuth().getEmail(), authKey));

        //when
        memberService.modifyMemberPassword(new ModifyMemberPasswordRequestDto(member.getAuth().getEmail(), authKey, newPwd, newCheckPwd));

        //then
        Member result = memberRepository.findMemberByEmail(member.getAuth().getEmail()).get();
        assertThat(passwordEncoder.matches(newPwd, result.getAuth().getPassword())).isTrue();
    }

    @DisplayName("이메일이 레디스 저장되어 있지 않으므로 실패한다.")
    @Test
    void failModifyMemberPasswordCase1(){

        //given
        String newPwd = "1q2w3e4r!";
        String newCheckPwd = "1q2w3e4r!";
        String authKey = "111336";
        Member member = MemberFactory.create();
        memberRepository.save(member);
        //emailRepository.save(new Email(member.getAuth().getEmail(), authKey));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            memberService.modifyMemberPassword(new ModifyMemberPasswordRequestDto(member.getAuth().getEmail(), authKey, newPwd, newCheckPwd));
        });

        //then
        result.isInstanceOf(NotExistsEmailException.class);
    }

    @DisplayName("인증번호 키와  레디스 저장되어 있는 인증 키가 다르므로 실패한다.")
    @Test
    void failModifyMemberPasswordCase2(){

        //given
        String newPwd = "1q2w3e4r!";
        String newCheckPwd = "1q2w3e4r!";
        String authKey = "111336";
        Member member = MemberFactory.create();
        memberRepository.save(member);
        emailRepository.save(new Email(member.getAuth().getEmail(), authKey));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            memberService.modifyMemberPassword(new ModifyMemberPasswordRequestDto(member.getAuth().getEmail(), "X", newPwd, newCheckPwd));
        });

        //then
        result.isInstanceOf(WrongEmailAuthKeyException.class);
    }

    @DisplayName("비밀번호 변경에서 입력한 새로운 비밀번호와 새로운 비밀번호를 확인할 비밀번호가 다르므로 변경을 실패한다.")
    @Test
    void failModifyMemberPasswordCase3(){

        //given
        String newPwd = "1q2w3e4r!";
        String newCheckPwd = "1q2w3e4r!!";
        String authKey = "111336";
        Member member = MemberFactory.create();
        memberRepository.save(member);
        emailRepository.save(new Email(member.getAuth().getEmail(), authKey));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            memberService.modifyMemberPassword(new ModifyMemberPasswordRequestDto(member.getAuth().getEmail(), authKey, newPwd, newCheckPwd));
        });

        //then
        result.isInstanceOf(PasswordAndCheckPasswordNotSameException.class);
    }

    @DisplayName("로컬 회원 가입만 비밀번호 변경을 진행할 수 있다.")
    @Test
    void failModifyMemberPasswordCase4(){

        //given
        String newPwd = "1q2w3e4r!";
        String newCheckPwd = "1q2w3e4r!!";
        String authKey = "111336";
        String email = "123@gmail.com";
        Member member = Member.builder()
                .authProvider(AuthProvider.google)
                .auth(new Auth(email, authKey))
                .build();
        memberRepository.save(member);
        emailRepository.save(new Email(member.getAuth().getEmail(), authKey));

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            memberService.modifyMemberPassword(new ModifyMemberPasswordRequestDto(member.getAuth().getEmail(), authKey, newPwd, newCheckPwd));
        });

        //then
        result.isInstanceOf(OAuthMemberCanNotChangePasswordException.class);
    }
}
