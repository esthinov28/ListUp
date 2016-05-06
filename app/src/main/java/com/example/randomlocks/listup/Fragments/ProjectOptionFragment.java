package com.example.randomlocks.listup.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.randomlocks.listup.Helper.Constants;
import com.example.randomlocks.listup.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by randomlocks on 5/6/2016.
 */
public class ProjectOptionFragment extends DialogFragment {

    public interface InterfaceListener{
        void onComplete(int which,String str);
    }

    InterfaceListener mInterfaceListener;
    int checked_position;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mInterfaceListener = (InterfaceListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException(activity.toString()+"must implement the dialog");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         checked_position = getArguments().getInt(Constants.CHECKED_KEY, 0);

    }


    public static  ProjectOptionFragment newInstance(int checked_position) {

        Bundle args = new Bundle();
        args.putInt(Constants.CHECKED_KEY, checked_position);
        ProjectOptionFragment fragment = new ProjectOptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.MyDialogTheme);
        builder.setTitle("Todo Type");
        final String[] items = getResources().getStringArray(R.array.Todo_Type);
        final ArrayList<String> itemList = new ArrayList<>(Arrays.asList(items));
        builder.setSingleChoiceItems(items, checked_position,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mInterfaceListener.onComplete(which, itemList.get(which));
                        dismiss();
                    }
                });




        LayoutInflater inflater = LayoutInflater.from(getContext());


        return builder.create();    }



}
