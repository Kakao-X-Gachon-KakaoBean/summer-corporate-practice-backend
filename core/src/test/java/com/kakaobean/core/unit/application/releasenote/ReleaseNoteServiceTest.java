package com.kakaobean.core.unit.application.releasenote;


import com.kakaobean.core.common.event.Events;
import com.kakaobean.core.factory.releasenote.dto.RegisterReleaseNoteRequestDtoFactory;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.releasenote.application.ReleaseNoteService;
import com.kakaobean.core.releasenote.domain.ReleaseNote;
import com.kakaobean.core.releasenote.domain.event.ReleaseNoteDeployedEvent;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import com.kakaobean.core.releasenote.domain.ReleaseNoteValidator;
import com.kakaobean.core.unit.UnitTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Optional;

import static com.kakaobean.core.factory.project.ProjectMemberFactory.*;
import static org.mockito.BDDMockito.*;

public class ReleaseNoteServiceTest extends UnitTest {

    private ReleaseNoteService releaseNoteService;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private ReleaseNoteRepository releaseNoteRepository;

    private static MockedStatic<Events> mockEvents;


    @BeforeEach
    void beforeEach(){
        releaseNoteService = new ReleaseNoteService(
                releaseNoteRepository,
                new ReleaseNoteValidator(projectMemberRepository)
        );
        mockEvents = mockStatic(Events.class);
    }

    @AfterEach
    void afterEach(){
        mockEvents.close();
    }


    @Test
    void 관리자가_릴리즈_노트를_등록한다(){
        //given
        ProjectMember writer = createAdmin();

        given(projectMemberRepository.findByMemberIdAndProjectId(Mockito.anyLong(), Mockito.anyLong()))
                .willReturn(Optional.of(writer));

        //when
        releaseNoteService.deployReleaseNote(RegisterReleaseNoteRequestDtoFactory.create());

        //then
        verify(releaseNoteRepository, times(1)).save(Mockito.any(ReleaseNote.class));
        mockEvents.verify(() -> Events.raise(Mockito.any(ReleaseNoteDeployedEvent.class)), times(1));
    }
}
