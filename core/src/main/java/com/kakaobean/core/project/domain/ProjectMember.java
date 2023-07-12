package com.kakaobean.core.project.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.common.event.Events;
import com.kakaobean.core.project.domain.event.ProjectMemberRegisteredEvent;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE project_member SET status = 'INACTIVE' WHERE id = ?")
@Entity
public class ProjectMember extends BaseEntity {

    /**
     * 중간 테이블 pk는 되도록 사용하지 말 것.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private Long memberId;

    @Enumerated(EnumType.STRING)
    private ProjectRole projectRole;

    protected ProjectMember(){}

    public ProjectMember(BaseStatus status,
                         Long projectId,
                         Long memberId,
                         ProjectRole projectRole) {
        super(status);
        this.projectId = projectId;
        this.memberId = memberId;
        this.projectRole = projectRole;
    }

    public void modifyProjectRole(ProjectRole projectRole) {
        this.projectRole = projectRole;
    }

    public void registered() {
        Events.raise(new ProjectMemberRegisteredEvent(projectId, memberId));
    }
}
