package com.kakaobean.core.member.infrastructure;

import com.kakaobean.core.member.domain.Email;
import com.kakaobean.core.member.domain.repository.EmailRepository;
import com.kakaobean.core.member.domain.service.VerifiedEmailService;
import com.kakaobean.core.member.exception.member.NotExistsEmailException;
import com.kakaobean.core.member.exception.member.WrongEmailAuthKeyException;
import com.kakaobean.independentlysystem.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberVerifiedEmailServiceImpl implements VerifiedEmailService {

    private final EmailSender emailSender;
    private final EmailRepository emailRepository;

    public void sendVerificationEmail(String receiveEmail) {
        String authKey = emailSender.sendVerificationEmail(receiveEmail);
        saveAuthKeyInRedis(new Email(receiveEmail, authKey));
    }

    private void saveAuthKeyInRedis(Email email) {
        if(emailRepository.hasKey(email)){
            emailRepository.removeEmailCertification(email);
        }
        emailRepository.save(email);
    }

    @Override
    public void verifyAuthEmailKey(String emailString, String authKey) {
        Email email = new Email(emailString, authKey);
        if(notExistsEmail(email)){
            throw new NotExistsEmailException();
        }
        if(wrongAuthKey(email)){
            throw new WrongEmailAuthKeyException();
        }
    }

    private boolean notExistsEmail(Email email) {
        return !emailRepository.hasKey(email);
    }

    private boolean wrongAuthKey(Email email) {
        Email emailCertification = emailRepository.getEmailCertification(email);
        return !email.getAuthKey().equals(emailCertification.getAuthKey());
    }
}
