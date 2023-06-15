package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class Project implements Serializable {
//    Project's id
    @Id
    @Column(name = "id")
    private Long id;
//    Short name of the project
    @Column(name = "title")
    private String title;
//    More detailed information about the project
    @Column(name = "description")
    private String description;
//    Project's status. May be: DRAFT -> DEVELOPMENT -> TESTING -> RELEASE
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProjectStatus status;

}
