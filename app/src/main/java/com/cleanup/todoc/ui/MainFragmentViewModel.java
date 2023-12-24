package com.cleanup.todoc.ui;

import android.content.Context;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MainFragmentViewModel extends ViewModel {
    private static TaskDataRepository taskDataRepository;
    private static Executor executor;
    private final ProjectDataRepository projectDataRepository;
    private List<Project> allProjects;
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


    public void updateTask(Task task) {
        taskDataRepository.updateTask(task);
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
    public Project getProjectById(long id){
        return projectDataRepository.getProjectById(id);
    }
    public  List<Task> orderByLastToNew() {
        return taskDataRepository.orderByLastToNew();}
    public  List<Task> orderByNewToLast(){return taskDataRepository.orderByNewToLast();}

    public  List<Task> orderZA(){return taskDataRepository.orderZA();}

    public  List<Task> orderAZ(){return taskDataRepository.orderAZ();}
}