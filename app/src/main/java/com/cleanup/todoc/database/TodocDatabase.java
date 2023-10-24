package com.cleanup.todoc.database;

import static com.cleanup.todoc.R.raw.projects;
import static com.cleanup.todoc.R.raw.tasks;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Database(entities = { Project.class,Task.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {
    private static volatile TodocDatabase INSTANCE;
    private static String DATABASE_NAME = "TODOC_database";
    private static Executor executor;
    private static Context contextAPP;
    private static ProjectDataRepository projectDataRepository;

    public abstract ProjectDao daoProject();

    public abstract TaskDao daoTask();

    public static synchronized TodocDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            contextAPP= context.getApplicationContext();
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodocDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(prepopulateDatabase())
                    .build();
            Log.d("instance", "getInstance:2 " + INSTANCE);
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback prepopulateDatabase() {

        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db){
                super.onCreate(db);
                Gson gsonProject = new Gson();
                InputStream inputStream = contextAPP.getResources().openRawResource(projects);
                InputStreamReader reader = new InputStreamReader(inputStream);

                try {
                    Project[] projects = gsonProject.fromJson(reader, Project[].class);

                    for (Project project : projects) {
                        Executors.newSingleThreadExecutor().execute(() -> INSTANCE.daoProject().insert(new Project(project.getId(),project.getName(),project.getColor())));
                    }
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Gson gsonTask = new Gson();
                InputStream inputStreamTask = contextAPP.getResources().openRawResource(tasks);
                InputStreamReader readerTask = new InputStreamReader(inputStreamTask);

                try {
                    Task[] tasks = gsonTask.fromJson(readerTask, Task[].class);

                    for (Task task : tasks) {
                        Log.d("valeur_task", "onCreate: "+ task.getName());
                        Executors.newSingleThreadExecutor().execute(() -> INSTANCE.daoTask().insertTask(new Task(task.getProjectId(),task.getName(), Calendar.getInstance().getTime().getTime())));
                    }
                    readerTask.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };

    }
}



