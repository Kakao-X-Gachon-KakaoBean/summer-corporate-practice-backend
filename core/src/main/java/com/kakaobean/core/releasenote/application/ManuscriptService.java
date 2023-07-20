package com.kakaobean.core.releasenote.application;

import com.kakaobean.core.releasenote.application.dto.request.ModifyManuscriptRequestDto;
import com.kakaobean.core.releasenote.application.dto.request.RegisterManuscriptRequestDto;
import com.kakaobean.core.releasenote.application.dto.response.ManuscriptResponseDto;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.ManuscriptStatus;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.domain.ManuscriptValidator;
import com.kakaobean.core.releasenote.exception.NotExistsManuscriptException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ManuscriptService {

    private final ManuscriptRepository manuscriptRepository;
    private final ManuscriptValidator manuscriptValidator;

    @Transactional
    public void registerManuscript(RegisterManuscriptRequestDto dto) {
        Manuscript manuscript = dto.toEntity();
        manuscriptValidator.valid(manuscript);
        manuscriptRepository.save(manuscript);
        manuscript.registered();
    }

    @Transactional
    public ManuscriptResponseDto hasRightToModifyManuscript(Long memberId, Long manuscriptId) {
        Manuscript manuscript = manuscriptRepository.findByIdWithPESSIMISTICLock(manuscriptId)
                .orElseThrow(NotExistsManuscriptException::new);
        manuscriptValidator.validRightToModify(manuscript, memberId);
        manuscript.modifyManuscriptStatus(ManuscriptStatus.Modifying);
        return new ManuscriptResponseDto(manuscript.getId(), manuscript.getTitle(), manuscript.getContent(), manuscript.getVersion());
    }

    @Transactional
    public void modifyManuscript(ModifyManuscriptRequestDto dto) {
        Manuscript manuscript = manuscriptRepository.findById(dto.getManuscriptId())
                .orElseThrow(NotExistsManuscriptException::new);
        manuscriptValidator.isModifiable(manuscript, dto.getEditingMemberId());
        manuscript.modify(dto.getTitle(), dto.getContent(), dto.getVersion(), dto.getEditingMemberId());
    }

    @Transactional
    public void deleteManuscript(Long adminId, Long manuscriptId) {
        Manuscript manuscript = manuscriptRepository.findById(manuscriptId)
                .orElseThrow(NotExistsManuscriptException::new);
        manuscriptValidator.validRightToDelete(manuscript, adminId);
        manuscriptRepository.delete(manuscript);
    }
}
