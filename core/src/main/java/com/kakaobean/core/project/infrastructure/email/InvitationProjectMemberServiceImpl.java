package com.kakaobean.core.project.infrastructure.email;

import com.kakaobean.common.EmailHtmlUtils;
import com.kakaobean.core.notification.config.EmailProperties;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.service.InvitationProjectMemberService;
import com.kakaobean.independentlysystem.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@RequiredArgsConstructor
@Component
public class InvitationProjectMemberServiceImpl implements InvitationProjectMemberService {

    private final EmailSender emailSender;
    private final EmailProperties emailProperties;


    @Override
    public void sendInvitationMails(List<String> invitedMemberEmail, Project project) {
        String subject = "[코코노트] "+ project.getTitle()  + " 프로젝트 초대 메일입니다.";
        String invitationUrl = emailProperties.getHostName() + "/invitations/" + project.getSecretKey();
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
                        invitationUrl
                )
        );
    }
}
