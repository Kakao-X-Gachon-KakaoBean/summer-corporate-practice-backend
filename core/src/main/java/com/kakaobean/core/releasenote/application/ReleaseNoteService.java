package com.kakaobean.core.releasenote.application;

import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.releasenote.application.dto.request.DeployReleaseNoteRequestDto;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.ReleaseNoteRepository;
import com.kakaobean.core.releasenote.domain.ReleaseNoteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ReleaseNoteService {

    private final ReleaseNoteRepository releaseNoteRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ReleaseNoteValidator releaseNoteValidator;

    public void deployReleaseNote(DeployReleaseNoteRequestDto dto) {
        ProjectMember writer = projectMemberRepository.findByMemberIdAndProjectId(dto.getWriterId(), dto.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);
        releaseNoteValidator.validWriterAccess(writer);
        ReleaseNote releaseNote = dto.toEntity();
        releaseNoteRepository.save(releaseNote);
        releaseNote.deployed();
    }
}
