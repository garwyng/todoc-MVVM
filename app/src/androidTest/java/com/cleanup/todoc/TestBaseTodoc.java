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

import java.util.Calendar;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TestBaseTodoc {
    private long TASK_ID = 0;
    private Task TASK_DEMO = new Task(TASK_ID,1,"test", Calendar.getInstance().getTime().getTime());
    private Project projectTest = new Project(4,"test",2502002);

    private TodocDatabase database;

    @Rule

    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception{
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(), TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
    }
    @After
    public void closeDb() throws Exception{
        database.close();
    }
    @Test
    public void getProjectTest() throws Exception{
        List<Project> projects =  this.database.daoProject().getAllProjects();

        Assert.assertEquals(3,projects.size());

    }
    @Test
    public void insertProjectTest()throws Exception{
        this.database.daoProject().insert(projectTest);
       List<Project> projects = (List<Project>) this.database.daoProject().getAllProjects();
        Log.d("insertProjectTest", "insertProjectTest: "+projects);
       Assert.assertTrue(projects.listIterator().next().getName().contains(projectTest.getName()));
       Assert.assertEquals(1,projects.size());
    }
    @Test
    public void addTaskTest() throws InterruptedException{
        this.database.daoTask().insertTask(TASK_DEMO);

        List<Task> tasks = this.database.daoTask().getAll();
        Assert.assertTrue(tasks.contains(TASK_DEMO));

       // Assert.assertTrue(task.getName().equals(TASK_DEMO.getName()));
    }
}
