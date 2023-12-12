package com.cleanup.todoc.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.cleanup.todoc.R;
import com.cleanup.todoc.models.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String ORDER = "ORDER";
    private List<Task> tasks;
    private TasksAdapter adapter;
    public static final String PREFS_ORDER="order";
    public SharedPreferences orderPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orderPreference= getSharedPreferences(PREFS_ORDER,0);
        getSupportFragmentManager().beginTransaction().add(MainFragment.getInstanceFragment(), "fragment_main").commitNow();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        SharedPreferences.Editor myEditor = orderPreference.edit();
        if (id == R.id.filter_alphabetical) {
            myEditor.putString(ORDER,"az");
        } else if (id == R.id.filter_alphabetical_inverted) {
            myEditor.putString(ORDER,"za");
        } else if (id == R.id.filter_oldest_first) {
            myEditor.putString(ORDER,"OlderToFirst");
        } else if (id == R.id.filter_recent_first) {
            myEditor.putString(ORDER,"FistToOlder");
        }
        myEditor.apply();
        adapter.updateTasks(tasks);

        return super.onOptionsItemSelected(item);
    }
}