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
    private String title;

    @NotEmpty
    private String description;

    @NotNull
    private Long projectId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public RegisterSprintRequest(String title, String description, Long projectId, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.description = description;
        this.projectId = projectId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public RegisterSprintRequestDto toServiceDto(Long memberId){
        return new RegisterSprintRequestDto(title, description, projectId, startDate, endDate, memberId);
    }
}
