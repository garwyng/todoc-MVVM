package com.cleanup.todoc.repositories;

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

    public List<Task> getTasks() {
        return TodocDatabase.getInstance(MainFragment.getInstanceFragment().getContext()).daoTask().getAll();
    }

    public void addTask(Task task) {
        this.taskDao.insertTask(task);
    }

    public void deleteTask(Task task) {
        this.taskDao.deleteTask(task);
    }

    public List<Task> orderByLastToNew() {
        return this.taskDao.orderByLastToNew();
    }

    public List<Task> orderByNewToLast() {
        return this.taskDao.orderByNewToLast();
    }

    public List<Task> orderZA() {
        return this.taskDao.orderZA();
    }

    public List<Task> orderAZ() {
        return this.taskDao.orderAZ();
    }
}

