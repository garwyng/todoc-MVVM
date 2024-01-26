package com.cleanup.todoc.ui;

import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class MainFragmentViewModel extends ViewModel {
    private static TaskDataRepository taskDataRepository;
    private final ProjectDataRepository projectDataRepository;
    private List<Project> allProjects;
    private static List<Task> tasks=new ArrayList<>();

    public MainFragmentViewModel(ProjectDataRepository projectDataRepository, TaskDataRepository taskDataRepository) {
        MainFragmentViewModel.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
    }

    public static void addTask(Task task) {
        taskDataRepository.addTask(task);
    }

    public static void deleteTask(Task task) {
        taskDataRepository.deleteTask(task);
    }

    public List<Project> getAllProject() {
        allProjects = projectDataRepository.getAllProject();
        return allProjects;
    }

    public List<Task> getTasks() {
        List<Task> tasks = taskDataRepository.getTasks();
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        return tasks;
    }

    public Project getProjectById(long id) {

        return projectDataRepository.getProjectById(id);
    }

    public List<Task> orderByLastToNew() {

        return taskDataRepository.orderByLastToNew();
    }

    public List<Task> orderByNewToLast() {

        return taskDataRepository.orderByNewToLast();
    }

    public List<Task> orderZA() {

        return taskDataRepository.orderZA();
    }

    public List<Task> orderAZ() {
        Collections.sort(tasks, new Task.TaskAZComparator());
        return taskDataRepository.orderAZ();
    }
}