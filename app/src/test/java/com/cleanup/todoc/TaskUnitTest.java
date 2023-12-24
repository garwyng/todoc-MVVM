package com.cleanup.todoc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.ui.MainActivity;
import com.cleanup.todoc.ui.MainFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.manipulation.Ordering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Unit tests for tasks
 *
 * @author Gaëtan HERFRAY
 */
public class TaskUnitTest {
    TodocDatabase database;
    Context context;
    @Before
    public void start(){

        database = TodocDatabase.getInstance(context);
    }
    @Test
    public void test_projects() {
        final Task task1 = new Task(1, 1, "task 1", new Date().getTime());
        final Task task2 = new Task(2, 2, "task 2", new Date().getTime());
        final Task task3 = new Task(3, 3, "task 3", new Date().getTime());
        final Task task4 = new Task(4, 4, "task 4", new Date().getTime());
        final List<Project> projectList = database.daoProject().getAllProjects();
        assertEquals("Projet Tartampion", projectList.get((int) task1.getProjectId()).getName());
        assertEquals("Projet Lucidia", projectList.get((int) task2.getProjectId()).getName());
        assertEquals("Projet Circus", projectList.get((int) task3.getProjectId()));
        assertNull(projectList.get((int) task4.getProjectId()));
    }

    @Test
    public void test_az_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        database.daoTask().insertTask(task1);
        database.daoTask().insertTask(task2);
        database.daoTask().insertTask(task3);
        List<Task> tasks = database.daoTask().orderAZ();

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task2);
    }

    @Test
    public void test_za_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        database.daoTask().insertTask(task1);
        database.daoTask().insertTask(task2);
        database.daoTask().insertTask(task3);
        List<Task> tasks = database.daoTask().orderZA();

        assertSame(tasks.get(0), task2);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_recent_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        database.daoTask().insertTask(task1);
        database.daoTask().insertTask(task2);
        database.daoTask().insertTask(task3);
        List<Task> tasks = database.daoTask().orderByNewToLast();

        assertSame(tasks.get(0), task3);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task1);
    }

    @Test
    public void test_old_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        database.daoTask().insertTask(task1);
        database.daoTask().insertTask(task2);
        database.daoTask().insertTask(task3);
        List<Task> tasks = database.daoTask().orderByLastToNew();

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }
}