package com.kakaobean.core.releasenote.domain.event;

import com.kakaobean.core.project.domain.event.RemovedProjectEvent;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.repository.HistoryRepository;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoveReleaseNoteEventHandler {

    private final ReleaseNoteRepository releaseNoteRepository;
    private final HistoryRepository historyRepository;

    @Async
    @Transactional
    @TransactionalEventListener(RemovedProjectEvent.class)
    public void handle(RemovedProjectEvent event){
        List<ReleaseNote> releaseNotes = releaseNoteRepository.findByProjectId(event.getProjectId());
        for (ReleaseNote releaseNote: releaseNotes) {
            historyRepository.deleteByReleaseNoteId(releaseNote.getId());
        }
        releaseNoteRepository.deleteByProjectId(event.getProjectId());
    }
}