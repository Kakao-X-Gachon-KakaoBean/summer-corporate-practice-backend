package com.kakaobean.core.issue.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.common.event.Events;
import com.kakaobean.core.issue.domain.event.RegisterIssueEvent;
import com.kakaobean.core.project.domain.event.ProjectRegisteredEvent;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE issue SET status = 'INACTIVE' WHERE id = ?")
@Entity
public class Issue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private String title;

    private String content;

    private Long writerId;

    protected Issue(){}

    public Issue(BaseStatus status,
                 Long projectId,
                 String title,
                 String content,
                 Long writerId) {
        super(status);
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.writerId = writerId;
    }

    /**
     * 테스트용
     */
    @Builder
    public Issue(BaseStatus status,
                 Long id,
                 Long projectId,
                 String title,
                 String content,
                 Long writerId) {
        super(BaseStatus.ACTIVE);
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.content = content;
        this.writerId = writerId;
    }
    public void registered(Long issueId) {
        Events.raise(new RegisterIssueEvent(issueId));
    }

    public void modify(String newTitle, String newContent, ){
        this.title = newTitle;
        this.content = content;
    }
}
