package com.kakaobean.core.project.domain.repository;

import com.kakaobean.core.project.application.dto.response.FindProjectInfoResponseDto;
import com.kakaobean.core.project.application.dto.response.FindProjectMemberResponseDto;
import com.kakaobean.core.project.application.dto.response.FindProjectResponseDto;
import com.kakaobean.core.project.application.dto.response.FindProjectTitleResponseDto;

import java.util.List;

public interface ProjectQueryRepository {
    List<FindProjectMemberResponseDto> findProjectMembers(Long projectId);
    List<FindProjectResponseDto> findProjects(Long memberId);
    FindProjectInfoResponseDto findProject(Long projectId);
    FindProjectTitleResponseDto findBySecretKey(String projectSecretKey);
}
