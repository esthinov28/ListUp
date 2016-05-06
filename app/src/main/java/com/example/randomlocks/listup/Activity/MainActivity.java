package com.example.randomlocks.listup.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.randomlocks.listup.Fragments.AddTodoList;
import com.example.randomlocks.listup.Fragments.TodoList;
import com.example.randomlocks.listup.Helper.Constants;
import com.example.randomlocks.listup.Modal.DatabaseModel;
import com.example.randomlocks.listup.R;

public class MainActivity extends AppCompatActivity implements TodoList.OnFragmentInteractionListener , AddTodoList.OnFragmentInteractionListener  {

    FrameLayout fragmentContainer;
    TodoList todoListFragment=null;
    AddTodoList addTodoListFragment=null;
    FragmentManager fragmentManager;
    private static boolean shouldExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        fragmentContainer = (FrameLayout) findViewById(R.id.layout);

        /***************************** ADDING TODOLIST FRAGMENT **************************/


        if(savedInstanceState==null){
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            todoListFragment = TodoList.newInstance("","");
            fragmentTransaction
                    .add(fragmentContainer.getId(), todoListFragment, Constants.TO_DO_LIST_TAG)
                    .commit();

        }else {

            todoListFragment = (TodoList) fragmentManager.findFragmentByTag(Constants.TO_DO_LIST_TAG);
        }


    }

    /********* ON PRESSING ADD FLOATING ACTION BUTTON ***************/

    @Override
    public void onFragmentInteraction(DatabaseModel model,Long rowId) {

        //Open a new AddToDo task when model= null else open an existing TODO task

        /* if (addTodoListFragment==null) {
            addTodoListFragment = new AddTodoList();
        }else {
            addTodoListFragment = (AddTodoList) fragmentManager.findFragmentByTag(Constants.ADD_TO_DO_LIST_TAG);
        } */

        addTodoListFragment = new AddTodoList();
        addTodoListFragment.setModel(model);
        addTodoListFragment.setRowId(rowId);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right,R.anim.slide_in_left)
                .replace(fragmentContainer.getId(), addTodoListFragment, Constants.ADD_TO_DO_LIST_TAG)
                .addToBackStack(null)
                .commit();



    }


    /********** ON SUBMITTING THE TASK *****************/

    @Override
    public void onAddTodo() {


        if(todoListFragment!=null){

         /*   fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left,R.anim.slide_in_right)
                    .replace(fragmentContainer.getId(),todoListFragment,Constants.TO_DO_LIST_TAG)
                    .commit(); */
        fragmentManager.popBackStack();
        }

    }


    @Override
    public void onBackPressed() {
        if(fragmentManager.getBackStackEntryCount()==0){
            if(shouldExit){
                shouldExit=false;
                super.onBackPressed();
            }else {
                shouldExit=true;
                Toast.makeText(this,"Press again to go back",Toast.LENGTH_SHORT).show();
            }

        }else {
            super.onBackPressed();
        }

    }


}
