package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.cleanup.todoc.models.Project;


@Dao
public interface ProjectDao {
    @Query("SELECT * FROM projects WHERE id = :projectId")
    LiveData<Project> getProject(long projectId);
}
