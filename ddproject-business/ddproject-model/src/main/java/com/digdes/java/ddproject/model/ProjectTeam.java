package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "project_team")
public class ProjectTeam {
    @EmbeddedId
    private PK id;
    //    Participant's role in project. May be: PROJECT_MANAGER, ANALYST, DEVELOPER, TESTER
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    public ProjectTeam(Long projectId, Long memberId) {
        this.id = new PK(Member.builder().id(memberId).build(), Project.builder().id(projectId).build());
    }

    public ProjectTeam(Long projectId, Long memberId, Role role) {
        this(projectId, memberId);
        this.role = role;
    }

    @Data
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PK implements Serializable {
        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "member_id")
        private Member member;

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "project_id")
        private Project project;

    }
}
