package com.example.randomlocks.listup.AsyncTask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.randomlocks.listup.Adapter.DatabaseAdapter;


public class QueryAllNotes extends AsyncTask<Integer,Void,Cursor> {


    public interface QueryAllNodeInterface {

        public void processFinish(Cursor cursor);

    }


    Context context;
    DatabaseAdapter adapter;
    Cursor cursor = null;
    QueryAllNodeInterface myInterface = null;

    public QueryAllNotes(Context context, QueryAllNodeInterface myInterface) {
        this.context = context;
        this.myInterface = myInterface;

    }


    @Override
    protected Cursor doInBackground(Integer... params) {

        DatabaseAdapter adapter = new DatabaseAdapter(context);


            cursor = adapter.getAllNote(params[0]);
            cursor.getCount();


            return cursor;


    }


    @Override
    protected void onPostExecute(Cursor cursor) {

        myInterface.processFinish(cursor);


    }
}