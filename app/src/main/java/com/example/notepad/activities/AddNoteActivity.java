package com.example.notepad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notepad.DB.DBManager;
import com.example.notepad.DB.NotePadConstants;
import com.example.notepad.R;
import com.example.notepad.DB.DBHelper;
import com.example.notepad.adapters.ListItem;
import com.example.notepad.executer.AppExecuter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNoteActivity extends AppCompatActivity {

    private EditText  textInputNote;
    private TextInputEditText textInputTitle;
    private TextInputLayout textLayoutTitle;
    private DBManager dbManager;
    ListItem listItem;
    private boolean isEditState=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        textInputTitle = findViewById(R.id.titleInputEditText);
        textInputNote = findViewById(R.id.noteInputEditText);
        textLayoutTitle = findViewById(R.id.titleInputLayout);
        dbManager = new DBManager(this);

        getMyIntent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        dbManager.openDB();
    }

    public void getMyIntent(){
        Intent intent = getIntent();
        if(intent != null){
            listItem = (ListItem) intent.getSerializableExtra(NotePadConstants.NOTE_EDIT_KEY);
           isEditState = intent.getBooleanExtra(NotePadConstants.NOTE_READ_KEY,true);
            if(!isEditState){
                textInputTitle.setText(listItem.getTitle());
                textInputNote.setText(listItem.getNote());
            }
        }

    }

    public void onClickSave(View view) {
        final String title = textInputTitle.getText().toString();
        final String note = textInputNote.getText().toString();

        if (title.trim().equals("")) {
            textLayoutTitle.setError(getString(R.string.title_error));
        } else {
            if(isEditState){
                AppExecuter.getInstance().getSubIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        dbManager.insertToDb(title, note);
                    }
                });
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            }else{
                dbManager.update(title,note, listItem.getId());
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            }
            dbManager.closeDb();
            finish();
        }

    }

}