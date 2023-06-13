package com.digdes.java.ddproject.app.integrations.jpa;

import com.digdes.java.ddproject.app.MainApp;
import com.digdes.java.ddproject.app.integrations.IntegrationEnvironment;
import com.digdes.java.ddproject.common.enums.ProjectStatus;
import com.digdes.java.ddproject.common.enums.Role;
import com.digdes.java.ddproject.dto.filters.SearchProjectFilterDto;
import com.digdes.java.ddproject.dto.member.MemberDto;
import com.digdes.java.ddproject.dto.project.ProjectDto;
import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.Project;
import com.digdes.java.ddproject.model.ProjectTeam;
import com.digdes.java.ddproject.repositories.jpa.MemberRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.ProjectRepositoryJpa;
import com.digdes.java.ddproject.repositories.jpa.ProjectTeamRepositoryJpa;
import com.digdes.java.ddproject.services.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest(classes = MainApp.class)
class ProjectIntegrationTest extends IntegrationEnvironment {

    @Autowired
    ProjectRepositoryJpa projectRepository;

    @Autowired
    MemberRepositoryJpa memberRepository;

    @Autowired
    ProjectTeamRepositoryJpa projectTeamRepository;

    @Autowired
    ProjectService projectService;

    @Test
    void findById_ProjectWithSuchIdNotExist_ReturnEmptyMemberDto() {
        Long id = getRandomLong();

        ProjectDto foundProject = projectService.findById(id);

        Assertions.assertNull(foundProject.getTitle());
    }

    @Test
    void findById_ProjectWithSuchIdExist_ReturnMemberDto() {
        Long id = getRandomLong();
        Project project = createProject(id);
        projectRepository.save(project);

        ProjectDto foundProject = projectService.findById(id);

        Assertions.assertEquals(project.getTitle(), foundProject.getTitle());
    }

    @Test
    void create_ProjectWithSuchIdNotExist_ProjectCreated() {
        ProjectDto project = createProjectDto(getRandomLong());

        projectService.create(project);

        Optional<Project> createdProject = projectRepository.findById(project.getId());
        Assertions.assertTrue(createdProject.isPresent());
        Assertions.assertEquals(createdProject.get().getTitle(), project.getTitle());

    }

    @Test
    void create_ProjectWithSuchIdAlreadyExist_Exception() {
        Long id = getRandomLong();
        Project project = createProject(id);
        projectRepository.save(project);
        ProjectDto otherProject = createProjectDto(id);

        Assertions.assertThrows(DuplicateKeyException.class, () -> projectService.create(otherProject));
    }

    @Test
    void update_ProjectWithSuchIdNotExist_Exception() {
        ProjectDto project = createProjectDto();
        Long id = getRandomLong();

        Assertions.assertThrows(NoSuchElementException.class, () -> projectService.update(id, project));
    }

    @Test
    void update_ProjectWithSuchIdExist_MemberUpdated() {
        Long id = getRandomLong();
        projectRepository.save(createProject(id));
        ProjectDto project = createProjectDto();

        projectService.update(id, project);

        Optional<Project> updatedProject = projectRepository.findById(id);

        Assertions.assertTrue(updatedProject.isPresent());
        Assertions.assertEquals(project.getTitle(), updatedProject.get().getTitle());
    }

    @Test
    void updateStatus_ProjectWithSuchIdNotExist_Exception() {
        Long id = getRandomLong();
        ProjectStatus status = ProjectStatus.TESTING;

        Assertions.assertThrows(NoSuchElementException.class, () -> projectService.updateStatus(id, status));
    }

    @Test
    void updateStatus_ProjectWithSuchIdExist_MemberStatusUpdated() {
        Long id = getRandomLong();
        ProjectStatus status = ProjectStatus.TESTING;
        Project originalProject = createProject(id);
        projectRepository.save(originalProject);

        projectService.updateStatus(id, status);

        Optional<Project> updatedProject = projectRepository.findById(id);

        Assertions.assertTrue(updatedProject.isPresent());
        Assertions.assertEquals(originalProject.getTitle(), updatedProject.get().getTitle());
        Assertions.assertEquals(status, updatedProject.get().getStatus());
    }

    @Test
    void search_SearchByTitle_ReturnProjectList() {
        Project targetProject = projectRepository.save(createProject(getRandomLong()));

        SearchProjectFilterDto filter = new SearchProjectFilterDto();
        filter.setTitle(targetProject.getTitle());
        List<ProjectDto> projects = projectService.search(filter);

        Assertions.assertEquals(1, projects.size());
        Assertions.assertEquals(projects.get(0).getTitle(), targetProject.getTitle());
    }

    @Test
    void addMember_ProjectWithSuchIdNotExist_Exception() {
        Long projectId = getRandomLong();
        Long memberId = getRandomLong();
        Role role = Role.TESTER;

        Assertions.assertThrows(NoSuchElementException.class, () -> projectService.addMember(projectId, memberId, role));
    }

    @Test
    void addMember_MemberWithSuchIdNotExist_Exception() {
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        Long memberId = getRandomLong();
        Role role = Role.TESTER;

        Assertions.assertThrows(NoSuchElementException.class, () -> projectService.addMember(projectId, memberId, role));
    }

    @Test
    void addMember_MemberAndProjectWithSuchIdExists_MemberAddToProject() {
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        Member member = createMember();
        Long memberId = memberRepository.save(member).getId();
        Role role = Role.TESTER;

        projectService.addMember(projectId, memberId, role);

        List<Member> team = projectTeamRepository.findByProjectId(projectId);
        Assertions.assertEquals(1, team.size());
        Assertions.assertEquals(member.getFirstName(), team.get(0).getFirstName());
    }

    @Test
    void deleteMember_MemberAndProjectWithSuchIdExists_MemberDeleted() {
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        Member member = createMember();
        Long memberId = memberRepository.save(member).getId();
        Role role = Role.TESTER;
        projectTeamRepository.save(new ProjectTeam(projectId, memberId, role));

        projectService.deleteMember(projectId, memberId);

        List<Member> team = projectTeamRepository.findByProjectId(projectId);
        Assertions.assertTrue(team.isEmpty());
    }

    @Test
    void deleteMember_MemberOrProjectWithSuchIdNotExists_NothingChanges() {
        Long projectId = getRandomLong();
        Long memberId = getRandomLong();

        projectService.deleteMember(projectId, memberId);

        List<Member> team = projectTeamRepository.findByProjectId(projectId);
        Assertions.assertTrue(team.isEmpty());
    }

    @Test
    void listMember_ProjectWithSuchIdExists_ReturnMemberDtoList() {
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));
        Member firstMember = createMember();
        Member secondMember = createMember();
        Role role = Role.TESTER;
        Long firstMemberId = memberRepository.save(firstMember).getId();
        Long secondMemberId = memberRepository.save(secondMember).getId();
        projectTeamRepository.save(new ProjectTeam(projectId, firstMemberId, role));
        projectTeamRepository.save(new ProjectTeam(projectId, secondMemberId, role));

        List<MemberDto> team = projectService.listAllMembers(projectId);

        Assertions.assertEquals(2, team.size());
        Assertions.assertEquals(firstMember.getFirstName(), team.get(0).getFirstName());
        Assertions.assertEquals(secondMember.getFirstName(), team.get(1).getFirstName());
    }

    @Test
    void listMember_ProjectWithSuchIdNotExists_ReturnEmptyMemberDtoList() {
        Long projectId = getRandomLong();

        List<MemberDto> team = projectService.listAllMembers(projectId);

        Assertions.assertTrue(team.isEmpty());
    }

    @Test
    void listMember_ProjectWithSuchIdExistsButHasNoMembers_ReturnEmptyMemberDtoList() {
        Long projectId = getRandomLong();
        projectRepository.save(createProject(projectId));

        List<MemberDto> team = projectService.listAllMembers(projectId);

        Assertions.assertTrue(team.isEmpty());
    }
}
