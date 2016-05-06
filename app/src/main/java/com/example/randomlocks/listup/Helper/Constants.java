package com.example.randomlocks.listup.Helper;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by randomlocks on 5/4/2016.
 */
public class Constants {

    public static final String TO_DO_LIST_TAG="todolist";
    public static final String ADD_TO_DO_LIST_TAG="addtodolist";
    public static final String CHECKED_STRING_KEY="checkedKey";
    public static final String IS_CHECKED_KEY="isCheckedKey";
    public static final String CHECKED_KEY="checkedKey";
    public static final String PROJECT_TYPE = "projectType";
    public static final String TODO_LIST = "todoRecyclerList";
    public static final String UPDATE_DATE = "update";
    public static final int SORT_BY_TITLE = 0;
    public static final int SORT_BY_DATE = 1;
    public static final int SORT_BY_EDITED = 2;


    public static void hideKeyBoard(View v,Context context){

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);


    }

    public static void showKeybaord(View v,Context context){

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInputFromInputMethod(v.getWindowToken(),0);

    }

}
