package com.kakaobean.core.project.domain.repository;

import com.kakaobean.core.project.application.dto.response.FindProjectMemberResponseDto;
import com.kakaobean.core.project.application.dto.response.FindProjectResponseDto;

import java.util.List;

public interface ProjectQueryRepository {
    List<FindProjectMemberResponseDto> findProjectMembers(Long projectId);
    List<FindProjectResponseDto> findProjects(Long memberId);
}
