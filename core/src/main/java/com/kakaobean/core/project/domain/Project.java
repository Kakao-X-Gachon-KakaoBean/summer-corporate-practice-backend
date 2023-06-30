package com.kakaobean.core.project.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.common.event.Events;
import com.kakaobean.core.project.domain.event.ProjectMemberInvitedEvent;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String secretKey;

    protected Project() {}

    public Project(String title,
                   String content,
                   BaseStatus baseStatus,
                   String secretKey) {
        super(baseStatus);
        this.title = title;
        this.content = content;
        this.secretKey = secretKey;
    }

    public List<ProjectMemberInvitedEvent> sendInvitationEmail(List<Long> invitedMemberIdList) {
        return invitedMemberIdList
                .stream()
                .map(id -> new ProjectMemberInvitedEvent(id, this))
                .collect(Collectors.toList());
    }
}
