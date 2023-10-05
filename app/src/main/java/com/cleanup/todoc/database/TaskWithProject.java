package com.cleanup.todoc.database;

import androidx.room.Embedded;

import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;

public class TaskWithProject {
    @Embedded
    Task task;
    @Embedded(prefix = Project.PREFIX)
    Project project;
}
