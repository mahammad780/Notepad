package com.example.notepad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.notepad.DB.DBManager;
import com.example.notepad.DB.NotePadConstants;
import com.example.notepad.R;
import com.example.notepad.DB.DBHelper;
import com.example.notepad.adapters.ListItem;
import com.example.notepad.adapters.NotePadRecyclerViewAdapter;
import com.example.notepad.executer.AppExecuter;
import com.example.notepad.executer.OnDataReceived;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class MainActivity extends AppCompatActivity implements OnDataReceived {

    private RecyclerView recyclerView;
    private NotePadRecyclerViewAdapter adapter;
    private DBManager dbManager;
    public static ImageView mainImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mainImage = findViewById(R.id.mainImage);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new NotePadRecyclerViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getItemTouchHelper().attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        dbManager = new DBManager(this);

        registerForContextMenu(recyclerView);

    }

    @Override
    protected void onResume() {
        super.onResume();

        dbManager.openDB();
        readFromDB("");
    }

    public void onClickAdd(View view){
        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.not_pad_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                AppExecuter.getInstance().getSubIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        dbManager.getFromDb(newText,MainActivity.this);
                    }
                });
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void readFromDB(final String text){
        AppExecuter.getInstance().getSubIO().execute(new Runnable() {
            @Override
            public void run() {
                dbManager.getFromDb(text,MainActivity.this);
            }
        });
    }
    private ItemTouchHelper getItemTouchHelper () {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.removeItemFromRecyclerView(viewHolder.getAdapterPosition(),dbManager);

            }
        });
    }

    @Override
    public void onReceived(List<ListItem> list) {
        AppExecuter.getInstance().getMainIO().execute(new Runnable() {
            @Override
            public void run() {
                adapter.updateAdapter(list);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edit1){
            Intent intent = new Intent(this, AddNoteActivity.class);
            intent.putExtra(NotePadConstants.NOTE_EDIT_KEY, adapter.getElement());
            intent.putExtra(NotePadConstants.NOTE_READ_KEY,false);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.delete2){
            adapter.removeItemFromRecyclerView(adapter.getPosition(),dbManager);
            return true;
        }
        return super.onContextItemSelected(item);
    }

}