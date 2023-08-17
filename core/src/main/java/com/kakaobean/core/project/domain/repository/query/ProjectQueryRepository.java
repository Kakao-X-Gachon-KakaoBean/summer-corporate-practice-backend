package com.kakaobean.core.project.domain.repository.query;

import java.util.List;

public interface ProjectQueryRepository {
    List<FindProjectMemberResponseDto> findProjectMembers(Long projectId);
    FindProjectsResponseDto findProjects(Long memberId);
    FindProjectInfoResponseDto findProject(Long projectId);
    FindProjectTitleResponseDto findBySecretKey(String projectSecretKey);
}
