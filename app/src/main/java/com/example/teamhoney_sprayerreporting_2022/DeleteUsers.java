package com.example.teamhoney_sprayerreporting_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class DeleteUsers extends AppCompatActivity {

    ListView userList;
    private Handler handler;
    private int interval = 1000;
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
        ArrayList<userFetch> entries = getUsers();
        writeToChart(entries);
    }
    public ArrayList<userFetch> getUsers() {
        ArrayList<userFetch> entries = new ArrayList<userFetch>();
        ArrayList<String> entryIdList = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Users"})));

        for(int i = 0; i < entryIdList.size(); i++) {
            userFetch entry = new userFetch();
            entry.address = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", entryIdList.get(i), "Address"})));
            entry.admin = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", entryIdList.get(i), "Admin"})));
            entry.email = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", entryIdList.get(i), "Email"})));
            entry.password = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", entryIdList.get(i), "Password"})));
            entry.name = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", entryIdList.get(i), "Name"})));
            entries.add(entry);
        }
        return entries;
    }
    public void writeToChart(ArrayList<userFetch> entries) {
        String[] entryTexts = new String[entries.size()];
        for(int i = 0; i < entries.size(); i++) {
            userFetch entryVals = entries.get(i).getEntryVals();
            String entryText =
                    entryVals.name + " " +
                    entryVals.email + " " +
                    entryVals.password + " " +
                    entryVals.address + " " +
                    entryVals.admin;
            entryTexts[i] = entryText;
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.entry_chart_text_format, R.id.entryText, entryTexts);
        userList.setAdapter(arrayAdapter);
    }

}