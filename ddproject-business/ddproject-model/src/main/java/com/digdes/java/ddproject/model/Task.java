package com.digdes.java.ddproject.model;

import com.digdes.java.ddproject.common.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
public class Task {
    //    Task's id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    //    Brief information about the task
    @Column(name = "title")
    private String title;
    //    More detailed information about the task
    @Column(name = "description")
    private String description;
    //    Member responsible for this task
    @ManyToOne
    @JoinColumn(name = "executor_id")
    private Member executor;
    //    Project to which the task is attached
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    //    Time to complete the task in hours
    @Column(name = "labor_hours")
    private Long laborHours;
    //    Deadline by which the task must be completed
    @Column(name = "deadline")
    private OffsetDateTime deadline;
    //    Task's status. May be:  NEW, IN_WORK, COMPLETED, CLOSED
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;
    //    Member who last modified the task
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;
    //    Task's creation date
    @Column(name = "creation_date")
    private OffsetDateTime creationDate;
    //    Task's last update date
    @Column(name = "last_update_date")
    private OffsetDateTime lastUpdateDate;
}
