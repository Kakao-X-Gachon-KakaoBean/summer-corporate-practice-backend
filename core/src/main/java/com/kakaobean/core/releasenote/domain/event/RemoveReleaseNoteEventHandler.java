package com.kakaobean.core.releasenote.domain.event;

import com.kakaobean.core.project.domain.event.RemovedProjectEvent;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.ReleaseNoteRepository;
import com.kakaobean.core.releasenote.domain.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * 프로젝트 삭제로 인한 이벤트 처리
 */
@Component
@RequiredArgsConstructor
public class RemoveReleaseNoteEventHandler {

    private final ReleaseNoteRepository releaseNoteRepository;
    private final HistoryRepository historyRepository;

    // 인수 테스트로만 비동기 테스트 가능/ 쓰레드 이슈
    @Async
    @TransactionalEventListener(RemovedProjectEvent.class)
    public void handle(RemovedProjectEvent event){
        List<ReleaseNote> removedReleaseNotes = releaseNoteRepository.deleteByProjectId(event.getProjectId());
        for (ReleaseNote releaseNote: removedReleaseNotes) {
            historyRepository.deleteByReleaseNoteId(releaseNote.getId());
        }
    }
}