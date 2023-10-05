package com.cleanup.todoc.ui;


import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final ProjectDataRepository projectDataRepository;
    private final TaskDataRepository taskDataRepository;
    private final Executor executor;

    private static ViewModelFactory factory;
    public static  ViewModelFactory getInstance(Context context){
        if (factory == null){
            synchronized (ViewModelFactory.class){
                if (factory==null){
                    factory = new ViewModelFactory(context);
                }
            }
        }
        return factory;
    }
    private  ViewModelFactory(Context context){
        TodocDatabase database = TodocDatabase.getInstance(context);
        this .projectDataRepository = new ProjectDataRepository(database.daoProject());
        this.taskDataRepository = new TaskDataRepository(database.daoTask());
        this.executor = Executors.newSingleThreadExecutor();
    }
    @Override
    @NotNull
    public <T extends ViewModel> T create(Class<T> modelClass){
        if (modelClass.isAssignableFrom(MainFragmentViewModel.class)) {
        return (T) new MainFragmentViewModel(projectDataRepository,taskDataRepository,executor);
        }

        throw new IllegalArgumentException("unknow ViewMdolel class");
    }
}
