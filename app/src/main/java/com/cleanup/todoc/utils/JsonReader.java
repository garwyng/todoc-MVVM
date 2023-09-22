package com.cleanup.todoc.utils;


import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.models.TaskList;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonReader {
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Task> tasks;
    public List<Task> getTaskListFromFile(){
        try {
            File jsonFile = new File("tasks.json");
            TaskList tasksList = objectMapper.readValue(jsonFile, TaskList.class);
            tasks = tasksList.getTaskslist();

        }catch (IOException e){
            e.printStackTrace();
        }
        return tasks;

    }
}
