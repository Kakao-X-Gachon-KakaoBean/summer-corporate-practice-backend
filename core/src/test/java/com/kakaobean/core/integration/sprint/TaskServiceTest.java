package com.kakaobean.core.integration.sprint;

import com.kakaobean.core.factory.project.ProjectFactory;
import com.kakaobean.core.factory.sprint.dto.RegisterTaskRequestDtoFactory;
import com.kakaobean.core.integration.IntegrationTest;
import com.kakaobean.core.project.domain.Project;
import com.kakaobean.core.project.domain.ProjectMember;
import com.kakaobean.core.project.domain.repository.ProjectMemberRepository;
import com.kakaobean.core.project.domain.repository.ProjectRepository;
import com.kakaobean.core.sprint.Exception.TaskAccessException;
import com.kakaobean.core.sprint.application.TaskService;
import com.kakaobean.core.sprint.domain.Sprint;
import com.kakaobean.core.sprint.domain.repository.SprintRepository;
import com.kakaobean.core.sprint.domain.repository.TaskRepository;
import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.kakaobean.core.factory.project.ProjectMemberFactory.createWithMemberIdAndProjectId;
import static com.kakaobean.core.factory.sprint.SprintFactory.createWithId;
import static com.kakaobean.core.project.domain.ProjectRole.ADMIN;
import static com.kakaobean.core.project.domain.ProjectRole.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TaskServiceTest extends IntegrationTest {

    @Autowired
    TaskService taskService;

    @Autowired
    SprintRepository sprintRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @BeforeEach
    void beforeEach() {
        sprintRepository.deleteAll();
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        projectMemberRepository.deleteAll();
    }

    @Test
    void 테스크를_생성한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), ADMIN));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));

        // when
        taskService.registerTask(RegisterTaskRequestDtoFactory.createWithId(sprint.getId(), projectMember.getMemberId()));

        // then
        assertThat(taskRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    void 일반멤버는_테스크를_생성하지_못한다() {
        // given
        Project project = projectRepository.save(ProjectFactory.create());
        ProjectMember projectMember = projectMemberRepository.save(createWithMemberIdAndProjectId(1L, project.getId(), MEMBER));
        Sprint sprint = sprintRepository.save(createWithId(project.getId()));

        // when
        AbstractThrowableAssert<?, ? extends Throwable> result = assertThatThrownBy(() -> {
            taskService.registerTask(RegisterTaskRequestDtoFactory.createWithId(sprint.getId(), projectMember.getMemberId()));
        });

        // then
        result.isInstanceOf(TaskAccessException.class);
    }

}
