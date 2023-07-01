package com.kakaobean.core.project.infrastructure;

import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.service.InvitationProjectMemberService;
import com.kakaobean.independentlysystem.email.EmailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvitationProjectMemberServiceImpl implements InvitationProjectMemberService {

    private final EmailSender emailSender;

    public InvitationProjectMemberServiceImpl(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendInvitationMails(List<String> invitedMemberEmail, Project project) {
        emailSender.sendProjectInvitationEmail(invitedMemberEmail, project.getTitle(), project.getSecretKey());
    }
}
