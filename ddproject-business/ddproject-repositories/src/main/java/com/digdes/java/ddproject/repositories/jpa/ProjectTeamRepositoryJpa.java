package com.digdes.java.ddproject.repositories.jpa;

import com.digdes.java.ddproject.model.Member;
import com.digdes.java.ddproject.model.ProjectTeam;
import com.digdes.java.ddproject.model.ProjectTeamPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectTeamRepositoryJpa extends JpaRepository<ProjectTeam, ProjectTeamPK> {

    @Query(value = """
            select m
            from Member m
            join ProjectTeam pt on m.id = pt.id.member.id
            join Project p on p.id = pt.id.project.id
            where p.id = :projectId and m.status <> 'DELETED'
            """)
    List<Member> findByProjectId(@Param("projectId") Long id);

}
