package com.example.todo.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.todo.Adapter.ToDoAdapter;
import com.example.todo.Model.ToDoModel;
import com.example.todo.R;
import com.example.todo.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

private RecyclerView todoRecycleView;
private ToDoAdapter toDoAdapter;
private List<ToDoModel> toDoList;
private DatabaseHandler db;
private FloatingActionButton fab;
private ItemTouchHelper itemTouchHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHandler(this);
        db.openDatabase();
        getSupportActionBar().hide();
        callingViews();
        recycleViewAction();
        toDoList = db.getAllTodo();
        Collections.reverse(toDoList);
        toDoAdapter.setToDo(toDoList);
        fabAction();
        itemTouchAction();
    }

    void callingViews(){
        todoRecycleView = findViewById(R.id.todoRecycleView);
        fab = findViewById(R.id.fab);
        toDoList = new ArrayList<>();
    }

    void recycleViewAction(){
        todoRecycleView.setLayoutManager(new LinearLayoutManager(this));
        toDoAdapter = new ToDoAdapter(this,db);
        todoRecycleView.setAdapter(toDoAdapter);
    }

    void fabAction(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewToDo.newInstance().show(getSupportFragmentManager(), AddNewToDo.TAG);
            }
        });
    }

    void itemTouchAction(){
        itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(toDoAdapter));
        itemTouchHelper.attachToRecyclerView(todoRecycleView);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        toDoList = db.getAllTodo();
        Collections.reverse(toDoList);
        toDoAdapter.setToDo(toDoList);
        toDoAdapter.notifyDataSetChanged();
    }
}