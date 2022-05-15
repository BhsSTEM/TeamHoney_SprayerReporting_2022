package com.example.teamhoney_sprayerreporting_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class DeleteUsers extends AppCompatActivity {

    ListView userList;
    private Handler handler;
    private int interval = 1000;
    private int selectedEntry = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_delete_user);
        userList = (ListView) findViewById(R.id.userListView);
        handler = new Handler();
        startUpdating();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUpdating();
    }

    Runnable StatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateUsers();
            }
            finally {
                handler.postDelayed(StatusChecker, interval);
            }
        }
    };

    void startUpdating() {
        StatusChecker.run();
    }

    void stopUpdating() {
        handler.removeCallbacks(StatusChecker);
    }

    public void updateUsers() {
        ArrayList<userFetch> users = getUsers();
        Log.d("j", users.get(0).name);
        writeToChart(users);
    }


    public ArrayList<userFetch> getUsers() {
        ArrayList<userFetch> users = new ArrayList<userFetch>();
        ArrayList<String> userIdList = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Users"})));

        for(int i = 0; i < userIdList.size(); i++) {
            userFetch user = new userFetch();
            user.address = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", userIdList.get(i), "Address"})));
            user.admin = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", userIdList.get(i), "Admin"})));
            user.email = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", userIdList.get(i), "Email"})));
            user.password = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", userIdList.get(i), "Password"})));
            user.name = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", userIdList.get(i), "Name"})));
            users.add(user);
        }
        return users;
    }

    public void writeToChart(ArrayList<userFetch> users) {
        String[] entryTexts = new String[users.size()];
        for(int i = 0; i < users.size(); i++) {
            userFetch userVals = users.get(i); //users.get(i).getUserVals();
            String entryText =
                    userVals.name + " | " +
                    userVals.email + " | " +
                    userVals.password + " | " +
                    userVals.address + " | " +
                    userVals.admin;
            entryTexts[i] = entryText;
        }
        Log.d("h", entryTexts[0]);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.entry_chart_text_format, R.id.entryText, entryTexts);
        userList.setAdapter(arrayAdapter);
    }


}