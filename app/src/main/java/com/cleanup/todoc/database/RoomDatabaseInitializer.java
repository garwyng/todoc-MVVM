package com.cleanup.todoc.database;

import android.content.Context;
import android.content.res.Resources;

import com.cleanup.todoc.R;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.ProjectData;
import com.cleanup.todoc.ui.MainActivity;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class RoomDatabaseInitializer {
    public static void initializeDatabase(Context context, TodocDatabase database) {
        try {
            // Open the JSON file from the res/raw directory
            Resources resources = context.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.projects);

            // Parse JSON using Gson
            Gson gson = new Gson();
            InputStreamReader reader = new InputStreamReader(inputStream);
            ProjectData projectData = gson.fromJson(reader, ProjectData.class);

            // Insert data into the Room database using your DAO
            List<Project> projects = projectData.getData();
            if (projects != null && !projects.isEmpty()) {
                database.daoProject().insertAll(projects);
            }

        } catch (Exception e) {
            // Handle exceptions here (e.g., parsing errors)
            e.printStackTrace();
        }
    }
}
