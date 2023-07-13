package com.kakaobean.core.releasenote.domain;

import com.kakaobean.core.common.domain.BaseEntity;

import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.releasenote.exception.DuplicateManuscriptVersionException;
import com.kakaobean.core.releasenote.exception.ManuscriptWriterAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ManuscriptValidator extends BaseEntity {

    private final ProjectMemberRepository projectMemberRepository;
    private final ManuscriptRepository manuscriptRepository;

    public void valid(Manuscript manuscript) {
        ProjectMember writer = projectMemberRepository
                .findByMemberIdAndProjectId(manuscript.getLastEditedMemberId(), manuscript.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);

        if(writer.getProjectRole() != ProjectRole.ADMIN){
            throw new ManuscriptWriterAccessException();
        }

        if(manuscriptRepository.findManuscriptByVersion(manuscript.getVersion()).isPresent()){
            throw new DuplicateManuscriptVersionException();
        }
    }
}
