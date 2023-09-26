package com.cleanup.todoc.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepositpry;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.io.Closeable;
import java.util.concurrent.Executor;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    /*private TaskDataRepository taskDataRepository;
    private final ProjectDataRepository projectDataRepository;
    private final Executor executor;

    //DATA
    @Nullable
    private LiveData<Task> currentTask;

    public MainViewModel(TaskDataRepository taskDataRepository, ProjectDataRepository projectDataRepository, Executor executor) {
        this.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
        this.executor = executor;
    }
    public void init(){
        TodocDatabase.getInstance(MainFragment.newInstance().getContext());
    }

    public MainViewModel(TaskDataRepository taskDataRepository, ProjectDataRepository projectDataRepository, Executor executor, @NonNull Closeable... closeables) {
        super(closeables);
        this.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
        this.executor = executor;
    }
    public void  createTask(long id,long projectID,String text,long creationTimestamp){
        executor.execute(()->{
            taskDataRepository.addTask(new Task(id,projectID,text,creationTimestamp));
        });
    }
    public void  deleteTask(Task task){
        executor.execute(()-> taskDataRepository.deleteTask(task));
    }
    public void updateTask(Task task){
        executor.execute(()-> taskDataRepository.updateTask(task));
    }*/
}