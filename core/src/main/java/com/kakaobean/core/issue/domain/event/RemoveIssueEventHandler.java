package com.kakaobean.core.issue.domain.event;

import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.project.domain.event.RemovedProjectEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Configuration
@RequiredArgsConstructor
public class RemoveIssueEventHandler {

    private final IssueRepository issueRepository;

    @Async
    @Transactional
    @TransactionalEventListener(RemovedProjectEvent.class)
    public void handler(RemovedProjectEvent event){
        issueRepository.deleteByProjectId(event.getProjectId());
    }
}
