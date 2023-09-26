package com.cleanup.todoc.utils;


import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.ProjectList;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.models.TaskList;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonReader {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks;
    private List<Project> projects;

    public List<Task> getTaskListFromFile(){
        try {
            File jsonFile = new File("app/sampledata/tasks.json");
            TaskList tasksList = objectMapper.readValue(jsonFile, TaskList.class);
            tasks = tasksList.getTaskslist();

        }catch (IOException e){
            e.printStackTrace();
        }
        return tasks;

    }
    public List<Project> getProjectsListFromFile(){
        try {
            File jsonFile = new File("app/sampledata/projects.json");
            ProjectList projectList = objectMapper.readValue(jsonFile, ProjectList.class);
            projects = projectList.getProjectslist();

        }catch (IOException e){
            e.printStackTrace();
        }
        return projects;

    }
}
