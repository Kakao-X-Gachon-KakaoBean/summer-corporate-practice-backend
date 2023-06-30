package com.kakaobean.core.project.infrastructure;

import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.service.InvitationProjectMemberService;
import com.kakaobean.independentlysystem.email.EmailSender;
import org.springframework.stereotype.Component;

@Component
public class InvitationProjectMemberServiceImpl implements InvitationProjectMemberService {

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;

    public InvitationProjectMemberServiceImpl(MemberRepository memberRepository,
                                              EmailSender emailSender) {
        this.memberRepository = memberRepository;
        this.emailSender = emailSender;
    }

    @Override
    public void sendInvitationMail(Long invitedMemberId, Project project) {
        Member member = memberRepository.findMemberById(invitedMemberId).orElseThrow();
        emailSender.sendProjectInvitationEmail(member.getAuth().getEmail(), project.getTitle(), project.getSecretKey());
    }
}
