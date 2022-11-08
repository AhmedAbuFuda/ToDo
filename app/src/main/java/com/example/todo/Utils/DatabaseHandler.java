package com.example.todo.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int version = 1;
    private static final String name = "ToDoListDatabase";
    private static final String ToDo_Table = "ToDo";
    private static final String Id = "Id";
    private static final String ToDo = "ToDo";
    private static final String Status = "Status";
    private static final String  CREATE_TODO_TABLE = "CREATE TABLE " + ToDo_Table + "(" + Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ToDo + " TEXT, "
            + Status + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db , int oldVersion , int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ ToDo_Table);
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertToDo(ToDoModel todo){
        ContentValues values = new ContentValues();
        values.put(ToDo,todo.getTodo());
        values.put(Status,0);
        db.insert(ToDo_Table, null, values);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTodo(){
        List<ToDoModel> todoList= new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try {
            cursor = db.query(ToDo_Table, null, null, null, null, null, null, null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        ToDoModel todo = new ToDoModel();
                        todo.setId(cursor.getInt(cursor.getColumnIndex(Id)));
                        todo.setTodo(cursor.getString(cursor.getColumnIndex(ToDo)));
                        todo.setStatus(cursor.getInt(cursor.getColumnIndex(Status)));
                        todoList.add(todo);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
        db.endTransaction();
        cursor.close();
        }
        return todoList;
    }

    public void updateStatus( int id, int status){
        ContentValues values = new ContentValues();
        values.put(Status, status);
        db.update(ToDo_Table, values, Id +"=?", new String[]{String.valueOf(id)});
    }

    public void updateToD( int id , String toDo){
        ContentValues values = new ContentValues();
        values.put(ToDo, toDo);
        db.update(ToDo_Table, values, Id +"=?", new String[]{String.valueOf(id)});
    }

    public void deleteToDo(int id){
        db.delete(ToDo_Table, Id + "=?", new String[]{String.valueOf(id)});
    }

}
