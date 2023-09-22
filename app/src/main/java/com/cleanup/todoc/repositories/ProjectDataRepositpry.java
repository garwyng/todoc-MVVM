package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.models.Project;


public class ProjectDataRepositpry {
    private final ProjectDao projectDao;

    public ProjectDataRepositpry(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public LiveData<Project> getProject(long projectId){return this.projectDao.getProject(projectId);}
}
