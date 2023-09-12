package com.example.notepad.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.DB.DBManager;
import com.example.notepad.DB.NotePadConstants;
import com.example.notepad.R;
import com.example.notepad.activities.AddNoteActivity;
import com.example.notepad.activities.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NotePadRecyclerViewAdapter extends RecyclerView.Adapter<NotePadRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<ListItem> list;
    private int adapterPosition;

    public NotePadRecyclerViewAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;
        private TextView title;
        private List<ListItem> list;

        public MyViewHolder(@NonNull View itemView, Context context, List<ListItem> list) {
            super(itemView);
            this.context = context;
            this.list = list;
            title = itemView.findViewById(R.id.Title);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    adapterPosition = getAdapterPosition();
                    return false;
                }
            });

        }

        public void setData(String inputTitle) {
            title.setText(inputTitle);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, AddNoteActivity.class);
            intent.putExtra(NotePadConstants.NOTE_EDIT_KEY, list.get(getAdapterPosition()));
            intent.putExtra(NotePadConstants.NOTE_READ_KEY, false);
            context.startActivity(intent);

        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notepad_item, parent, false);
        return new MyViewHolder(view, context, list);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(list.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateAdapter(List<ListItem> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
        imageAppearance();

    }

    public void removeItemFromRecyclerView(int position, DBManager dbManager) {
        dbManager.delete(list.get(position).getId());
        list.remove(position);
        notifyItemRangeChanged(0, list.size());
        notifyItemRemoved(position);
    }

    public int getPosition() {
        return adapterPosition;
    }

    public ListItem getElement() {
        return list.get(adapterPosition);
    }

    public void imageAppearance(){
        if(getItemCount() == 0){
            MainActivity.mainImage.setVisibility(View.VISIBLE);
        }else {
            MainActivity.mainImage.setVisibility(View.GONE);
        }
    }
}
