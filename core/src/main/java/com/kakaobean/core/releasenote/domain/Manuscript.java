package com.kakaobean.core.releasenote.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.common.event.Events;
import com.kakaobean.core.releasenote.domain.event.ManuscriptRegisteredEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE manuscript SET status = 'INACTIVE' WHERE id = ?")
@Entity
public class Manuscript extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String version;

    private Long lastEditedMemberId;

    private Long projectId;

    @Enumerated(EnumType.STRING)
    private ManuscriptStatus manuscriptStatus;

    public Manuscript(BaseStatus status,
                      String title,
                      String content,
                      String version,
                      Long lastEditedMemberId,
                      Long projectId,
                      ManuscriptStatus manuscriptStatus) {
        super(status);
        this.title = title;
        this.content = content;
        this.version = version;
        this.lastEditedMemberId = lastEditedMemberId;
        this.projectId = projectId;
        this.manuscriptStatus = manuscriptStatus;
    }

    public void registered() {
        Events.raise(new ManuscriptRegisteredEvent(projectId, id, title));
    }

    public void modifyManuscriptStatus(ManuscriptStatus status) {
        this.manuscriptStatus = status;
    }
}
