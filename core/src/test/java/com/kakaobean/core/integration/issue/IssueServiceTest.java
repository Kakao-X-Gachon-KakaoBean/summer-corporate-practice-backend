package com.kakaobean.core.integration.issue;


import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.issue.application.IssueService;
import com.kakaobean.core.issue.domain.repository.CommentRepository;
import com.kakaobean.core.issue.domain.repository.IssueRepository;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.factory.issue.dto.RegisterIssueRequestDtoFactory.createWithId;
import static com.kakaobean.core.factory.project.ProjectMemberFactory.createWithMemberIdAndProjectId;
import static com.kakaobean.core.project.domain.ProjectRole.*;
import static org.assertj.core.api.Assertions.assertThat;


public class IssueServiceTest extends IntegrationTest {

    @Autowired
    IssueService issueService;

    @Autowired
    IssueRepository issueRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void beforeEach() {
        issueRepository.deleteAll();
        commentRepository.deleteAll();
        projectRepository.deleteAll();
        projectMemberRepository.deleteAll();
    }


    @Test
    void 일반_멤버가_이슈_생성에_성공한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));

        // when
        issueService.registerIssue(createWithId(project.getId(), projectMember.getMemberId()));

        // then
        assertThat(issueRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 게스트_멤버가_이슈_생성에_성공한다(){
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), VIEWER));

        // when
        issueService.registerIssue(createWithId(project.getId(), projectMember.getMemberId()));

        // then
        assertThat(issueRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 관리자가_이슈_생성에_성공한다(){
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));

        // when
        issueService.registerIssue(createWithId(project.getId(), projectMember.getMemberId()));

        // then
        assertThat(issueRepository.findAll().size()).isEqualTo(1);
    }
}
