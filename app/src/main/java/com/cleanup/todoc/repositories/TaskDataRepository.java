package com.cleanup.todoc.repositories;

import androidx.lifecycle.LiveData;


import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.ui.MainFragment;

import java.util.List;

public class TaskDataRepository {
    private final TaskDao taskDao;

    public TaskDataRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public LiveData<Task> getATask(Long TaskId){return  this.taskDao.getTaskById(TaskId);}
    public LiveData<List<Task>> getTasks(TasksCallback tasksCallback){return TodocDatabase.getInstance(MainFragment.getInstanceFragment().getContext()).daoTask().getTasks();
    }

    public void addTask(Task task) {
        this.taskDao.insertTask(task);
    }

    public void deleteTask(Task task) {
        this.taskDao.deleteTask(task);
    }

    public void updateTask(Task task) { this.taskDao.updateTask(task);}
    public interface TasksCallback {
        void onTasksLoaded(List<Task> tasks);
    }
}
