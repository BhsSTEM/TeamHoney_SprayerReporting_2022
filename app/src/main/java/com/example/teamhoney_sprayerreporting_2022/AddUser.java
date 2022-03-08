package com.example.teamhoney_sprayerreporting_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;

public class AddUser extends AppCompatActivity {
    String name;
    String email;
    String username;
    String password;

    Button submitButton;

    EditText nameInput;
    EditText emailInput;
    EditText usernameInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        nameInput = (EditText) findViewById(R.id.editName);
        emailInput = (EditText) findViewById(R.id.editTexEmail);
        usernameInput = (EditText) findViewById(R.id.editTextUsername);
        passwordInput = (EditText) findViewById(R.id.editTextPassword);

        submitButton = (Button) findViewById(R.id.button2);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString();
                email = emailInput.getText().toString();
                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();
            }
            public void saveInfo(View view){
                String File = "Admin_info.csv";
                name = nameInput.getText().toString();
                email = emailInput.getText().toString();
                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();
                try{
                    FileOutputStream out = openFileOutput(File, Context.MODE_APPEND);
                    out.write(name.getBytes());
                    out.write(email.getBytes());
                    out.write(username.getBytes());
                    out.write(password.getBytes());;
                    out.close();
                }
                catch(Exception e) {
                e.printStackTrace();
                }



            }
        });


        }
    }