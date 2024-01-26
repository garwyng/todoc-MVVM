package com.cleanup.todoc.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM task_table")
    List<Task> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * From task_table WHERE id= :taskId")
    Task getTaskById(Long taskId);

    @Query("SELECT * FROM task_table ORDER BY name ASC")
    List<Task> orderAZ();

    @Query("SELECT * FROM task_table ORDER BY name DESC")
    List<Task> orderZA();

    @Query("SELECT * FROM task_table ORDER BY creationTimestamp DESC")
    List<Task> orderByNewToLast();

    @Query("SELECT * FROM task_table ORDER BY creationTimestamp ASC")
    List<Task> orderByLastToNew();
    @Delete
    void deleteAllTasks(List<Task> tasks);


}
