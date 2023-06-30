package com.kakaobean.core.project.domain.service;

import com.kakaobean.core.project.domain.Project;

import java.util.List;

public interface InvitationProjectMemberService {
    void sendInvitationMail(Long invitedMemberId, Project project);

}
