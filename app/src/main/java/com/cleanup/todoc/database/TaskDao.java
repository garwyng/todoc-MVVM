package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.cleanup.todoc.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insertTask(Task task);
    @Delete
    void deleteTask(Task task);
    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getTasks();
    @Query("SELECT * FROM tasks WHERE id= :id")
    LiveData<Task> getTaskById(long id);

}
