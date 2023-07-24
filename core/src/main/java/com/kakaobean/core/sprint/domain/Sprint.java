package com.kakaobean.core.sprint.domain;


import com.kakaobean.core.common.domain.BaseEntity;
import com.kakaobean.core.common.domain.BaseStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "status = 'ACTIVE'")
@SQLDelete(sql = "UPDATE sprint SET status = 'INACTIVE' WHERE id = ?")
@Entity
public class Sprint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    private String title;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    public Sprint(BaseStatus status, Long projectId, String title, String description, LocalDate startDate, LocalDate endDate) {
        super(status);
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * 테스트용
     */
    @Builder
    public Sprint(BaseStatus status,
                  Long id,
                  Long projectId,
                  String title,
                  String description,
                  LocalDate startDate,
                  LocalDate endDate) {
        super(status);
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
