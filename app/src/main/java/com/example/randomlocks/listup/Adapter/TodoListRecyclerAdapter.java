package com.example.randomlocks.listup.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.randomlocks.listup.Helper.Constants;
import com.example.randomlocks.listup.Helper.CursorRecyclerViewAdapter;
import com.example.randomlocks.listup.Modal.DatabaseModel;
import com.example.randomlocks.listup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by randomlocks on 5/5/2016.
 */
public class TodoListRecyclerAdapter extends CursorRecyclerViewAdapter<TodoListRecyclerAdapter.MyViewHolder> {

    Context context;
    private InterfaceListener mInterfaceListener=null;

    public TodoListRecyclerAdapter(Context context, Cursor cursor,InterfaceListener mInterfaceListener) {
        super(context, cursor);
        this.context = context;
        this.mInterfaceListener = mInterfaceListener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_todo_list,parent,false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, final Cursor cursor) {

        final DatabaseModel model = DatabaseModel.fromCursor(cursor);
        viewHolder.title.setText(model.getTitle());
        ((CardView)viewHolder.itemView).setCardBackgroundColor(ContextCompat.getColor(context,model.getColor()));


        String str = "";
        try {

            JSONObject jsonObject = new JSONObject(model.getCheckboxText());
            JSONArray checkitemArray = jsonObject.optJSONArray(Constants.CHECKED_STRING_KEY);

            for (int i = 0; i <checkitemArray.length() ; i++) {

                str+="\u2022 "+checkitemArray.optString(i)+"\n";

            }
            viewHolder.description.setText(str);

        } catch (JSONException e) {
            Log.e("error","json error");
        }
        viewHolder.description.setText(str);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mInterfaceListener!=null){
                    mInterfaceListener.onItemClick(model, model.getRowId());
                }
            }
        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,description;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }



    public interface InterfaceListener{
        public void onItemClick(DatabaseModel model,Long rowId);
    }
}
