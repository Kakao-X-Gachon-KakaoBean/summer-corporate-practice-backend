package com.kakaobean.sprint.dto.request;

import com.kakaobean.core.sprint.application.dto.ModifySprintRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ModifySprintRequest {

    @NotEmpty
    private String sprintTitle;

    @NotEmpty
    private String sprintDesc;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate dueDate;

    public ModifySprintRequest(String sprintTitle, String sprintDesc, LocalDate startDate, LocalDate dueDate) {
        this.sprintTitle = sprintTitle;
        this.sprintDesc = sprintDesc;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public ModifySprintRequestDto toServiceDto(Long adminId, Long sprintId){
        return new ModifySprintRequestDto(
                sprintTitle,
                sprintDesc,
                startDate,
                dueDate,
                adminId,
                sprintId
        );
    }
}
