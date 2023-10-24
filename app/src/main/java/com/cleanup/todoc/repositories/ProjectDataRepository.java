package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.models.Project;

import java.util.List;


public class ProjectDataRepository {
    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public LiveData<Project> getProjectById(long projectId){return this.projectDao.getProjectById(projectId);}
    public LiveData<List<Project>> getAllProject(ProjectsCallback projectsCallback) {return this.projectDao.getAllProjects();}
    public void insert(Project project) {this.projectDao.insert(project);}
    public interface ProjectsCallback {
        void onProjectsLoaded(List<Project> projects);
    }
}
