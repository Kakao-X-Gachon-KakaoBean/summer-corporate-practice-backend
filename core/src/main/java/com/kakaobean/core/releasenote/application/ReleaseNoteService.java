package com.kakaobean.core.releasenote.application;

import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.core.project.exception.NotExistsProjectMemberException;
import com.kakaobean.core.releasenote.application.dto.request.RegisterReleaseNoteRequestDto;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.ReleaseNoteRepository;
import com.kakaobean.core.releasenote.domain.ReleaseNoteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.kakaobean.core.releasenote.domain.ReleaseNoteRegisteredEvent.*;
import static java.util.stream.Collectors.*;

@Transactional
@Service
@RequiredArgsConstructor
public class ReleaseNoteService {

    private final ReleaseNoteRepository releaseNoteRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectQueryRepository projectQueryRepository;

    private final ReleaseNoteValidator releaseNoteValidator;

    public void registerReleaseNote(RegisterReleaseNoteRequestDto dto) {
        ProjectMember writer = projectMemberRepository.findByMemberIdAndProjectId(dto.getWriterId(), dto.getProjectId())
                .orElseThrow(NotExistsProjectMemberException::new);
        releaseNoteValidator.validWriterAccess(writer);
        ReleaseNote releaseNote = releaseNoteRepository.save(dto.toEntity());
        releaseNote.registered(findNotifiedProjectMemberInfos(releaseNote.getProjectId()));
    }

    private List<NotifiedTargetInfo> findNotifiedProjectMemberInfos(Long projectId) {
        return projectQueryRepository
                .findProjectMembers(projectId)
                .stream()
                .map(info -> new NotifiedTargetInfo(info.getProjectMemberId(), info.getProjectMemberEmail()))
                .collect(toList());
    }
}
