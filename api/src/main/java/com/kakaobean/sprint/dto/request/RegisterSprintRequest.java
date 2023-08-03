package com.kakaobean.sprint.dto.request;

import com.kakaobean.core.sprint.application.dto.RegisterSprintRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RegisterSprintRequest {

    @NotEmpty
    private String sprintTitle;

    @NotEmpty
    private String sprintDesc;

    @NotNull
    private Long projectId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate dueDate;

    public RegisterSprintRequest(String sprintTitle, String sprintDesc, Long projectId, LocalDate startDate, LocalDate dueDate) {
        this.sprintTitle = sprintTitle;
        this.sprintDesc = sprintDesc;
        this.projectId = projectId;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public RegisterSprintRequestDto toServiceDto(Long memberId){
        return new RegisterSprintRequestDto(sprintTitle, sprintDesc, projectId, startDate, dueDate, memberId);
    }
}
