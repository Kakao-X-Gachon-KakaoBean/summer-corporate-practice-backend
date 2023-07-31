package com.kakaobean.core.sprint.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.common.event.Events;
import com.kakaobean.core.sprint.domain.event.TaskAssignedEvent;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE task SET status = 'INACTIVE' WHERE id = ?")
@Entity
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workerId;

    private Long sprintId;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    public Task(BaseStatus status,
                Long workerId,
                Long sprintId,
                String title,
                String content,
                WorkStatus workStatus) {
        super(status);
        this.workerId = workerId;
        this.sprintId = sprintId;
        this.title = title;
        this.content = content;
        this.workStatus = workStatus;
    }

    /**
     * 테스트용
     */
    @Builder
    public Task(Long id,
                Long workerId,
                Long sprintId,
                String title,
                String content,
                WorkStatus workStatus) {
        super(BaseStatus.ACTIVE);
        this.id = id;
        this.workerId = workerId;
        this.sprintId = sprintId;
        this.title = title;
        this.content = content;
        this.workStatus = workStatus;
    }

    public void modify(String newTitle, String newContent) {
        this.title = newTitle;
        this.content = newContent;
    }

    public void assigned(Long memberId) {
        this.workStatus = WorkStatus.WORKING;
        this.workerId = memberId;
        Events.raise(new TaskAssignedEvent(id, sprintId, workerId));
    }
}
