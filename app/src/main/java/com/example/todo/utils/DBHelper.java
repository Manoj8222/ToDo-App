package com.example.todo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.todo.DBToDoModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DB_NAME ="TODO_DB";
    private static final String TABLE_NAME ="TODO_TABLE";
    private static final String COL_1 ="ID";
    private static final String COL_2 ="TASK";
    private static final String COL_3 ="DESCRIPTION";
    private static final String COL_4 ="START_TIME";
    private static final String COL_5 ="START_DATE";
    private static final String COL_6 ="END_TIME";
    private static final String COL_7 ="END_DATE";
    private static final String COL_8 ="PRIORITY";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT , TASK TEXT , DESCRIPTION TEXT , START_TIME TIME , START_DATE DATE, END_TIME TIME , END_DATE DATE,PRIORITY TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void addTask(DBToDoModel model){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,model.getTask());
        values.put(COL_3,model.getDescription());
        values.put(COL_4,model.getStartTime());
        values.put(COL_5,model.getStartDate());
        values.put(COL_6,model.getEndTime());
        values.put(COL_7,model.getEndDate());
        values.put(COL_8,model.getPriority());
        db.insert(TABLE_NAME,null,values);
    }
    public void updateTask(int id , String task,String description, String start_time, String start_date, String end_time, String end_date,String priority){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,task);
        values.put(COL_3,description);
        values.put(COL_4,start_time);
        values.put(COL_5,start_date);
        values.put(COL_6,end_time);
        values.put(COL_7,end_date);
        values.put(COL_8,priority);
        db.update(TABLE_NAME,values,"ID=?",new String[]{String.valueOf(id)});
    }
    public void deleteTask(int id ){
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME,"ID=?",new String[]{String.valueOf(id)});
    }

    public List<DBToDoModel> getAllTasks(){
        db = this.getWritableDatabase();
        Cursor c = null;
        List<DBToDoModel> list = new ArrayList<>();

        db.beginTransaction();;
        try{
            c = db.query(TABLE_NAME,null,null,null,null,null,null);
            if(c!=null){
                if(c.moveToFirst()){
                    do{
                        DBToDoModel model = new DBToDoModel();
                        model.setId(c.getInt(c.getColumnIndex(COL_1)));
                        model.setTask(c.getString(c.getColumnIndex(COL_2)));
                        model.setDescription(c.getString(c.getColumnIndex(COL_3)));
                        model.setStartTime(c.getString(c.getColumnIndex(COL_4)));
                        model.setStartDate(c.getString(c.getColumnIndex(COL_5)));
                        model.setEndTime(c.getString(c.getColumnIndex(COL_6)));
                        model.setEndDate(c.getString(c.getColumnIndex(COL_7)));
                        model.setPriority(c.getString(c.getColumnIndex(COL_8)));
                        list.add(model);
                        Collections.sort(list);
                    }while(c.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            c.close();
        }
        return list;
    }
}
