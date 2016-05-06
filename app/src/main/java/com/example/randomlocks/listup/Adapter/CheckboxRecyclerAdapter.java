package com.example.randomlocks.listup.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.randomlocks.listup.R;

import java.util.List;

/**
 * Created by randomlocks on 5/5/2016.
 */
public class CheckboxRecyclerAdapter extends RecyclerView.Adapter<CheckboxRecyclerAdapter.MyViewHolder> {

    List<String> checkList;
    List<Boolean> isCheckedList;
    Context context;

    public CheckboxRecyclerAdapter(List<String> checkList,List<Boolean> isCheckedList ,Context context){

        this.checkList = checkList;
        this.isCheckedList = isCheckedList;
        this.context = context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_checkbox_layout,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String str = checkList.get(position);

        holder.checkBox.setText(checkList.get(position));
        holder.checkBox.setChecked(isCheckedList.get(position));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isCheckedList.set(position,true);
                    buttonView.setPaintFlags(buttonView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }else {
                    isCheckedList.set(position,false);
                    buttonView.setPaintFlags(0);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }

    public  class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);

            checkBox = (CheckBox) itemView.findViewById(R.id.checked_item);

        }
    }


}
