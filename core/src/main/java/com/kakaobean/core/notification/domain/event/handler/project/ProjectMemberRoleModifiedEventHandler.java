package com.kakaobean.core.notification.domain.event.handler.project;

import com.kakaobean.core.member.domain.Member;
import com.kakaobean.core.member.domain.repository.MemberRepository;
import com.kakaobean.core.member.exception.member.NotExistsMemberException;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.event.ProjectMemberRoleModifiedEvent;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.project.exception.NotExistsProjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProjectMemberRoleModifiedEventHandler {

    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    /**
     * 프로젝트 멤버의 권한이 바뀌었을 때 발생하는 메서드
     *
     * 권한이 수정된 멤버에게 메시지와 이메일이 알림이 가야한다.
     * 알림은 저장되어야 한다.
     */
    @TransactionalEventListener(ProjectMemberRoleModifiedEvent.class)
    public void handle(ProjectMemberRoleModifiedEvent event) {
        Member member = memberRepository.findById(event.getMemberId())
                .orElseThrow(NotExistsMemberException::new);
        Project project = projectRepository.findById(event.getProjectId())
                .orElseThrow(NotExistsProjectException::new);


    }
}
