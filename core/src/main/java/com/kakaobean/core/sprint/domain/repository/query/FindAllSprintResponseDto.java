package com.kakaobean.core.sprint.domain.repository.query;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindAllSprintResponseDto {

    private List<SprintsDto> sprints;

    public FindAllSprintResponseDto(List<SprintsDto> sprints) {
        this.sprints = sprints;
    }
}
