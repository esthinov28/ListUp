package com.example.randomlocks.listup.AsyncTask;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.randomlocks.listup.Adapter.DatabaseAdapter;
import com.example.randomlocks.listup.Modal.DatabaseModel;



public class QueryAddUpdateNote extends AsyncTask<Long, Void, Void> {

    public interface QueryAllNodeInterface {

        public void processFinish();

    }


    Context context;
    Long RowId;
    DatabaseModel modal;

    Cursor cursor = null;
    QueryAllNodeInterface myInterface = null;

    public QueryAddUpdateNote(Context context, DatabaseModel modal, QueryAllNodeInterface myInterface) {
        this.context = context;
        this.modal = modal;
        this.myInterface = myInterface;
    }


    @Override
    protected Void doInBackground(Long... params) {

        DatabaseAdapter adapter = new DatabaseAdapter(context);

        RowId = params[0];

        if (RowId == null) {

            adapter.insertNote(modal);  //INSERTING NOTES

        }

        else {

            adapter.updateNote(RowId,modal); //UPDATING NOTES
        }


        return null;
    }


    @Override
    protected void onPostExecute(Void cursor) {

        myInterface.processFinish();


    }




}
