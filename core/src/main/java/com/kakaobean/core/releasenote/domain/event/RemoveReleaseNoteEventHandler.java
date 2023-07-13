package com.kakaobean.core.releasenote.domain.event;

import com.kakaobean.core.project.domain.event.RemovedProjectEvent;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RemoveReleaseNoteEventHandler {

    private final ReleaseNoteRepository releaseNoteRepository;

    @Async
    @Transactional
    @TransactionalEventListener(RemovedProjectEvent.class)
    public void handle(RemovedProjectEvent event){
        releaseNoteRepository.deleteByProjectId(event.getProjectId());
    }
}