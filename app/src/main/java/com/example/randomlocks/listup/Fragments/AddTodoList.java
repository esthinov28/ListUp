package com.example.randomlocks.listup.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomlocks.listup.Adapter.CheckboxRecyclerAdapter;
import com.example.randomlocks.listup.Adapter.DatabaseAdapter;
import com.example.randomlocks.listup.AsyncTask.QueryAddUpdateNote;
import com.example.randomlocks.listup.Helper.Constants;
import com.example.randomlocks.listup.Modal.DatabaseModel;
import com.example.randomlocks.listup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class AddTodoList extends Fragment implements ColorPickerFragment.DialogListener,ProjectOptionFragment.InterfaceListener {

    private static final String COLOR_KEY = "color";
    Toolbar toolbar;
    TextView project,project_type;
    int whichProject=0;
    EditText title,newItem;
    RecyclerView recyclerView;
    FloatingActionButton saveButton;
    ArrayList<String> checkedList;
    ArrayList<Boolean> isCheckedList;
    DatabaseAdapter dbadapter;
    DatabaseModel model = null;
    private Long rowId = null;
    int color=R.color.color1;


    private OnFragmentInteractionListener mListener;


    public AddTodoList() {
        // Required empty public constructor
    }

    public void setRowId(Long rowId) {
        this.rowId = rowId;
    }

    @Override
    public void colorPicker(int color) {
        if(toolbar!=null)
            toolbar.setBackgroundColor(ContextCompat.getColor(getContext(),color));

        this.color = color;
    }

    /** WHEN PROJECT TYPE IS SELECTED **/

    @Override
    public void onComplete(int which, String str) {

        project_type.setText(str);
        whichProject = which;

    }


    public interface OnFragmentInteractionListener {
        void onAddTodo();

    }




    public void setModel(DatabaseModel model){
        this.model = model;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           setHasOptionsMenu(true);
        checkedList = new ArrayList<>();
        isCheckedList = new ArrayList<>();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_todo_list, container, false);
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        title = (EditText) getActivity().findViewById(R.id.title);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        project = (TextView) toolbar.findViewById(R.id.project);
        project_type = (TextView) toolbar.findViewById(R.id.project_type);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);
        newItem = (EditText) getActivity().findViewById(R.id.new_item);



        project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProjectOptionFragment projectOptionFragment = ProjectOptionFragment.newInstance(whichProject);
                projectOptionFragment.setTargetFragment(AddTodoList.this,0);
                projectOptionFragment.show(getActivity().getSupportFragmentManager(),"Project_option");


            }
        });



        if(savedInstanceState!=null)
        {
            Toast.makeText(getContext(), "hello savedinstance", Toast.LENGTH_SHORT).show();
            color = savedInstanceState.getInt(COLOR_KEY);
            toolbar.setBackgroundColor(ContextCompat.getColor(getContext(), color));
            project_type.setText(savedInstanceState.getString(Constants.PROJECT_TYPE));
            checkedList = savedInstanceState.getStringArrayList(Constants.CHECKED_KEY);
            boolean array[] = savedInstanceState.getBooleanArray(Constants.IS_CHECKED_KEY);

            for (int i = 0; i <array.length ; i++) {
                isCheckedList.add(array[i]);

            }
        }

        /************* POPULATE THE DATA FROM DATABASE *****************************/

        if(model!=null)
            populateFields();


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);


        /*********************** SETTING RECYCLER VIEW ***********************************/
        final CheckboxRecyclerAdapter adapter = new CheckboxRecyclerAdapter(checkedList,isCheckedList,getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));





        /***************************SETTING FILTERING OPTION FOR SEARCHING ********************/





        /************************** UPDATING RECYCLE VIEW *************************************/
        newItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    int curSize = adapter.getItemCount();
                    checkedList.add(v.getText().toString());
                    isCheckedList.add(false);
                    v.setText("");
                    adapter.notifyItemRangeInserted(curSize, checkedList.size());



                    return true;
                }

                return false;
            }
        });


        /************************* SAVING THE DATA TO DB *****************************/
        saveButton = (FloatingActionButton) getActivity().findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                JSONObject jsonCheckedList = new JSONObject();
                try {
                    jsonCheckedList.put(Constants.CHECKED_STRING_KEY,new JSONArray(checkedList));
                    jsonCheckedList.put(Constants.IS_CHECKED_KEY,new JSONArray(isCheckedList));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                DatabaseModel dbModel = new DatabaseModel(title.getText().toString(),color,jsonCheckedList.toString(),whichProject);

           /*     if(model==null){
                    //save the notes :-
                    Toast.makeText(getContext(),String.valueOf(whichProject),Toast.LENGTH_SHORT).show();
                    dbadapter.insertNote(dbModel);
                }else {
                    //update the note
                    boolean b =dbadapter.updateNote(rowId,dbModel);
                } */

                new QueryAddUpdateNote(getContext(), dbModel, new QueryAddUpdateNote.QueryAllNodeInterface() {
                    @Override
                    public void processFinish() {
                        Constants.hideKeyBoard(v,getContext());
                        mListener.onAddTodo();
                    }
                }).execute(rowId);




            }
        });


    }

    private void populateFields() {

        toolbar.setBackgroundColor(ContextCompat.getColor(getContext(),model.getColor()));
        color = model.getColor();
        title.setText(model.getTitle());
        whichProject = model.getTodoType();
        final String[] items = getResources().getStringArray(R.array.Todo_Type);
        final ArrayList<String> itemList = new ArrayList<>(Arrays.asList(items));
        project_type.setText(itemList.get(whichProject));
        try {
            JSONObject jsonObject = new JSONObject(model.getCheckboxText());
            JSONArray checkedString = jsonObject.optJSONArray(Constants.CHECKED_STRING_KEY);
            JSONArray isChecked = jsonObject.optJSONArray(Constants.IS_CHECKED_KEY);

            for (int i = 0; i < checkedString.length() ; i++) {
                checkedList.add(checkedString.optString(i));
                isCheckedList.add(isChecked.optBoolean(i));


            }


        } catch (JSONException e) {
            Log.e("error",e.toString());
        }


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_add_todo_list, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.change_background :
                ColorPickerFragment picker = ColorPickerFragment.newInstance(color);
                picker.setTargetFragment(this, 0);
                picker.show(getActivity().getSupportFragmentManager(),"COLOR");
                break;

            case android.R.id.home:
                getActivity().onBackPressed();
        }








        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COLOR_KEY, color);
        outState.putString(Constants.PROJECT_TYPE, project_type.getText().toString());
        outState.putStringArrayList(Constants.CHECKED_KEY, checkedList);

        boolean[] array=new boolean[isCheckedList.size()];
        for (int i = 0; i <isCheckedList.size() ; i++) {
                        array[i]= isCheckedList.get(i);
        }
        outState.putBooleanArray(Constants.IS_CHECKED_KEY,array);
    }


}
