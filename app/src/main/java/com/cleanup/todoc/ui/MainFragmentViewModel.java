package com.cleanup.todoc.ui;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.R;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executor;

public class MainFragmentViewModel extends ViewModel {


    public static List<Project> allProjects;
    // TODO: Implement the ViewModel
    private static TaskDataRepository taskDataRepository;
    private final ProjectDataRepository projectDataRepository;
    private static Executor executor;

    //DATA
    @Nullable
    private LiveData<Task> currentTask;

    public MainFragmentViewModel(ProjectDataRepository projectDataRepository, TaskDataRepository taskDataRepository, Executor executor) {
        this.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
        this.executor =executor;
    }

    public void init(){
        TodocDatabase database= TodocDatabase.getInstance(MainFragment.getInstanceFragment().getContext());
        Context context= MainFragment.getInstanceFragment().getContext();
        InputStream inputStream = context.getResources().openRawResource(R.raw.projects);
        Gson gson = new Gson();
        String jsonFilePath ="app/src/main/res/raw/projects.json";

        try {
            FileReader reader = new FileReader(jsonFilePath);
            Project[] projects = gson.fromJson(reader, Project[].class);

            for (Project project : projects) {
                database.daoProject().insert(project);
            }

            reader.close();
        } catch (IOException e) {
            // Handle any exceptions that may occur during parsing or database insertion
            e.printStackTrace();
        }
    }
    public static void  createTask(Task task){
        executor.execute(()->{
            taskDataRepository.addTask(task);
        });
    }
    public void  deleteTask(Task task){
        executor.execute(()-> taskDataRepository.deleteTask(task));
    }
    public void updateTask(Task task){
        executor.execute(()-> taskDataRepository.updateTask(task));
    }
    public void getAllProject(){
        executor.execute(()->{allProjects= projectDataRepository.getAllProject();}
        );
    }


}