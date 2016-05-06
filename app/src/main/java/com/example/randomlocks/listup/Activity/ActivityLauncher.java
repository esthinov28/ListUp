package com.example.randomlocks.listup.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.randomlocks.listup.Helper.SharedPreferenceHelper;
import com.example.randomlocks.listup.R;

public class ActivityLauncher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_launcher);

        boolean rememberMe = SharedPreferenceHelper.getFromSharedPreferences(SharedPreferenceHelper.REMEMBER_ME,false,this);
        if(rememberMe){
            Intent it = new Intent(this,MainActivity.class);
            startActivity(it);
        }

        else {
            Intent it = new Intent(this,LoginActivity.class);
            startActivity(it);
        }



    }
}
