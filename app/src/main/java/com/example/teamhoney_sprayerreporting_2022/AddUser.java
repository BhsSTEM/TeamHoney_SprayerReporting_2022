package com.example.teamhoney_sprayerreporting_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import android.content.Intent;
import android.widget.Switch;

public class AddUser extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    //mDatabase = FirebaseDatabase.getInstance().getReference();
    //private Database base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_user);
        Button submitBtn = (Button) findViewById(R.id.submitButton);
        Button backBtn = (Button) findViewById(R.id.addUserToAdminButton);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameInput = (EditText) findViewById(R.id.editName);
                EditText emailInput = (EditText) findViewById(R.id.editMail);
                EditText addressInput = (EditText) findViewById(R.id.editAddress);
                EditText passwordInput = (EditText) findViewById(R.id.editPassword);
                Switch adminSwitch = findViewById(R.id.adminSwitch);

                int slot = findAvailableUserSlot();

                if(!nameInput.getText().toString().equals("") && !emailInput.getText().toString().equals("") && !addressInput.getText().toString().equals("") && !passwordInput.getText().toString().equals("")) {
                    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", Integer.toString(slot), "Name"})), nameInput.getText().toString());
                    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", Integer.toString(slot), "Email"})), emailInput.getText().toString());
                    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", Integer.toString(slot), "Address"})), addressInput.getText().toString());
                    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", Integer.toString(slot), "Password"})), passwordInput.getText().toString());
                    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", Integer.toString(slot), "Admin"})), Boolean.toString(adminSwitch.isChecked()));
                }
            }
        });
    }

    public int findAvailableUserSlot() {
        ArrayList<String> fieldIds = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Users"})));
        int slot = Integer.parseInt(fieldIds.get(fieldIds.size() - 1)) + 1;
        for(int i = 0; i < fieldIds.size() + 1; i++) {
            if(fieldIds != null && !fieldIds.contains(Integer.toString(i))) {
                slot = i;
                i = fieldIds.size();
            }
        }
        return slot;
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