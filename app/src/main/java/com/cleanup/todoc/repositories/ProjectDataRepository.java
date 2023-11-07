package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.models.Project;

import java.util.List;


public class ProjectDataRepository {
    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public Project getProjectById(long projectId){return this.projectDao.getProjectById(projectId);}
    public List<Project> getAllProject() {return this.projectDao.getAllProjects();
    }
    public void insert(Project project) {this.projectDao.insert(project);}
}
