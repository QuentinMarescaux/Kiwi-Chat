package com.example.chat2i.Models;

import android.support.annotation.NonNull;

public class Conversation {

    private int id;
    private String theme;
    private Boolean active;

    public Conversation(int id, String theme, Boolean active) {
        this.id = id;
        this.theme = theme;
        this.active = active;
    }

    @NonNull
    public String toString() {
        return theme;
    }

    public int getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public Boolean getActive() {
        return active;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
