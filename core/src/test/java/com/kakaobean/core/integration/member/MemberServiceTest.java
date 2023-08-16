package com.kakaobean.core.integration.member;

import com.kakaobean.core.factory.member.MemberFactory;
import com.kakaobean.core.member.application.MemberProvider;
import com.kakaobean.core.member.application.dto.request.ModifyMemberPasswordRequestDto;
import com.kakaobean.core.member.application.dto.request.ModifyMemberRequestDto;
import com.kakaobean.core.member.application.dto.response.FindMemberInfoResponseDto;
import com.kakaobean.core.member.domain.*;
import com.kakaobean.core.member.domain.Email;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.domain.service.VerifiedEmailService;
import com.kakaobean.core.member.exception.member.*;
import com.kakaobean.core.factory.member.RegisterMemberServiceDtoFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.member.application.MemberService;
import com.kakaobean.core.member.application.dto.request.RegisterMemberRequestDto;
import com.kakaobean.core.member.infrastructure.ModifyMemberServiceImpl;
import com.kakaobean.independentlysystem.image.ImageService;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;

public class MemberServiceTest extends IntegrationTest {

    @Mock
    private ImageService imageService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberService memberServiceForImageUpload;

    @Autowired
    VerifiedEmailService memberVerifiedEmailService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    MemberProvider memberProvider;

    PasswordEncoder passwordEncoder;

    @Mock
    MultipartFile profileImg;

    @Mock
    MultipartFile thumbnailImg;




    @BeforeEach
    void beforeEach(){
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.flushDb();
            return null;
        });
        this.passwordEncoder = new BCryptPasswordEncoder(); //임시 처리
        memberRepository.deleteAll();

        memberServiceForImageUpload = new MemberService(
                memberRepository,
                new MemberValidator(memberRepository),
                memberVerifiedEmailService,
                new ModifyMemberServiceImpl(),
                imageService
        );
    }

    @DisplayName("멤버를 등록한다.")
    @Test
    void registerMember(){
        //given
        RegisterMemberRequestDto dto = RegisterMemberServiceDtoFactory.createSuccessCaseRequestDto();
        emailRepository.save(new Email(dto.getEmail(), dto.getEmailAuthKey()));

        //when
        memberService.registerMember(dto);

        //then
        assertThat(memberRepository.findAll().size()).isEqualTo(1);
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

        //when
        memberService.sendVerificationEmail(email, authKey);

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
        memberService.sendVerificationEmail(email, authKey);

        //when
        memberService.sendVerificationEmail(email, authKey2);

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
        memberService.sendVerificationEmail("123@gmail.com", "111333");

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
        memberService.sendVerificationEmail(dto.getEmail(), "12345");

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            memberService.registerMember(dto);
        });

        //then
        result.isInstanceOf(WrongEmailAuthKeyException.class);
        result.hasMessage("이메일 인증번호가 틀립니다.");
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
        assertThat(res.getEmail()).isEqualTo(member.getAuth().getEmail());
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

    @DisplayName("멤버 정보(이름) 변경을 성공한다.")
    @Test
    void successModifyMember(){

        //given
        String newName = "newHiki";
        Member member = memberRepository.save(MemberFactory.create());

        //when
        memberService.modifyMember(new ModifyMemberRequestDto(member.getId(), newName));

        //then
        Member result = memberRepository.findById(member.getId()).get();
        assertThat(newName.equals(result.getName())).isTrue();
    }

    @DisplayName("로컬 회원 가입만 회원 정보 변경을 진행할 수 있다.")
    @Test
    void failModifyMemberCase1(){

        //given
        String newName="newHiki";
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
            memberService.modifyMember(new ModifyMemberRequestDto(member.getId(), newName));
        });

        //then
        result.isInstanceOf(OAuthMemberCanNotChangeNameException.class);
    }

    @DisplayName("바꾸려는 이름이 기존 이름과 같은 경우 변경할 수 없다.")
    @Test
    void failModifyMemberCase2(){

        //given
        String newName = "kakoBean";

        Member member = memberRepository.save(MemberFactory.create());

        //when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            memberService.modifyMember(new ModifyMemberRequestDto(member.getId(), newName));
        });

        //then
        result.isInstanceOf(ChangingNameToSameNameException.class);
    }

    @DisplayName("유저가 프로필 사진을 업로드한다.")
    @Test
    void successProfileImagesUploadCase1() throws IOException{
        // Given
        Member member = memberRepository.save(MemberFactory.create());
        String profileImgUrl = "https://wayc-deploy-bucket.s3.ap-northeast-2.amazonaws.com/images/profile.jpg";
        String thumbnailImgUrl = "https://wayc-deploy-bucket.s3.ap-northeast-2.amazonaws.com/images/thumbnail.jpg";

        Mockito.when(imageService.upload(Mockito.any(MultipartFile.class))).thenReturn(profileImgUrl, thumbnailImgUrl);

        MultipartFile mockProfileImg = Mockito.mock(MultipartFile.class);
        MultipartFile mockThumbnailImg = Mockito.mock(MultipartFile.class);

        // Configure the input streams for the mock MultipartFile instances
        InputStream profileInputStream = Mockito.mock(ByteArrayInputStream.class);
        InputStream thumbnailInputStream = Mockito.mock(ByteArrayInputStream.class);

        Mockito.when(mockProfileImg.getInputStream()).thenReturn(profileInputStream);
        Mockito.when(mockThumbnailImg.getInputStream()).thenReturn(thumbnailInputStream);

        // When
        memberServiceForImageUpload.uploadProfileImages(member.getId(), mockProfileImg, mockThumbnailImg);

        // Then
        Member result = memberRepository.findById(member.getId()).orElse(null);
        assertThat(result).isNotNull();
        assertThat(result.getProfileImg()).isEqualTo(profileImgUrl);
        assertThat(result.getThumbnailImg()).isEqualTo(thumbnailImgUrl);
    }
}
