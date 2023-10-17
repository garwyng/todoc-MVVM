package com.cleanup.todoc.ui;

import static com.cleanup.todoc.ui.MainFragmentViewModel.addTask;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.databinding.FragmentMainBinding;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import java.util.Date;
import java.util.List;

public class MainFragment extends androidx.fragment.app.Fragment implements TasksAdapter.DeleteTaskListener {
    private FragmentMainBinding binding;
    private static MainFragment INSTANCE_FRAGMENT;
    private MainFragmentViewModel mViewModel;
    private EditText dialogEditText;
    private Spinner dialogSpinner;
    private ProjectDataRepository projectDataRepository;
    private TaskDataRepository taskDataRepository;
    private MainFragmentViewModel mainFragmentViewModel;
    private RecyclerView mRecyclerView;


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
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        dialogSpinner = binding.getRoot().findViewById(R.id.project_spinner);
        Context context = view.getContext();

        return view;

    }
    private void configureViewModel() {
        this.mainFragmentViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this.getContext())).get(MainFragmentViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);
        List<Project> projectList = mViewModel.getAllProject();

        if (binding != null && binding.fabAddTask != null) {
            // Access fabAddTask here
            binding.fabAddTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddTaskDialog();

                }
            });
        }
    }

    @Override
    public void onDeleteTask(Task task) {
        mViewModel.deleteTask(task);
    }
    public void showAddTaskDialog() {
        final AlertDialog dialog = getAddTaskDialog();

        dialog.show();

        dialogEditText = dialog.findViewById(R.id.txt_task_name);
        dialogSpinner = dialog.findViewById(R.id.project_spinner);

        populateDialogSpinner();
    }

    /**
     * Sets the data of the Spinner with projects to associate to a new task
     */
    public void populateDialogSpinner() {
        List<Project> projects = mViewModel.getAllProject();
        if (projects != null && !projects.isEmpty()) {
            ArrayAdapter<Project> adapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, mViewModel.getAllProject());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            if (dialogSpinner != null) {
                dialogSpinner.setAdapter(adapter);
            } else {
                Log.e("SpinnerError", "dialogSpinner is null.");
            }
        } else {
            Log.e("SpinnerError", "No projects found.");
        }
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
                dialogEditText.setError((this.getContext().getString(R.string.empty_task_name)));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {
                Task task = new Task(
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