package com.kakaobean.core.sprint.application.dto;

import com.kakaobean.core.common.domain.BaseStatus;
import com.kakaobean.core.sprint.domain.Sprint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RegisterSprintRequestDto {

    private String title;
    private String description;
    private Long projectId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long memberId;

    public RegisterSprintRequestDto(String title,
                                    String description,
                                    Long projectId,
                                    LocalDate startDate,
                                    LocalDate endDate,
                                    Long memberId) {
        this.title = title;
        this.description = description;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memberId = memberId;
    }

    public Sprint toEntity(){
        return new Sprint(BaseStatus.ACTIVE, projectId, title, description, startDate, endDate);
    }
}
