package com.kakaobean.core.project.infrastructure;

import com.kakaobean.common.EmailHtmlUtils;
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
        String subject = "[코코노트] "+ project.getTitle()  + " 프로젝트 초대 메일입니다.";
        String invitationUrl = "localhost:3000/invitations/" + project.getSecretKey(); //TODO 개발할 때 편의상 사용.
        emailSender.sendEmail(
                invitedMemberEmail,
                subject,
                () -> EmailHtmlUtils.makeLinkHtml(
                        "프로젝트 초대",
                        "프로젝트 참여를 위한 링크를 알려드립니다.",
                        "",
                        "아래 링크",
                        "로 입장하시면 프로젝트에 참여 가능합니다.",
                        "링크",
                        invitationUrl,
                        invitationUrl
                )
        );
    }
}
