package com.cleanup.todoc.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cleanup.todoc.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(MainFragment.getInstanceFragment(), "fragment_main").commitNow();

    }

}