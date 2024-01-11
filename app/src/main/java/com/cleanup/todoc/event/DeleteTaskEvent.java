package com.cleanup.todoc.event;

import com.cleanup.todoc.models.Task;

public class DeleteTaskEvent {
    public Task task;

    public DeleteTaskEvent(Task task) {
        this.task = task;
    }
}
