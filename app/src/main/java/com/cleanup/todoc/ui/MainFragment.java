package com.cleanup.todoc.ui;

import static com.cleanup.todoc.ui.MainActivity.PREFS_ORDER;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.databinding.FragmentMainBinding;
import com.cleanup.todoc.event.DeleteTaskEvent;
import com.cleanup.todoc.event.SharePrefEvent;
import com.cleanup.todoc.models.Project;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment {
    private static MainFragment INSTANCE_FRAGMENT;
    private FragmentMainBinding binding;
    private MainFragmentViewModel mViewModel;
    private EditText dialogEditText;
    private Spinner dialogSpinner;
    private AlertDialog dialog;
    private ProjectDataRepository projectDataRepository;
    private TaskDataRepository taskDataRepository;
    private MainFragmentViewModel mainFragmentViewModel;
    private RecyclerView mRecyclerView;
    private List<Task> tasks;
    private TasksAdapter adapter;

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
        mRecyclerView = binding.listTasks;
        return view;

    }

    private void configureViewModel() {
        this.mainFragmentViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this.getContext())).get(MainFragmentViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);
        initRecyclerview();

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

    public void initRecyclerview() {
        tasks = mViewModel.getTasks();
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(new TasksAdapter(tasks, mViewModel));
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

                Button button = dialog[0].findViewById(R.id.ajouter_button);
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

    public void onPositiveButtonClick(DialogInterface dialogInterface) {
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
                dialogEditText.setError((getContext().getString(R.string.empty_task_name)));
            }
            // If both project and name of the task have been set
            else if (taskProject != null) {

                Task task = new Task(
                        taskProject.getId(),
                        taskName,
                        new Date().getTime()
                );
                MainFragmentViewModel.addTask(task);
                tasks = mViewModel.getTasks();
                updateTasks();
                dialogInterface.dismiss();
            }
            // If name has been set, but project has not been set (this should never occur)
            else {
                dialogInterface.dismiss();
            }
        }
        // If dialog is already closed
        else {
            dialogInterface.dismiss();
        }
    }

    /**
     * Updates the list of tasks in the UI
     */
    public void updateTasks() {
        if (tasks.size() == 0) {
            binding.lblNoTask.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            SharedPreferences orderPreferences = getActivity().getSharedPreferences(PREFS_ORDER, 0);
            String order = orderPreferences.getString("ORDER", "none");
            Log.d("order", "updateTasks: " + order);
            binding.lblNoTask.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            switch (order) {
                case "az":
                    tasks = mViewModel.orderAZ();
                    break;
                case "za":
                    tasks = mViewModel.orderZA();
                    break;
                case "FistToOlder":
                    tasks = mViewModel.orderByNewToLast();
                    break;
                case "OlderToFirst":
                    tasks = mViewModel.orderByLastToNew();
                    break;
            }
            mRecyclerView.setAdapter(new TasksAdapter(tasks, mViewModel));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateTasks();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTasks();
    }

    @Subscribe
    public void onSharePref(SharePrefEvent event) {
        Log.d("aaaaa", "onSharePref: ");
        updateTasks();
    }

    @Subscribe
    public void onDeleteTask(DeleteTaskEvent event) {
        MainFragmentViewModel.deleteTask(event.task);
        tasks = mViewModel.getTasks();
        updateTasks();
    }

}