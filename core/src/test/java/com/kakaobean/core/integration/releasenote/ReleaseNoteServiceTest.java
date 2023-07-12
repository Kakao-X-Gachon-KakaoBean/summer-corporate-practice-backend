package com.kakaobean.core.integration.releasenote;


import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectQueryRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.releasenote.application.ReleaseNoteService;
import com.kakaobean.core.releasenote.domain.repository.ReleaseNoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


import static com.kakaobean.core.factory.project.ProjectMemberFactory.*;
import static com.kakaobean.core.factory.project.dto.RegisterReleaseNoteRequestDtoFactory.*;
import static com.kakaobean.core.project.domain.ProjectRole.*;
import static org.assertj.core.api.Assertions.*;

public class ReleaseNoteServiceTest extends IntegrationTest {

    @Autowired
    private ReleaseNoteService releaseNoteService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectQueryRepository projectQueryRepository;

    @Autowired
    private ProjectMemberRepository projectMemberRepository;

    @Autowired
    private ReleaseNoteRepository releaseNoteRepository;

    @Test
    void 관리자가_릴리즈_노트를_등록한다(){
        //given

        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember admin = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        projectMemberRepository.save(createWithMemberIdAndProjectId(2L, project.getId(), MEMBER));
        projectMemberRepository.save(createWithMemberIdAndProjectId(3L, project.getId(), MEMBER));

        //when
        releaseNoteService.deployReleaseNote(createWithProjectIdAndWriterId(project.getId(), admin.getMemberId()));

        //then
        assertThat(releaseNoteRepository.findAll().size()).isEqualTo(1);
    }
}
