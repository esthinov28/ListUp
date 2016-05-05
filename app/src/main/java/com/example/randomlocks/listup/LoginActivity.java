package com.example.randomlocks.listup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.randomlocks.listup.Helper.SharedPreferenceHelper;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout mEmailLayout,mPasswordLayout;
    TextInputEditText mEmail,mPassword;
    Button mLogin,mRegister;
    CheckBox rememberMe;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        mEmail = (TextInputEditText) findViewById(R.id.email);
        mEmailLayout = (TextInputLayout) mEmail.getParent();
        mPassword = (TextInputEditText) findViewById(R.id.password);
        mPasswordLayout = (TextInputLayout) mPassword.getParent();
        mLogin = (Button) findViewById(R.id.login);
        mRegister = (Button) findViewById(R.id.register);
        rememberMe = (CheckBox) findViewById(R.id.checkBox);

        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);



        boolean comingFromSignup=getIntent().getBooleanExtra(SharedPreferenceHelper.SUCCESSFULL,false);
        if(comingFromSignup){
            Snackbar.make(linearLayout,"Sign Up Successful",Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.login :

                boolean shouldContinue = false;
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String storedEmail = SharedPreferenceHelper.getFromSharedPreferences(SharedPreferenceHelper.EMAIL, null, this);
                String storedPassword = SharedPreferenceHelper.getFromSharedPreferences(SharedPreferenceHelper.PASSWORD,null,this);



                if((storedEmail!=null && storedEmail.equals(email))&&(storedPassword!=null && storedPassword.equals(password))){

                    shouldContinue = true;
                }else {
                    mEmailLayout.setErrorEnabled(true);
                    mPasswordLayout.setErrorEnabled(true);
                    mPasswordLayout.setError("Enter a correct email & password");
                }

                if(shouldContinue){
                    /*********** Save the remember me information and open main activity *************/

                    SharedPreferenceHelper.saveToSharedPreference(SharedPreferenceHelper.REMEMBER_ME,rememberMe.isChecked(),this);
                    Intent it = new Intent(this,MainActivity.class);
                    startActivity(it);
                }






                break;

            case R.id.register :
                Intent it = new Intent(this,SignupActivity.class);
                startActivity(it);



        }
    }
}

