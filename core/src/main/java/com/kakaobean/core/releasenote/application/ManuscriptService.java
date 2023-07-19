package com.kakaobean.core.releasenote.application;

import com.kakaobean.core.releasenote.application.dto.request.RegisterManuscriptRequestDto;
import com.kakaobean.core.releasenote.domain.Manuscript;
import com.kakaobean.core.releasenote.domain.repository.ManuscriptRepository;
import com.kakaobean.core.releasenote.domain.ManuscriptValidator;
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
}
