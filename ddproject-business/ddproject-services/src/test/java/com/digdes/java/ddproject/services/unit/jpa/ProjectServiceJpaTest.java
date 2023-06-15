package com.digdes.java.ddproject.services.unit.jpa;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import com.digdes.java.ddproject.common.enums.Role;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import com.digdes.java.ddproject.mapping.filters.SearchProjectFilterMapper;
import com.digdes.java.ddproject.mapping.member.MemberMapper;
import com.digdes.java.ddproject.mapping.project.ProjectMapper;
import com.digdes.java.ddproject.mapping.useraccount.UserAccountMapper;
import com.digdes.java.ddproject.model.Project;
import com.digdes.java.ddproject.model.ProjectTeam;
import com.digdes.java.ddproject.repositories.jpa.MemberRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.ProjectRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.ProjectTeamRepositoryJpa;
import com.digdes.java.ddproject.services.ProjectTeamService;
import com.digdes.java.ddproject.services.jpa.MemberServiceJpa;
import com.digdes.java.ddproject.services.jpa.ProjectServiceJpa;
import com.digdes.java.ddproject.services.jpa.ProjectTeamServiceJpa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceJpaTest {
    @Spy
    private ProjectRepositoryJpa projectRepository;

    @Spy
    private ProjectMapper projectMapper;
    @Spy
    private SearchProjectFilterMapper filterMapper;
    @Mock
    private MemberServiceJpa memberService;
    @Mock
    private ProjectTeamServiceJpa projectTeamService;

    private ProjectServiceJpa projectService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        projectService = new ProjectServiceJpa(projectRepository, projectTeamService, memberService, projectMapper, filterMapper);
    }

    protected ProjectDto createProjectDto() {
        return ProjectDto.builder()
                .title(UUID.randomUUID().toString())
                .status(ProjectStatus.DRAFT)
                .build();
    }

    protected ProjectDto createProjectDto(Long id) {
        return ProjectDto.builder()
                .id(id)
                .title(UUID.randomUUID().toString())
                .status(ProjectStatus.DRAFT)
                .build();
    }

    protected Project createProject(Long id) {
        return Project.builder()
                .id(id)
                .title(UUID.randomUUID().toString())
                .status(ProjectStatus.DRAFT)
                .build();
    }

    private Long getRandomLong() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }


    @Test
    void findById_ProjectWithSuchIdNotExist_ReturnEmptyProjectDto() {
        when(projectRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertNull(projectService.findById(1L).getTitle());
    }

    @Test
    void findById_MemberWithSuchIdExist_ReturnMemberDto() {
        Long id = getRandomLong();
        Project project = createProject(id);

        when(projectRepository.findById(any())).thenReturn(Optional.of(project));

        Assertions.assertEquals(project.getTitle(), projectService.findById(id).getTitle());
    }
    @Test
    void create_ProjectWithSuchIdNotExist_ProjectCreated() {
        Long id = getRandomLong();
        Project project = createProject(id);
        when(projectRepository.findById(any())).thenReturn(Optional.empty());
        when(projectRepository.save(any())).thenReturn(project);

        ProjectDto createdProject = projectService.create(projectMapper.toProjectDto(project));

        Assertions.assertNotNull(createdProject);
        Assertions.assertEquals(project.getTitle(), createdProject.getTitle());
    }

    @Test
    void create_ProjectWithSuchIdAlreadyExist_Exception() {
        Long id = getRandomLong();
        Project project = createProject(id);
        when(projectRepository.findById(any())).thenReturn(Optional.of(new Project()));

        Assertions.assertThrows(DuplicateKeyException.class, () -> projectService.create(projectMapper.toProjectDto(project)));
    }
    @Test
    void update_ProjectWithSuchIdNotExist_Exception() {
        Long id = getRandomLong();
        Project project = createProject(id);
        when(projectRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> projectService.update(id, projectMapper.toProjectDto(project)));
    }

    @Test
    void update_ProjectWithSuchIdExist_ProjectUpdated() {
        Long id = getRandomLong();
        Project originalProject = createProject(id);
        when(projectRepository.findById(any())).thenReturn(Optional.of(originalProject));
        Project project = createProject(id);
        when(projectRepository.save(any())).thenReturn(project);

        ProjectDto updatedProject = projectService.update(id, projectMapper.toProjectDto(project));

        Assertions.assertNotNull(updatedProject);
        Assertions.assertEquals(project.getTitle(), updatedProject.getTitle());
    }

    @Test
    void updateStatus_ProjectWithSuchIdNotExist_Exception() {
        Long id = getRandomLong();
        when(projectRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> projectService.updateStatus(id, ProjectStatus.TESTING));
    }

    @Test
    void updateStatus_ProjectWithSuchIdExist_ProjectStatusUpdated() {
        Long id = getRandomLong();
        Project originalProject = createProject(id);
        when(projectRepository.findById(any())).thenReturn(Optional.of(originalProject));
        Project project = createProject(id);
        when(projectRepository.save(any())).thenReturn(project);

        ProjectDto updatedProject = projectService.updateStatus(id, ProjectStatus.TESTING);

        Assertions.assertNotNull(updatedProject);
        Assertions.assertEquals(project.getTitle(), updatedProject.getTitle());
    }

    @Test
    void addMember_ProjectWithSuchIdNotExist_Exception() {
        Long projectId = getRandomLong();
        Long memberId = getRandomLong();
        when(projectRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> projectService.addMember(projectId, memberId, Role.TESTER));
    }

    @Test
    void addMember_MemberWithSuchIdNotExist_Exception() {
        Long projectId = getRandomLong();
        Long memberId = getRandomLong();
        when(projectRepository.findById(any())).thenReturn(Optional.of(createProject(projectId)));
        when(memberService.isMemberExist(any())).thenReturn(false);

        Assertions.assertThrows(NoSuchElementException.class, () -> projectService.addMember(projectId, memberId, Role.TESTER));
    }

}
