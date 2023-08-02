package com.kakaobean.core.sprint.domain.repository.query;

import lombok.Getter;

import java.util.List;

@Getter
public class FindAllSprintResponseDto {
    private final List<SprintsDto> sprints;

    public FindAllSprintResponseDto(List<SprintsDto> sprints) {
        this.sprints = sprints;
    }
}
