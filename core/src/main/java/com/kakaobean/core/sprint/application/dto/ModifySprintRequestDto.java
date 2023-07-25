package com.kakaobean.core.sprint.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ModifySprintRequestDto {

    private String newTitle;
    private String newDescription;
    private LocalDate newStartDate;
    private LocalDate newEndDate;
    private Long adminId;
    private Long sprintId;

    public ModifySprintRequestDto(String newTitle,
                                  String newDescription,
                                  LocalDate newStartDate,
                                  LocalDate newEndDate,
                                  Long adminId,
                                  Long sprintId) {
        this.newTitle = newTitle;
        this.newDescription = newDescription;
        this.newStartDate = newStartDate;
        this.newEndDate = newEndDate;
        this.adminId = adminId;
        this.sprintId = sprintId;
    }
}
