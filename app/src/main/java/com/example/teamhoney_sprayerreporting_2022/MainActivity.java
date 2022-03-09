package com.example.teamhoney_sprayerreporting_2022;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity{
    private TextInputLayout usernameView;
    private TextInputLayout passwordView;
    private String username = "";
    private String password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameView = findViewById(R.id.usernameBox);
        passwordView = findViewById(R.id.passwordBox);
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
        Log.d("test", username);
        Log.d("test", password);
        //Code to check username and password in .csv
        Scanner sc = null;
        try {
            sc = new Scanner(new File("test"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (sc != null) {
            sc.close();
        }
    }
}
