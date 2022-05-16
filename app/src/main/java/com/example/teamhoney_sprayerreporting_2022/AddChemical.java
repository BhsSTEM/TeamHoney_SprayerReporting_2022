package com.example.teamhoney_sprayerreporting_2022;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class AddChemical extends AppCompatActivity {
    //private DatabaseReference mDatabase;
    //mDatabase = FirebaseDatabase.getInstance().getReference();
    //private Database base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_chemical);
        Button submitBtn = (Button) findViewById(R.id.submitButton);
        Button backBtn = (Button) findViewById(R.id.addUserToAdminButton);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText nameInput = (EditText) findViewById(R.id.editNameChem);
                EditText actInInput = (EditText) findViewById(R.id.editActiveInChem);
                EditText EPAInput = (EditText) findViewById(R.id.editEPAChem);

                int slot = findAvailableChemicalSlot();

                if(!nameInput.getText().toString().equals("") && !actInInput.getText().toString().equals("") && !EPAInput.getText().toString().equals("")) {
                    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", Integer.toString(slot), "Name"})), nameInput.getText().toString());
                    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", Integer.toString(slot), "ActIn"})), actInInput.getText().toString().toLowerCase());
                    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", Integer.toString(slot), "EPA"})), EPAInput.getText().toString());
                    nameInput.setText("");
                    actInInput.setText("");
                    EPAInput.setText("");
                }
            }
        });
    }

    public int findAvailableChemicalSlot() {
        ArrayList<String> fieldIds = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals"})));
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
