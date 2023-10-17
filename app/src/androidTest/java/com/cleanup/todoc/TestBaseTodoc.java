package com.cleanup.todoc;
import static com.cleanup.todoc.R.raw.projects;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.espresso.internal.inject.InstrumentationContext;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.google.gson.Gson;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
public class TestBaseTodoc {
    private Task TASK_DEMO = new Task(1,"test", Calendar.getInstance().getTime().getTime());
    private Project projectTest = new Project(4,"test",2502002);
    private List<Project> projects = Arrays.asList(Project.getAllProjects());
    private TodocDatabase database;

    @Rule

    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception{
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
        /*for (Project project: projects
        ) {this.database.daoProject().insert(project);
        }*/
    }
    @After
    public void closeDb() throws Exception{
        database.close();
    }
    @Test
    public void getProjectTest() throws Exception{
        List<Project> projectsList =  this.database.daoProject().getAllProjects();

        Assert.assertEquals(projects.size(),projectsList.size());

    }
    @Test
    public void insertProjectTest()throws Exception{
        this.database.daoProject().insert(projectTest);
        List<Project> projectList = this.database.daoProject().getAllProjects();
       Assert.assertEquals(projects.size() +1,projectList.size());
    }
    @Test
    public void addTaskTest() throws InterruptedException{
        List<Task> tasks = this.database.daoTask().getAll();
        Assert.assertEquals(0,tasks.size());
        this.database.daoTask().insertTask(TASK_DEMO);
        tasks = this.database.daoTask().getAll();
        Assert.assertEquals(1,tasks.size());

       // Assert.assertTrue(task.getName().equals(TASK_DEMO.getName()));
    }
}
