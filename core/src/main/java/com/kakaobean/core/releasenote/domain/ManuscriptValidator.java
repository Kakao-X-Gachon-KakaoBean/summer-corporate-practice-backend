package com.kakaobean.core.releasenote.domain;

import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

import static com.kakaobean.core.project.domain.ProjectRole.*;


@Component
@RequiredArgsConstructor
public class ManuscriptValidator {

    private final ProjectMemberRepository projectMemberRepository;
    private final ManuscriptRepository manuscriptRepository;

    public void valid(Manuscript manuscript) {
        ProjectMember writer = projectMemberRepository
                .findByMemberIdAndProjectId(manuscript.getLastEditedMemberId(), manuscript.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);

        if(writer.getProjectRole() != ADMIN) {
            throw new ManuscriptWriterAccessException(writer.getProjectRole().name());
        }

        List<Manuscript> manuscripts = manuscriptRepository.findManuscriptByProjectId(manuscript.getProjectId());
        for (Manuscript m : manuscripts) {
            if(isSameVersion(manuscript, m)) {
                throw new DuplicateManuscriptVersionException();
            }
        }
    }

    private boolean isSameVersion(Manuscript manuscript, Manuscript m) {
        return Objects.equals(manuscript.getVersion(), m.getVersion());
    }

    public void validRightToModify(Manuscript manuscript, Long memberId){
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(memberId, manuscript.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);

        if(projectMember.getProjectRole() != ADMIN & projectMember.getProjectRole() != MEMBER){
            throw new ManuscriptModificationAccessException(projectMember.getProjectRole().name());
        }

        if(manuscript.getManuscriptStatus() == ManuscriptStatus.Modifying) {
            throw new AnotherMemberAlreadyModifyingException();
        }
    }

    public void isModifiable(Manuscript manuscript, Long memberId){
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(memberId, manuscript.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);

        if(projectMember.getProjectRole() != ADMIN & projectMember.getProjectRole() != MEMBER){
            throw new ManuscriptModificationAccessException(projectMember.getProjectRole().name());
        }

        if(manuscript.getManuscriptStatus() != ManuscriptStatus.Modifying) {
            throw new CannotModifyManuscriptException();
        }
    }

    public void validRightToDelete(Manuscript manuscript, Long adminId) {
        ProjectMember projectMember = projectMemberRepository.findByMemberIdAndProjectId(adminId, manuscript.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);

        if(projectMember.getProjectRole() != ADMIN){
            throw new CannotDeleteManuscriptException(projectMember.getProjectRole().name());
        }
    }
}
