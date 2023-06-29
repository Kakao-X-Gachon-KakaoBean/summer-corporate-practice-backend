package com.kakaobean.core.task.domain;

import com.kakaobean.core.common.domain.BaseEntity;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workerId;

    private String title;

    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    private LocalDate startDate;

    private LocalDate endDate;
}
