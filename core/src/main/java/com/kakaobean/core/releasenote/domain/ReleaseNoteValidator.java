package com.kakaobean.core.releasenote.domain;


import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.ProjectRole;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.releasenote.exception.ReleaseNoteWriterAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.kakaobean.core.project.domain.ProjectRole.*;

@Component
@RequiredArgsConstructor
public class ReleaseNoteValidator {

    private final ProjectMemberRepository projectMemberRepository;

    public void validWriterAccess(ReleaseNote releaseNote) {
        ProjectMember writer = projectMemberRepository
                .findByMemberIdAndProjectId(releaseNote.getMemberId(), releaseNote.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);

        if(writer.getProjectRole() != ADMIN){
            throw new ReleaseNoteWriterAccessException(writer.getProjectRole().name());
        }
    }
}
