package com.example.todo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.UI.AddNewToDo;
import com.example.todo.UI.MainActivity;
import com.example.todo.Model.ToDoModel;
import com.example.todo.R;
import com.example.todo.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> toDoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(MainActivity activity, DatabaseHandler db){
        this.activity = activity;
        this.db = db;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_layout,parent,false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        ToDoModel item = toDoList.get(position);
        holder.todo.setText(item.getTodo());
        holder.todo.setChecked(toBoolean(item.getStatus()));
        holder.todo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    db.updateStatus(item.getId(), 1);
                }else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    private boolean toBoolean(int n){
        return n!=0;
    }

    public void setToDo(List<ToDoModel> toDoList){
        this.toDoList = toDoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        ToDoModel item = toDoList.get(position);
        db.deleteToDo(item.getId());
        toDoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position){
        ToDoModel item = toDoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("todo",item.getTodo());
        AddNewToDo fragment = new AddNewToDo();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewToDo.TAG);
    }

    public Context getContext() {
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox todo;
        ViewHolder(View view){
            super(view);
            todo = view.findViewById(R.id.todoCheckox);
        }
    }
}
