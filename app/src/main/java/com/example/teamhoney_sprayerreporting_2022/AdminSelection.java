package com.example.teamhoney_sprayerreporting_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class AdminSelection extends AppCompatActivity {
    private Button add;
    private Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_selection);
        delete = (Button) findViewById(R.id.delete_button);
        add = (Button) findViewById(R.id.add_button);
        /*add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openAddUser();
            }
        });*/
    }
    public void openAddUser(View view) {
        Intent intent = new Intent(this, AddUser.class);
        startActivity(intent);
    }

    public void openDeleteUser(View view) {
        Intent intent = new Intent(this, DeleteUsers.class);
        startActivity(intent);
    }

    public void openAddChem(View view) {
        Intent intent = new Intent(this, AddChemical.class);
        startActivity(intent);
    }

    public void openDeleteChem(View view) {
        Intent intent = new Intent(this, DeleteChemicals.class);
        startActivity(intent);
    }

    public void signOut(View view) {
        super.finish();
    }

    public void openEntry(View view) {
        Intent intent = new Intent(this, SprayEntries.class);
        startActivity(intent);
    }
}
