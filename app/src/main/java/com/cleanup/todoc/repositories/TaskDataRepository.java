package com.cleanup.todoc.repositories;

import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.ui.MainFragment;

import java.util.Collections;
import java.util.List;

public class TaskDataRepository {
    private final TaskDao taskDao;
    private List<Task> tasks;


    public TaskDataRepository(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public List<Task> getTasks() {
        return this.taskDao.getAll();
    }

    public void addTask(Task task) {
        this.taskDao.insertTask(task);
    }

    public void deleteTask(Task task) {
        this.taskDao.deleteTask(task);
    }

    public List<Task> orderByLastToNew() {
        tasks=getTasks();
        Collections.sort(tasks, new Task.TaskOldComparator());
        return this.taskDao.orderByLastToNew();
    }

    public List<Task> orderByNewToLast() {
        tasks=getTasks();
        Collections.sort(tasks, new Task.TaskRecentComparator());
        return this.taskDao.orderByNewToLast();
    }

    public List<Task> orderZA() {
        tasks=getTasks();
        Collections.sort(tasks, new Task.TaskZAComparator());
        return this.taskDao.orderZA();
    }

    public List<Task> orderAZ() {
        tasks=getTasks();
        Collections.sort(tasks, new Task.TaskAZComparator());
        return this.taskDao.orderAZ();
    }
    public void deleteAllTasks(List<Task> tasks){this.taskDao.deleteAllTasks(tasks);}
}

