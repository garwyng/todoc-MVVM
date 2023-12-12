package com.cleanup.todoc.event;

import android.view.MenuItem;

public class MenuEvent {
    public MenuItem menuSelected;

    public MenuEvent(MenuItem menuSelected) {
        this.menuSelected = menuSelected;
    }
}
