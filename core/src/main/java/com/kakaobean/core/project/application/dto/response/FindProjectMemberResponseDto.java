package com.kakaobean.core.project.application.dto.response;

import com.kakaobean.core.project.domain.ProjectRole;
import lombok.Getter;

@Getter
public class FindProjectMemberResponseDto {

    private Long projectMemberId;
    private String projectMemberName;
    private String projectMemberEmail;
    private ProjectRole projectMemberRole;
    private String memberThumbnailImg;

    protected FindProjectMemberResponseDto() {}

    public FindProjectMemberResponseDto(Long projectMemberId,
                                        String projectMemberName,
                                        String projectMemberEmail,
                                        ProjectRole projectMemberRole,
                                        String memberThumbnailImg) {
        this.projectMemberId = projectMemberId;
        this.projectMemberName = projectMemberName;
        this.projectMemberEmail = projectMemberEmail;
        this.projectMemberRole = projectMemberRole;
        this.memberThumbnailImg = memberThumbnailImg;
    }
}
