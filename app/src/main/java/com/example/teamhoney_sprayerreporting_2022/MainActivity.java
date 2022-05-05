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
    public static Database dataBase;
    public static int currUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dataBase = new Database();
        usernameView = findViewById(R.id.usernameBox);
        passwordView = findViewById(R.id.passwordBox);
        Button signInBtn = (Button) findViewById(R.id.signinButton);
        currUserId = -1;

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(view);
            }
        });

        //Intent intent = new Intent(this, AdminSelection.class);
        //startActivity(intent);
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
        ArrayList<String> userIds = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Users"})));
        Log.d("userId", userIds.get(0));
        ArrayList<String> emails = new ArrayList<String>();
        ArrayList<String> passwords = new ArrayList<String>();
        ArrayList<String> admins = new ArrayList<String>();

        for(int i = 0; i < userIds.size(); i++) {
            emails.add(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", userIds.get(i), "Email"}))));
            passwords.add(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", userIds.get(i), "Password"}))));
            admins.add(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", userIds.get(i), "Admin"}))));
        }

        EditText emailText = findViewById(R.id.emailText);
        EditText passwordText = findViewById(R.id.passwordText);
        int i;
        boolean found = false;
        for(i = 0; i < emails.size(); i++) {
            if(emails.get(i).equals(emailText.getText().toString()) && passwords.get(i).equals(passwordText.getText().toString())) {
                found = true;
                currUserId = Integer.parseInt(userIds.get(i));
                emailText.setText("");
                passwordText.setText("");
                if(admins.get(i).equals("true")) {
                    Intent intent = new Intent(this, AdminSelection.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, SprayEntries.class);
                    startActivity(intent);
                }
            }
        }
        if(!found) {
            Toast.makeText(MainActivity.this, "Email or password is incorrect", Toast.LENGTH_LONG).show();
        }
    }
}
