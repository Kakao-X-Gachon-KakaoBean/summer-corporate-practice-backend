package com.kakaobean.core.releasenote.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.common.event.Events;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReleaseNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Double version;

    private Long projectId;

    private Long memberId;

    public ReleaseNote(BaseStatus status,
                       String title,
                       String content,
                       Double version,
                       Long projectId,
                       Long memberId) {
        super(status);
        this.title = title;
        this.content = content;
        this.version = version;
        this.projectId = projectId;
        this.memberId = memberId;
    }

    public void registered(List<ReleaseNoteRegisteredEvent.NotifiedTargetInfo> notifiedMails) {
        Events.raise(new ReleaseNoteRegisteredEvent(projectId, id, title,  notifiedMails));
    }
}
