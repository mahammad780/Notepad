package com.example.notepad.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.notepad.adapters.ListItem;
import com.example.notepad.executer.OnDataReceived;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    public void openDB(){
        db = dbHelper.getWritableDatabase();
    }
    public void insertToDb(String title,String note){
        ContentValues cv = new ContentValues();
        cv.put(NotePadConstants.TITLE, title);
        cv.put(NotePadConstants.NOTE, note);
        db.insert(NotePadConstants.TABLE_NAME,null,cv);
    }

    public void update(String title,String note,int id){
        String selection = NotePadConstants.ID + "=" + id;
        ContentValues cv = new ContentValues();
        cv.put(NotePadConstants.TITLE, title);
        cv.put(NotePadConstants.NOTE, note);
        db.update(NotePadConstants.TABLE_NAME,cv,selection,null);
    }

    public void delete(int id){
        String selection = NotePadConstants.ID + "=" + id;
        db.delete(NotePadConstants.TABLE_NAME,selection,null);
    }

    public void getFromDb(String searchByTitle, OnDataReceived onDataReceived){
        List<ListItem> tempList = new ArrayList<>();
        String selection = NotePadConstants.TITLE + " like ?";
        Cursor cursor = db.query(NotePadConstants.TABLE_NAME, null, selection,
                new String[]{"%" + searchByTitle + "%"},null,null,null);

        try {

            while (cursor.moveToNext()) {
                ListItem listItem = new ListItem();
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NotePadConstants.TITLE));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(NotePadConstants.NOTE));
                int _id = cursor.getInt(cursor.getColumnIndexOrThrow(NotePadConstants.ID));
                listItem.setTitle(title);
                listItem.setNote(note);
                listItem.setId(_id);
                tempList.add(listItem);
            }
            cursor.close();
            onDataReceived.onReceived(tempList);
        }catch(SQLException e){

            cursor.close();
            onDataReceived.onReceived(tempList);
        }
    }

    public void closeDb(){
        dbHelper.close();
    }
}
