package com.example.randomlocks.listup.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.randomlocks.listup.Helper.SharedPreferenceHelper;
import com.example.randomlocks.listup.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText firstName,lastName,email,password;
    TextInputLayout firstNameLayout,lastNameLayout,emaillLayout,passwordLayout;
    Button signUp,signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firstName = (TextInputEditText) findViewById(R.id.first_name);
        firstNameLayout = (TextInputLayout) firstName.getParent();

        lastName = (TextInputEditText) findViewById(R.id.last_name);
        lastNameLayout = (TextInputLayout) lastName.getParent();

        email = (TextInputEditText) findViewById(R.id.email);
        emaillLayout = (TextInputLayout) email.getParent();

        password = (TextInputEditText) findViewById(R.id.password);
        passwordLayout = (TextInputLayout) password.getParent();


        signUp = (Button) findViewById(R.id.sign_up);
        signIn = (Button) findViewById(R.id.sign_in);



        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){


            case R.id.sign_up :

                if(validateDate()){

                    /*************Saving sign up data **************************/

                    SharedPreferenceHelper.saveToSharedPreference(SharedPreferenceHelper.NAME,firstName.getText().toString()+lastName.getText().toString(),this);
                    SharedPreferenceHelper.saveToSharedPreference(SharedPreferenceHelper.EMAIL, email.getText().toString(), this);
                    SharedPreferenceHelper.saveToSharedPreference(SharedPreferenceHelper.PASSWORD, password.getText().toString(), this);
                    SharedPreferenceHelper.saveToSharedPreference(SharedPreferenceHelper.EMAIL,email.getText().toString(),this);


                    Intent it = new Intent(this,LoginActivity.class);
                    it.putExtra(SharedPreferenceHelper.SUCCESSFULL,true);
                    startActivity(it);



                }

                break;

            case R.id.sign_in :

                Intent it = new Intent(this,LoginActivity.class);
                startActivity(it);


        } //switch

    }

    private boolean validateDate() {

        boolean isValid = true;

        if(firstName.getText().toString().trim().equals("")){
            isValid = false;
            firstNameLayout.setErrorEnabled(true);
            firstNameLayout.setError("Please enter your first name ");

        }
        else {
            if (firstNameLayout.isErrorEnabled()) {
                firstNameLayout.setErrorEnabled(false);
            }
        }

        if(lastName.getText().toString().trim().equals("")){
            isValid = false;
            lastNameLayout.setErrorEnabled(true);
            lastNameLayout.setError("Please enter your last name");
        }
        else {
            if(lastNameLayout.isErrorEnabled()){
                lastNameLayout.setErrorEnabled(false);
            }
        }

        if(email.getText().toString().trim().equals("")|!email.getText().toString().contains("@")){
            isValid = false;
            emaillLayout.setErrorEnabled(true);
            emaillLayout.setError("Please enter valid email address");

        }else{
            if(emaillLayout.isErrorEnabled()){
                emaillLayout.setErrorEnabled(false);
            }
        }

        if(password.getText().toString().trim().length()<6){
            isValid = false;
            passwordLayout.setErrorEnabled(true);
            passwordLayout.setError("Password should be atleast 6 character");
        }else {
            if(passwordLayout.isErrorEnabled()){
                passwordLayout.setErrorEnabled(false);
            }
        }






return isValid;
    }


}
