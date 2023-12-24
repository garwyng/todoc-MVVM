package com.cleanup.todoc.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.cleanup.todoc.R;
import com.cleanup.todoc.event.SharePrefEvent;
import com.cleanup.todoc.models.Task;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String ORDER = "ORDER";
    private List<Task> tasks=new ArrayList<>();
    private TasksAdapter adapter;
    public static final String PREFS_ORDER="order";
    public SharedPreferences orderPreference;
    private MainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orderPreference= getSharedPreferences(PREFS_ORDER,0);
        fragment = MainFragment.getInstanceFragment();
        getSupportFragmentManager().beginTransaction().add(fragment, "fragment_main").commitNow();

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
            myEditor.apply();
        } else if (id == R.id.filter_alphabetical_inverted) {
            myEditor.putString(ORDER,"za");
            myEditor.apply();
        } else if (id == R.id.filter_oldest_first) {
            myEditor.putString(ORDER,"OlderToFirst");
            myEditor.apply();
        } else if (id == R.id.filter_recent_first) {
            myEditor.putString(ORDER,"FistToOlder");
            myEditor.apply();
        }
        EventBus.getDefault().post(new SharePrefEvent());
        return super.onOptionsItemSelected(item);
    }

}