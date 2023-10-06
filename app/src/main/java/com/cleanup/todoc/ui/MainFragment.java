package com.cleanup.todoc.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.R;
import com.cleanup.todoc.databinding.FragmentMainBinding;
import com.cleanup.todoc.models.Task;
import com.cleanup.todoc.repositories.ProjectDataRepository;
import com.cleanup.todoc.repositories.TaskDataRepository;

public class MainFragment extends Fragment implements TasksAdapter.DeleteTaskListener {
    private FragmentMainBinding binding;
    private static MainFragment INSTANCE_FRAGMENT;
    private MainFragmentViewModel mViewModel;
    private EditText dialogEditText;
    private Spinner dialogSpinner;
    private AlertDialog dialog;
    private ProjectDataRepository projectDataRepository;
    private TaskDataRepository taskDataRepository;
    private MainFragmentViewModel mainFragmentViewModel;


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
        binding = FragmentMainBinding.inflate(inflater,container,false);
        configureViewModel();
        View view = binding.getRoot();
        dialogSpinner = binding.getRoot().findViewById(R.id.project_spinner);
        return view;

    }

    private void configureViewModel() {
        this.mainFragmentViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(this.getContext())).get(MainFragmentViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainFragmentViewModel.class);

        mViewModel.init();
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
        final AlertDialog dialog = mViewModel.getAddTaskDialog();

        dialog.show();

        //dialogEditText = dialog.findViewById(R.id.txt_task_name);
        //dialogSpinner = dialog.findViewById(R.id.project_spinner);

        mViewModel.populateDialogSpinner();
    }
}