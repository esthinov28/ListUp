package com.example.randomlocks.listup.Modal;

import android.database.Cursor;

import com.example.randomlocks.listup.Adapter.DatabaseAdapter;

/**
 * Created by randomlocks on 5/4/2016.
 */
public class DatabaseModel  {

    private Long rowId;
    private String title;
    private String checkboxText;
    private   long CurrentDate;
    private  int color;
    private  int todoType;
    private long lastEdited;



    public DatabaseModel(String title, int color,String checkboxText,int todoType) {
        this.title = title;
        this.color=color;
        this.checkboxText = checkboxText;
        this.todoType = todoType;



    }

    public DatabaseModel(String title, int color,String checkboxText,int todoType,Long rowId) {
        this.title = title;
        this.color=color;
        this.checkboxText = checkboxText;
        this.todoType = todoType;
        this.rowId = rowId;


    }

    public static DatabaseModel fromCursor(Cursor cursor) {


        return new DatabaseModel(cursor.getString(DatabaseAdapter.getColumnTitle(cursor)),
                cursor.getInt(DatabaseAdapter.getColumnColor(cursor)),
                cursor.getString(DatabaseAdapter.getColumnCheckedText(cursor)),
                cursor.getInt(DatabaseAdapter.getColumnTodoType(cursor)),
                cursor.getLong(DatabaseAdapter.getColumnRowId(cursor))
                );

    }



    public long getLastEdited() {
        return lastEdited;
    }


    public int getColor() {
        return color;
    }


    public String getTitle() {
        return title;
    }


    public String getCheckboxText() {
        return checkboxText;
    }

    public int getTodoType() {
        return todoType;
    }

    @Override
    public String toString() {
        return title;
    }

    public Long getRowId() {
        return rowId;
    }
}
