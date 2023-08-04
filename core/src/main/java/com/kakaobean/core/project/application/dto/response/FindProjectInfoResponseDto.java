package com.kakaobean.core.project.application.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FindProjectInfoResponseDto {

    private String projectTitle;
    private String projectContent;
    private List<FindProjectMemberResponseDto> projectMembers = new ArrayList<>();

    public FindProjectInfoResponseDto(String projectTitle, String projectContent) {
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
    }

    //테스트용
    public FindProjectInfoResponseDto(String projectTitle, String projectContent, List<FindProjectMemberResponseDto> projectMembers) {
        this.projectTitle = projectTitle;
        this.projectContent = projectContent;
        this.projectMembers = projectMembers;
    }
}
