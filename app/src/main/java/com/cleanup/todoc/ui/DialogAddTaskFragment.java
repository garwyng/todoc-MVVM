package com.cleanup.todoc.ui;

import androidx.lifecycle.ViewModelProvider;

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
import com.cleanup.todoc.databinding.DialogAddTaskBinding;

public class DialogAddTaskFragment extends Fragment {

    private DialogViewModel mViewModel;


    public static DialogAddTaskFragment newInstance() {
        return new DialogAddTaskFragment();
    }
    private DialogAddTaskBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_add_task, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Spinner spinner = binding.projectSpinner;
        EditText text =binding.txtTaskName;
        
        mViewModel = new ViewModelProvider(this).get(DialogViewModel.class);
        // TODO: Use the ViewModel
    }

}