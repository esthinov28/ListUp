package com.example.randomlocks.listup.Fragments;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randomlocks.listup.Adapter.DatabaseAdapter;
import com.example.randomlocks.listup.Adapter.TodoListRecyclerAdapter;
import com.example.randomlocks.listup.AsyncTask.QueryAllNotes;
import com.example.randomlocks.listup.Helper.Constants;
import com.example.randomlocks.listup.Helper.SharedPreferenceHelper;
import com.example.randomlocks.listup.Modal.DatabaseModel;
import com.example.randomlocks.listup.R;


public class TodoList extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Toolbar toolbar;
    Activity activity;
    FloatingActionButton addTodo;
    RecyclerView recyclerView;
    TodoListRecyclerAdapter adapter;
    TextView emptyTextHint;



    private enum LayoutManagerType {
        STAGGERED_GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER;

        public static LayoutManagerType toMyEnum (String myEnumString) {
            try {
                return valueOf(myEnumString);
            } catch (Exception ex) {
                // For error cases
                return LayoutManagerType.LINEAR_LAYOUT_MANAGER;
            }
        }



    }

    protected LayoutManagerType mCurrentLayoutManagerType;
    protected RecyclerView.LayoutManager mLayoutManager;
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;







    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TodoList() {
        // Required empty public constructor
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(DatabaseModel model,Long RowId);

    }









    // TODO: Rename and change types and number of parameters
    public static TodoList newInstance(String param1, String param2) {
        TodoList fragment = new TodoList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_list, container, false);
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
        activity = getActivity();
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) activity.findViewById(R.id.recycler_view);
        recyclerView.scrollToPosition(0);
        addTodo = (FloatingActionButton) activity.findViewById(R.id.fab);
        emptyTextHint = (TextView) activity.findViewById(R.id.empty_text);

        //Setting toolbar
        ((AppCompatActivity)activity).setSupportActionBar(toolbar);
        ((AppCompatActivity)activity).setTitle("TodoList");


        //on clicking fab "new"
        addTodo.setOnClickListener(this);

        /********** SETTING RECYCLER VIEW **************/






        mCurrentLayoutManagerType = LayoutManagerType.toMyEnum(SharedPreferenceHelper.getFromSharedPreferences(KEY_LAYOUT_MANAGER,LayoutManagerType.LINEAR_LAYOUT_MANAGER.toString(),getContext()));
      setRecyclerViewLayoutManager(mCurrentLayoutManagerType);



        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mCurrentLayoutManagerType = (LayoutManagerType) savedInstanceState
                    .getSerializable(KEY_LAYOUT_MANAGER);
        }

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);
       // Cursor cursor = dbadapter.getAllNote(Constants.SORT_BY_DATE);

        new QueryAllNotes(getContext(), new QueryAllNotes.QueryAllNodeInterface() {
            @Override
            public void processFinish(Cursor cursor) {
                if(cursor.getCount()!=0){
                    emptyTextHint.setVisibility(View.GONE);
                }
                adapter = new TodoListRecyclerAdapter(getContext(), cursor, new TodoListRecyclerAdapter.InterfaceListener() {
                    @Override
                    public void onItemClick(DatabaseModel model,Long rowId) {

                        mListener.onFragmentInteraction(model,rowId);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        }).execute(Constants.SORT_BY_DATE);




    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_todo_list_fragmet, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.change_view :

                if(mCurrentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER){
                    setRecyclerViewLayoutManager(LayoutManagerType.STAGGERED_GRID_LAYOUT_MANAGER);
                }else{
                    setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER);
                }
                break;

            case R.id.search :
                Toast.makeText(getContext(),"Todo",Toast.LENGTH_SHORT).show();
                break;

            case R.id.sort :
                Toast.makeText(getContext(),"Todo",Toast.LENGTH_SHORT).show();
                break;









        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //when user click addtoDolist
    @Override
    public void onClick(View v) {



        mListener.onFragmentInteraction(null, null);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        int scrollPosition = 0;

        // If a layout manager has already been set, get current scroll position.
        if (recyclerView.getLayoutManager() != null && mCurrentLayoutManagerType==LayoutManagerType.LINEAR_LAYOUT_MANAGER) {
            scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }

        switch (layoutManagerType) {
            case STAGGERED_GRID_LAYOUT_MANAGER:
                mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT,StaggeredGridLayoutManager.VERTICAL);
                mCurrentLayoutManagerType = LayoutManagerType.STAGGERED_GRID_LAYOUT_MANAGER;
                break;
            case LINEAR_LAYOUT_MANAGER:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                break;
            default:
                mLayoutManager = new LinearLayoutManager(getActivity());
                mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        }

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.scrollToPosition(scrollPosition);
    }


    @Override
    public void onPause() {
        SharedPreferenceHelper.saveToSharedPreference(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType.toString(), getContext());
        super.onPause();

    }
}
