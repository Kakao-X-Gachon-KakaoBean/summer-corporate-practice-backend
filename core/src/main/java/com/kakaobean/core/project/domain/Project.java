package com.kakaobean.core.project.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.common.event.Events;
import com.kakaobean.core.project.domain.event.ProjectMemberInvitedEvent;
import com.kakaobean.core.project.domain.event.RemovedProjectEvent;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Getter
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE project SET status = INACTIVE WHERE id = ?")
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


    /**
     * 테스트에서 사용
     */
    public Project(BaseStatus status, Long id, String title, String content, String secretKey) {
        super(status);
        this.id = id;
        this.title = title;
        this.content = content;
        this.secretKey = secretKey;
    }

    public ProjectMemberInvitedEvent createInvitationProjectMemberEvent(List<String> invitedMemberEmails) {
        return new ProjectMemberInvitedEvent(invitedMemberEmails, this);
    }

    public void modify(String newTitle, String newContent){
        this.title = newTitle;
        this.content = newContent;
    }

    public void remove() {
        super.delete();
        Events.raise(new RemovedProjectEvent(this.id));
    }
}
