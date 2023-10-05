package com.cleanup.todoc.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.appcompat.view.menu.MenuWrapperICS;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.cleanup.todoc.R;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {
    private static TodocDatabase INSTANCE;
    private static String DATABASE_NAME = "TODOC_database";

    public abstract ProjectDao daoProject();

    public abstract TaskDao daoTask();

    public static synchronized TodocDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            Log.d("instance", "getInstance: 1"+context);
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodocDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(prepopulateDatabase(context,INSTANCE))
                    .build();
            Log.d("instance", "getInstance:2 " + INSTANCE);
        }
        Log.d("instance", "getInstance:3 ");
        return INSTANCE;
    }

    private static Callback prepopulateDatabase(Context context,TodocDatabase database) {

        return new Callback() {

            @Override

            public void onCreate(@NonNull SupportSQLiteDatabase db) {

                super.onCreate(db);

                boolean alreadyInTransaction = db.inTransaction();
                Gson gson = new Gson();
                InputStream inputStream = context.getResources().openRawResource(R.raw.projects);
                InputStreamReader reader = new InputStreamReader(inputStream);
                try {
                    Project[] projects = gson.fromJson(reader, Project[].class);

                    for (Project project : projects) {
                        INSTANCE.daoProject().insert(new Project(project.getId(),project.getName(),project.getColor()));
                    }
                    if (!alreadyInTransaction) {
                        db.setTransactionSuccessful();
                        db.endTransaction();
                    }

                    reader.close();
                } catch (IOException e) {
                    // Handle any exceptions that may occur during parsing or database insertion
                    e.printStackTrace();
                }


            }
            @Override
            public void onOpen(SupportSQLiteDatabase db) {
                super.onOpen(db);
                /* Here? always checks that correct number of projects exist
                 * Most flexible option but least efficient
                 * i.e. Called whenever DB is opened
                 * doesn't need DB version change to introduce changed projects as
                 * adding new project with new APK will cause new project to be loaded
                 */
                Cursor csr = db.query("SELECT count(*) FROM " + Project.TABLE_NAME);
                if (csr.moveToFirst()) {
                    long row_count = csr.getLong(0);
                    if (row_count != Project.DEFINED_PROJECTS.length) prepopulateDatabase(context,INSTANCE);
                }
                csr.close();
            }

            @Override
            public void onDestructiveMigration(SupportSQLiteDatabase db) {
                super.onDestructiveMigration(db);
                prepopulateDatabase(context,INSTANCE); //<<<<< or Here?
            }

        };

    }


}
    /*public List<Task> readJsonStreamTask(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readTaskArray(reader);
        } finally {
            reader.close();
        }
    }
    public static List<Project> readJsonStreamProject(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readProjectArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Task> readTaskArray(JsonReader reader) throws IOException {
        List<Task> tasks = new ArrayList<Task>();

        reader.beginArray();
        while (reader.hasNext()) {
            tasks.add(readTask(reader));
        }
        reader.endArray();
        return tasks;
    }
    public List<Project> readProjectArray(JsonReader reader) throws IOException {
        List<Project> projects = new ArrayList<Project>();

        reader.beginArray();
        while (reader.hasNext()) {
            projects.add(readProject(reader));
        }
        reader.endArray();
        return projects;
    }

    public Task readTask(JsonReader reader) throws IOException {
        long id = -1;
        long projectId = -1;
        String name = null;
        long creationTimestamp = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String task= reader.nextName();
            if (task.equals("id")) {
                id = reader.nextLong();
            } else if (task.equals("projectId")) {
                projectId = reader.nextLong();
            } else if (task.equals("name")) {
                name = reader.nextString();
            } else if (task.equals("creationTimestamp")) {
                creationTimestamp = reader.nextLong();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Task(id, projectId, name, creationTimestamp);
    }

    public Project readProject(JsonReader reader) throws IOException {
        long id = -1;
        String name = null;
        int color = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String project = reader.nextName();
            if (project.equals("id")) {
                id = reader.nextLong();
            } else if (project.equals("name")) {
                name = reader.nextString();
            } else if (project.equals("color")) {
                color = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Project(id, name,color);
    }*/


