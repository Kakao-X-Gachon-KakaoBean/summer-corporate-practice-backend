package com.kakaobean.core.releasenote.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.common.event.Events;
import com.kakaobean.core.releasenote.domain.event.ReleaseNoteDeployedEvent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Entity
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE release_note SET status = 'INACTIVE' WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReleaseNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String version;

    private Long projectId;

    private Long memberId;

    public ReleaseNote(BaseStatus status,
                       String title,
                       String content,
                       String version,
                       Long projectId,
                       Long memberId) {
        super(status);
        this.title = title;
        this.content = content;
        this.version = version;
        this.projectId = projectId;
        this.memberId = memberId;
    }

    /**
     * 테스트용
     */
    @Builder
    public ReleaseNote(BaseStatus status,
                       Long id,
                       String title,
                       String content,
                       String version,
                       Long projectId,
                       Long memberId) {
        super(BaseStatus.ACTIVE);
        this.id = id;
        this.title = title;
        this.content = content;
        this.version = version;
        this.projectId = projectId;
        this.memberId = memberId;
    }

    public void deployed() {
        Events.raise(new ReleaseNoteDeployedEvent(id));
    }
}
