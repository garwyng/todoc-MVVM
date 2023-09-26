package com.cleanup.todoc.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.utils.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

@Database(entities = {Task.class, Project.class},version = 1,exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {
    private static volatile TodocDatabase INSTANCE;
   private static final int NUMBER_OF_THREADS = 4;

    public abstract TaskDao daoTask();

    public abstract ProjectDao daoProject();

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TodocDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (TodocDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodocDatabase.class,"todoc_database")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

        private static Callback prepopulateDatabase() {

            return new Callback() {

                @Override

                public void onCreate(@NonNull SupportSQLiteDatabase db) {

                    super.onCreate(db);

                    JsonReader jsonReaderProject = new JsonReader();
                    List<Project> projects = jsonReaderProject.getProjectsListFromFile();
                    Log.d("TASKS", "onCreate: "+ projects);
                    for (Project project: projects
                    ) {
                        INSTANCE.daoProject().insertProject(project);
                    }

                    JsonReader jsonReaderTask = new JsonReader();
                    List<Task> tasks = jsonReaderTask.getTaskListFromFile();
                    Log.d("TASKS", "onCreate: "+ tasks);
                    for (Task task: tasks
                         ) {
                        INSTANCE.daoTask().insertTask(task);
                    }

                }

            };

        }
}

