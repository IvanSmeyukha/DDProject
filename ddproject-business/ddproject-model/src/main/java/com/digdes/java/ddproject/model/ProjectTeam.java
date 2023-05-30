package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "project_team")
public class ProjectTeam {
    @EmbeddedId
    private ProjectTeamPK id;
    //    Participant's role in project. May be: PROJECT_MANAGER, ANALYST, DEVELOPER, TESTER
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public ProjectTeam(Long projectId, Long memberId) {
        this.id = new ProjectTeamPK(Member.builder().id(memberId).build(), Project.builder().id(projectId).build());
    }

    public ProjectTeam(Long projectId, Long memberId, Role role) {
        this(projectId, memberId);
        this.role = role;
    }
}
