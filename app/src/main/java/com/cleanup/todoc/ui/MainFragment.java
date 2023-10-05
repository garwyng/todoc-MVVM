package com.cleanup.todoc.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.cleanup.todoc.R;
import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.concurrent.Executors;

public class MainFragment extends Fragment implements TasksAdapter.DeleteTaskListener {

    private static MainFragment INSTANCE_FRAGMENT;
    private MainFragmentViewModel mViewModel;
    private EditText dialogEditText;
    private Spinner dialogSpinner;
    private AlertDialog dialog;
    private ProjectDataRepository projectDataRepository;
    private TaskDataRepository taskDataRepository;
    private MainFragmentViewModel MainFragmentViewModel;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment getInstanceFragment() {
        if (INSTANCE_FRAGMENT == null) {
            INSTANCE_FRAGMENT = new MainFragment();
        }
        return INSTANCE_FRAGMENT;
    }


    /**
     * Returns the dialog allowing the user to create a new task.
     *
     * @return the dialog allowing the user to create a new task
     */
    @NonNull


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        configureViewModel();
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void configureViewModel() {
        this.MainFragmentViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this.getContext())).get(MainFragmentViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);
        // TODO: Use the ViewModel


        /**
         * Called when the user clicks on the positive button of the Create Task Dialog.
         *
         * @param dialogInterface the current displayed dialog
         */

        /*private void onPositiveButtonClick(DialogInterface dialogInterface) {
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
                    dialogEditText.setError(getString(R.string.empty_task_name));
                }
                // If both project and name of the task have been set
                else if (taskProject != null) {
                    // TODO: Replace this by id of persisted task
                    long id = (long) (Math.random() * 50000);


                    Task task = new Task(
                            id,
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
            // If dialog is aloready closed
            else {
                dialogInterface.dismiss();
            }
        }*/
    }

    @Override
    public void onDeleteTask(Task task) {

    }
}