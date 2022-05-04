package com.example.teamhoney_sprayerreporting_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class AddUser extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    //mDatabase = FirebaseDatabase.getInstance().getReference();
    String name;
    String email;
    String username;
    String password;
    //private Database base;



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
        emailInput = (EditText) findViewById(R.id.editMail);
        usernameInput = (EditText) findViewById(R.id.editUsername);
        passwordInput = (EditText) findViewById(R.id.editPassword);

        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                name = nameInput.getText().toString();
                email = emailInput.getText().toString();
                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();
                int x = AddOneToNumberOfusers();
                System.out.println(String.valueOf(x));
                MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", String.valueOf(x), "Name"})), name);
                MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", String.valueOf(x), "Email"})), email);
                MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", String.valueOf(x), "Username"})), username);
                MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", String.valueOf(x), "Password"})), password);


            }
public int AddOneToNumberOfusers(){
    String stringval = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Counter"})));
    int intval = Integer.parseInt(stringval);
    intval++;

    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Counter"})), String.valueOf(intval));
    return intval++;
}
            public void saveInfo(View view){
                String File = "Admin_info.csv";
//                new ArrayList<String>(Arrays.asList(new String[]{"Crops", "0"}));
//                base.write(new ArrayList<String>(Arrays.asList(new String[]{"Crops", "0"})), "sgsgs");

                name = nameInput.getText().toString();
                email = emailInput.getText().toString();
                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();
                try{
                    //base = new Database();
                    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", "0", "Name"})), name);
                }
                catch(Exception e) {
                e.printStackTrace();
                }



            }
        });


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

    public void goBack(View view) {
        super.finish();
    }
}