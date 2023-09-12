package com.example.notepad.adapters;

import java.io.Serializable;

public class ListItem implements Serializable {
    private String title;
    private String note;
    private int id=0;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }


    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
