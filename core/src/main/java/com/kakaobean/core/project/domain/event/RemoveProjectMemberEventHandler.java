package com.kakaobean.core.project.domain.event;

import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class RemoveProjectMemberEventHandler {

    private final ProjectMemberRepository projectMemberRepository;

//    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(RemovedProjectEvent.class)
    public void handle(RemovedProjectEvent event){
        projectMemberRepository.deleteByProjectId(event.getProjectId());
    }

}
