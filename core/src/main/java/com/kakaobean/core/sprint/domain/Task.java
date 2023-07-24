package com.kakaobean.core.sprint.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    /**
     * 테스트용
     */
    @Builder
    public Task(BaseStatus status,
                Long id,
                Long workerId,
                Long sprintId,
                String title,
                WorkStatus workStatus, LocalDate startDate,
                LocalDate endDate) {
        super(status);
        this.id = id;
        this.workerId = workerId;
        this.sprintId = sprintId;
        this.title = title;
        this.workStatus = workStatus;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
