package com.cleanup.todoc;

import androidx.room.TypeConverter;

import com.cleanup.todoc.models.Project;

public class Converters {
    @TypeConverter
    public static Project projectFromIdProject(Long id) {
        return id ==null ? null : Project.getProjectById(id);
    }
    @TypeConverter
    public static long idFromProject(Project project) {
        return project ==null ? null : project.getId();
    }
}
