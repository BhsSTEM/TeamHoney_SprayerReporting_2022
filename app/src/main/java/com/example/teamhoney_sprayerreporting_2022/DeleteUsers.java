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

import org.w3c.dom.Text;

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

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int clickPos, long l) {
                PopupWindow deletePopup = openDeletePopup(view, clickPos);

                Button deleteBtn = deletePopup.getContentView().findViewById(R.id.deleteBtn);

                deleteBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        deleteUser(clickPos);
                        deletePopup.dismiss();
                    }
                });
            }
        });

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
        writeToChart(users);
    }

    public PopupWindow openDeletePopup(View view, int clickPos) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.delete_popup, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        updateDeletePopup(popupWindow, clickPos);

        return popupWindow;
    }

    public void updateDeletePopup(PopupWindow popupWindow, int clickPos) {
        TextView nameText = popupWindow.getContentView().findViewById(R.id.nameText);
        TextView emailText = popupWindow.getContentView().findViewById(R.id.emailText);
        TextView passwordText = popupWindow.getContentView().findViewById(R.id.passwordText);
        TextView addressText = popupWindow.getContentView().findViewById(R.id.addressText);
        TextView adminText = popupWindow.getContentView().findViewById(R.id.adminText);

        userFetch user = getUsers().get(clickPos);

        nameText.setText("Name: " + user.name);
        emailText.setText("Email: " + user.email);
        passwordText.setText("Password: " + user.password);
        addressText.setText("Address: " + user.address);
        if(user.admin.equals("true")) {
            adminText.setText("Admin");
        } else {
            adminText.setText("");
        }
    }

    public void deleteUser(int clickPos) {
        ArrayList<String> userIds =  MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Users"})));
        String selectedUser = userIds.get(clickPos);
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Users", selectedUser})), null);
        if(Integer.parseInt(selectedUser) == MainActivity.currUserId) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
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
                    userVals.address;
            if(userVals.admin.equals("true")) {
                entryText = entryText + " | Admin";
            }
            entryTexts[i] = entryText;
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.entry_chart_text_format, R.id.entryText, entryTexts);
        userList.setAdapter(arrayAdapter);
    }


}