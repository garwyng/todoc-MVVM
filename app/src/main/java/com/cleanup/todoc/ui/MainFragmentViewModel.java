package com.cleanup.todoc.ui;

import static com.cleanup.todoc.R.raw.projects;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Ignore;

import com.cleanup.todoc.R;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainFragmentViewModel extends ViewModel {


    public static LiveData<List<Project>> allProjects;
    // TODO: Implement the ViewModel
    private static TaskDataRepository taskDataRepository;
    private final ProjectDataRepository projectDataRepository;
    private static Executor executor;

    //DATA
    @Nullable
    private LiveData<Task> currentTask;
    private Context context = MainFragment.getInstanceFragment().getContext();
    private TodocDatabase db = TodocDatabase.getInstance(context);
    private EditText dialogEditText;
    private Spinner dialogSpinner;

    public MainFragmentViewModel(ProjectDataRepository projectDataRepository, TaskDataRepository taskDataRepository, Executor executor) {
        this.taskDataRepository = taskDataRepository;
        this.projectDataRepository = projectDataRepository;
        this.executor =executor;
    }

    public void init(){

    }

    public static void  addTask(Task task){
        executor.execute(()->{
            taskDataRepository.addTask(task);
        });
    }
    public void  deleteTask(Task task){
        executor.execute(()-> taskDataRepository.deleteTask(task));
    }
    public void updateTask(Task task){
        executor.execute(()-> taskDataRepository.updateTask(task));
    }
    public LiveData<List<Project>> getAllProject(){
        executor.execute(()->{allProjects= projectDataRepository.getAllProject();}
        );
        return allProjects;
    }
    public List<Task> getTasks() {
        List<Task> task = taskDataRepository.getTasks();
        return task;
    }
    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull
    public AlertDialog getAddTaskDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainFragment.getInstanceFragment().getContext(), R.style.Dialog);

        alertBuilder.setTitle(R.string.add_task);
        alertBuilder.setView(R.layout.dialog_add_task);
        alertBuilder.setPositiveButton(R.string.add, null);
        final AlertDialog[] dialog = new AlertDialog[1];
        alertBuilder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                dialogEditText = null;
                dialogSpinner = null;
                dialog[0] = null;
            }
        });

        dialog[0] = alertBuilder.create();

        // This instead of listener to positive button in order to avoid automatic dismiss
        dialog[0].setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialog[0].getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        onPositiveButtonClick(dialog[0]);
                    }
                });
            }
        });

        return dialog[0];
    }
    public void  onPositiveButtonClick(DialogInterface dialogInterface) {
        // If dialog is open
        if (dialogEditText != null && dialogSpinner != null) {
            // Get the name of the task
            String taskName = dialogEditText.getText().toString();

            // Get the selected project to be associated to the task
            Project taskProject = null;
            if (dialogSpinner.getSelectedItem() instanceof Project) {
                taskProject = (Project) dialogSpinner.getSelectedItem();
            }

            // If a name has not been set
            if (taskName.trim().isEmpty()) {
                dialogEditText.setError((context.getApplicationContext().getString(R.string.empty_task_name)));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                long id = MainFragmentViewModel.taskDataRepository.getTasks().size() + 1;
                Task task = new Task(id,
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );
                addTask(task);

                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else{
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }



}