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
    private String newTitle;

    @NotEmpty
    private String newDescription;

    @NotNull
    private LocalDate newStartDate;

    @NotNull
    private LocalDate newEndDate;


    public ModifySprintRequest(String newTitle, String newDescription, LocalDate newStartDate, LocalDate newEndDate) {
        this.newTitle = newTitle;
        this.newDescription = newDescription;
        this.newStartDate = newStartDate;
        this.newEndDate = newEndDate;
    }

    public ModifySprintRequestDto toServiceDto(Long adminId, Long sprintId){
        return new ModifySprintRequestDto(
                newTitle,
                newDescription,
                newStartDate,
                newEndDate,
                adminId,
                sprintId
        );
    }
}
