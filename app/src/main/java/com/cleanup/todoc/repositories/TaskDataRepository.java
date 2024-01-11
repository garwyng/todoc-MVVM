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

    public Task getTask(Long TaskId) {
        return this.taskDao.getTaskById(TaskId);
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

    public void updateTask(Task task) {
        this.taskDao.updateTask(task);
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

    /**
     * List of all possible sort methods for task
     */
    private enum SortMethod {
        /**
         * Sort alphabetical by name
         */
        ALPHABETICAL,
        /**
         * Inverted sort alphabetical by name
         */
        ALPHABETICAL_INVERTED,
        /**
         * Lastly created first
         */
        RECENT_FIRST,
        /**
         * First created first
         */
        OLD_FIRST,
        /**
         * No sort
         */
        NONE

    }
}

