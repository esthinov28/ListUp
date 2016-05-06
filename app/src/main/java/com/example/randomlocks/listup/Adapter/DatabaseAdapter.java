package com.example.randomlocks.listup.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.randomlocks.listup.Helper.Constants;
import com.example.randomlocks.listup.Modal.DatabaseModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by randomlocks on 5/4/2016.
 */
public class DatabaseAdapter {

    Context context;
    DatabaseScehma schema;


    //Constructor
    public DatabaseAdapter(Context context) {
        this.context=context;
        schema = DatabaseScehma.getInstance(context);

    }

    /*********  ALL OPERATION WILL BE PERFORMEd ON NON UI THREAD ****************************/

    public long insertNote(DatabaseModel modal)
    {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseScehma.TITLE,modal.getTitle());
        cv.put(DatabaseScehma.CHECKBOX_TEXT, modal.getCheckboxText());
        cv.put(DatabaseScehma.COLOR,modal.getColor());
        cv.put(DatabaseScehma.CURRENT_DATE,System.currentTimeMillis());
        cv.put(DatabaseScehma.TODO_TYPE,modal.getTodoType());
        cv.put(DatabaseScehma.LAST_EDITED,0);

        SQLiteDatabase db=schema.getWritableDatabase();
        return db.insert(DatabaseScehma.TABLE_NAME, null, cv);

    }

    public int deleteNote(Long id)               //Deleting the notes
    {

        SQLiteDatabase db = schema.getWritableDatabase();

        return db.delete(DatabaseScehma.TABLE_NAME, DatabaseScehma.ID + "=" + id, null);
    }



    //Function to fetch each notes with and without sorting
    public Cursor getAllNote(int sortBy)
    {
        SQLiteDatabase db = schema.getWritableDatabase();

        String sortByColumn;
        String columns[] = {
                DatabaseScehma.ID,
                DatabaseScehma.TITLE,
                DatabaseScehma.CHECKBOX_TEXT,
                DatabaseScehma.CURRENT_DATE,
                DatabaseScehma.TODO_TYPE,
                DatabaseScehma.LAST_EDITED,
                DatabaseScehma.COLOR};

        if(sortBy== Constants.SORT_BY_TITLE)
            sortByColumn = DatabaseScehma.TITLE+ " COLLATE NOCASE";
        else if(sortBy==Constants.SORT_BY_DATE)
            sortByColumn = DatabaseScehma.CURRENT_DATE+ " DESC ";
        else
            sortByColumn = DatabaseScehma.LAST_EDITED+ " DESC ";


        Cursor cursor = db.query(DatabaseScehma.TABLE_NAME, columns, null, null, null, null,sortByColumn);

        if(cursor!=null)
            cursor.moveToFirst();

        return cursor;

    }


    //Function to fetch a given note
    public Cursor getGivenNote(Long rowId)
    {
        SQLiteDatabase db = schema.getWritableDatabase();
        String columns[] = {
                DatabaseScehma.ID,
                DatabaseScehma.TITLE,
                DatabaseScehma.CHECKBOX_TEXT,
                DatabaseScehma.CURRENT_DATE,
                DatabaseScehma.TODO_TYPE,
                DatabaseScehma.LAST_EDITED,
                DatabaseScehma.COLOR
        };
        Cursor cursor= db.query(DatabaseScehma.TABLE_NAME,columns,DatabaseScehma.ID+"="+rowId,null,null,null,null);

        if(cursor!=null)
            cursor.moveToFirst();

        return cursor;

    }

    //Used for searching from a list

    public Cursor getAllNode(String sequence){

        SQLiteDatabase db = schema.getWritableDatabase();
        String columns[] = {
                DatabaseScehma.ID,DatabaseScehma.TITLE,DatabaseScehma.CHECKBOX_TEXT,DatabaseScehma.CURRENT_DATE,DatabaseScehma.TODO_TYPE,DatabaseScehma.LAST_EDITED,DatabaseScehma.COLOR};

        String where = DatabaseScehma.TITLE+" LIKE ? OR "+DatabaseScehma.CHECKBOX_TEXT +" LIKE ?";

        Cursor cursor = db.query(false,DatabaseScehma.TABLE_NAME,columns,where,new String[]{"%"+sequence+"%","%"+sequence+"%"},null,null,null,null);

        if(cursor!=null)
            cursor.moveToFirst();

        return cursor;
    }


    //function to update a note

    public boolean updateNote(Long rowId,DatabaseModel modal)
    {
        SQLiteDatabase db = schema.getWritableDatabase();
        String columns[] = {
                DatabaseScehma.ID,DatabaseScehma.TITLE,DatabaseScehma.CHECKBOX_TEXT,DatabaseScehma.LAST_EDITED,DatabaseScehma.COLOR};
        ContentValues cv = new ContentValues();
        cv.put(DatabaseScehma.TITLE,modal.getTitle());
        cv.put(DatabaseScehma.CHECKBOX_TEXT, modal.getCheckboxText());
        cv.put(DatabaseScehma.LAST_EDITED,System.currentTimeMillis());
        cv.put(DatabaseScehma.COLOR, modal.getColor());
        cv.put(DatabaseScehma.TODO_TYPE,modal.getTodoType());
        return db.update(DatabaseScehma.TABLE_NAME,cv,DatabaseScehma.ID+"="+rowId,null )>0;


    }

   public static int  getColumnColor(Cursor cursor) {

       return  cursor.getColumnIndexOrThrow(DatabaseScehma.COLOR);
   }

    public static int  getColumnTitle(Cursor cursor) {

        return  cursor.getColumnIndexOrThrow(DatabaseScehma.TITLE);
    }

    public static int  getColumnCheckedText(Cursor cursor) {

        return  cursor.getColumnIndexOrThrow(DatabaseScehma.CHECKBOX_TEXT);
    }

    public static int  getColumnTodoType(Cursor cursor) {

        return  cursor.getColumnIndexOrThrow(DatabaseScehma.TODO_TYPE);
    }

    public static int  getColumnRowId(Cursor cursor) {

        return  cursor.getColumnIndexOrThrow(DatabaseScehma.ID);
    }






    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "MM-dd HH:mm", Locale.getDefault());

        Date date = new Date();
        return dateFormat.format(date);
    }





    static class DatabaseScehma extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "Abdullah";
        private static final int DATABASE_VERSION = 12;
        private static final String TABLE_NAME = "TodoList";
        private static final String ID = "id";
        private static final String TITLE = "title";
        private static final String CURRENT_DATE = "currentdate";
        public static final String LAST_EDITED = "editeddate";
        private static final String COLOR = "color";
        public static final String TODO_TYPE = "todoType";
        private static final String CHECKBOX_TEXT = "checked_text";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( " + ID + " integer primary key autoincrement , " + TITLE + " varchar(50)   , " + CHECKBOX_TEXT + " varchar(255) , " + CURRENT_DATE + " int , " +TODO_TYPE + " int , " + LAST_EDITED + " int , " + COLOR + " int );";
        private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private static DatabaseScehma sInstance = null;


        public static synchronized DatabaseScehma getInstance(Context context) {
            // Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
            if (sInstance == null) {
                sInstance = new DatabaseScehma(context.getApplicationContext());
            }
            return sInstance;
        }


        private DatabaseScehma(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {


            try {
                db.execSQL(CREATE_TABLE);


            } catch (SQLException e) {
                Log.e("error", "Cannot create table");
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try {
                db.execSQL(DELETE_TABLE);

            } catch (SQLException e) {
                Log.e("error","cannot upgrade table");
            }
            onCreate(db);

        }
    }

}
