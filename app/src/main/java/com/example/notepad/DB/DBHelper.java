package com.example.notepad.DB;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.notepad.DB.NotePadConstants.*;
import com.example.notepad.activities.MainActivity;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, NotePadConstants.DB_NAME, null, NotePadConstants.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + NotePadConstants.TABLE_NAME + " (" + NotePadConstants.ID
        + " INTEGER PRIMARY KEY," + NotePadConstants.TITLE + " TEXT," + NotePadConstants.NOTE + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + NotePadConstants.TABLE_NAME);
        onCreate(db);

    }

}
