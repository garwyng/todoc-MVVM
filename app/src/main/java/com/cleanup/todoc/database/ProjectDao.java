package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.models.Project;

import java.util.List;


@Dao
public interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Project project);
    @Query("SELECT * FROM projects_table")
    LiveData<List<Project>> getAllProjects();
    @Query("SELECT * FROM " + Project.TABLE_NAME + " WHERE " + Project.ID_COLUMN_NAME + "=:projectId")
    LiveData<Project> getProjectById(long projectId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Project> projects);
}

