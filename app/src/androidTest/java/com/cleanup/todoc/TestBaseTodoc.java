package com.cleanup.todoc;

import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TestBaseTodoc {
    private final long TASK_ID = 0;
    private final Task TASK_DEMO = new Task(TASK_ID, 1, "test", Calendar.getInstance().getTime().getTime());
    private final Project projectTest = new Project(4, "test", 2502002);
    @Rule

    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private TodocDatabase database;

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
        List<Project> projectList = new ArrayList<>();
        projectList.add(new Project(1L, "Projet Tartampion", -1385775));
        projectList.add(new Project(2L, "Projet Lucidia", -4928070));
        projectList.add(new Project(3L, "Projet Circus", -6041902));
        database.daoProject().insertAll(projectList);
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void getProjectTest() throws Exception {
        List<Project> projects = this.database.daoProject().getAllProjects();

        Assert.assertEquals(3, projects.size());

    }

    @Test
    public void insertProjectTest() throws Exception {
        this.database.daoProject().insert(projectTest);
        List<Project> projects = this.database.daoProject().getAllProjects();
        Log.d("insertProjectTest", "insertProjectTest: " + projects);
        Assert.assertEquals(projects.get(3).getName(), projectTest.getName());
        Assert.assertEquals(4, projects.size());
    }

    @Test
    public void addTaskTest() throws InterruptedException {
        List<Task> tasks = this.database.daoTask().getAll();
        Assert.assertEquals(0, tasks.size());

        this.database.daoTask().insertTask(TASK_DEMO);
        tasks = this.database.daoTask().getAll();
        Assert.assertEquals(1, tasks.size());
        Assert.assertEquals(tasks.get(0).getName(), TASK_DEMO.getName());
    }

    @Test
    public void removeTaskTest() {
        List<Task> tasks = this.database.daoTask().getAll();
        Assert.assertEquals(0, tasks.size());

        this.database.daoTask().insertTask(TASK_DEMO);
        tasks = this.database.daoTask().getAll();
        Assert.assertEquals(1, tasks.size());

        this.database.daoTask().deleteTask(tasks.get(0));
        tasks = this.database.daoTask().getAll();
        Assert.assertEquals(0, tasks.size());
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

        Assert.assertEquals(tasks.get(0).getName(), task1.getName());
        Assert.assertEquals(tasks.get(1).getName(), task3.getName());
        Assert.assertEquals(tasks.get(2).getName(), task2.getName());
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

        Assert.assertEquals(tasks.get(0).getName(), task2.getName());
        Assert.assertEquals(tasks.get(1).getName(), task3.getName());
        Assert.assertEquals(tasks.get(2).getName(), task1.getName());
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

        Assert.assertEquals(tasks.get(0).getName(), task3.getName());
        Assert.assertEquals(tasks.get(1).getName(), task2.getName());
        Assert.assertEquals(tasks.get(2).getName(), task1.getName());
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

        Assert.assertEquals(tasks.get(0).getName(), task1.getName());
        Assert.assertEquals(tasks.get(1).getName(), task2.getName());
        Assert.assertEquals(tasks.get(2).getName(), task3.getName());
    }
}
