package com.kakaobean.core.sprint.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
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
}
