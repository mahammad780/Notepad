package com.example.notepad.executer;

import com.example.notepad.adapters.ListItem;

import java.util.List;

public interface OnDataReceived {
    void onReceived(List<ListItem> list);

}
