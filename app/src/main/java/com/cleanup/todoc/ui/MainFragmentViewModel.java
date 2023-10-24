package com.cleanup.todoc.ui;

import static com.cleanup.todoc.R.raw.projects;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Ignore;

import com.cleanup.todoc.R;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainFragmentViewModel extends ViewModel {


    private MutableLiveData<List<Project>> projectLiveData;
    private MutableLiveData<List<Task>> taskLiveData;
    private static TaskDataRepository taskDataRepository;
    private final ProjectDataRepository projectDataRepository;
    private static Executor executor;
    private Context context = MainFragment.getInstanceFragment().getContext();
    private TodocDatabase db = TodocDatabase.getInstance(context);
    private EditText dialogEditText;
    private Spinner dialogSpinner;

    public MainFragmentViewModel(ProjectDataRepository projectDataRepository, TaskDataRepository taskDataRepository, Executor executor) {
        this.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
        this.executor =executor;
    }
    public LiveData<List<Project>> getProjects() {
        if (projectLiveData == null) {
            projectLiveData = new MutableLiveData<>();
            loadProjects();
        }
        return projectLiveData;
    }
    public LiveData<List<Task>> getTasks() {
        if (taskLiveData == null) {
            taskLiveData = new MutableLiveData<>();
            loadTasks();
        }
        return taskLiveData;
    }
    private void loadProjects() {
        projectDataRepository.getAllProject(new ProjectDataRepository.ProjectsCallback() {
            @Override
            public void onProjectsLoaded(List<Project> projects) {
                projectLiveData.setValue(projects);
            }
        });
    }
    private void loadTasks() {
        taskDataRepository.getTasks(new TaskDataRepository.TasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                taskLiveData.setValue(tasks);
            }
        });
    }
    public static void  addTask(Task task){
        executor.execute(()->{
            taskDataRepository.addTask(task);
        });
    }
    public void  deleteTask(Task task){
        executor.execute(()-> taskDataRepository.deleteTask(task));
    }

}