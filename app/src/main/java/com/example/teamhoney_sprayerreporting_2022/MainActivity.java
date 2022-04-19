package com.example.teamhoney_sprayerreporting_2022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity{
    private TextInputLayout usernameView;
    private TextInputLayout passwordView;
    private String username = "";
    private String password = "";
    private ArrayList<String> usernames = new ArrayList<>();
    private ArrayList<String> passwords = new ArrayList<>();
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        usernameView = findViewById(R.id.usernameBox);
        passwordView = findViewById(R.id.passwordBox);
        button = (Button) findViewById(R.id.signinButton);
        usernames.add("admin");
        passwords.add("password");
    }

    public void openAdminSelection(){
        Intent intent = new Intent(this, AdminSelection.class);
        startActivity(intent);
    }

    public void close(View view){
        closeKeyboard();
    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void signIn(View view) {
        //noinspection ConstantConditions
        username = usernameView.getEditText().getText().toString();
        //noinspection ConstantConditions
        password = passwordView.getEditText().getText().toString();
        //Code to check username and password
        if(TextUtils.isEmpty(usernameView.getEditText().getText().toString()) || TextUtils.isEmpty(passwordView.getEditText().getText().toString())){
            Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_LONG).show();
        }
        else{
            for(int i = 0;i<usernames.size();i++){
                if(usernameView.getEditText().getText().toString().equals(usernames.get(i))){
                    if(passwordView.getEditText().getText().toString().equals(passwords.get(i))){
                        Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                        if(usernames.get(i).equals("admin")){
                            openAdminSelection();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
