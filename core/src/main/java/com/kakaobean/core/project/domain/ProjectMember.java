package com.kakaobean.core.project.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE project_member SET status = INACTIVE WHERE id = ?")
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
}
