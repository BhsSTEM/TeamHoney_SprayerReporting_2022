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

public class DeleteChemicals extends AppCompatActivity {

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
                PopupWindow deleteChemPopup = openDeletePopup(view, clickPos);

                Button deleteBtn = deleteChemPopup.getContentView().findViewById(R.id.deleteChemBtn);

                deleteBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        deleteChemical(clickPos);
                        deleteChemPopup.dismiss();
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
        ArrayList<Chemical> chem = getChemicals();
        writeToChart(chem);
    }

    public PopupWindow openDeletePopup(View view, int clickPos) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.deletechem_popup, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        updateDeletePopup(popupWindow, clickPos);

        return popupWindow;
    }

    public void updateDeletePopup(PopupWindow popupWindow, int clickPos) {
        TextView nameText = popupWindow.getContentView().findViewById(R.id.nameTextChem);
        TextView emailText = popupWindow.getContentView().findViewById(R.id.actInTextChem);
        TextView addressText = popupWindow.getContentView().findViewById(R.id.EPATextChem);

        Chemical chemical = getChemicals().get(clickPos);

        nameText.setText("Name: " + chemical.name);
        emailText.setText("Active Ingredient/s: " + chemical.actIn);
        addressText.setText("EPA #: " + chemical.epa);
    }

    public void deleteChemical(int clickPos) {
        ArrayList<String> userIds =  MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals"})));
        String selectedUser = userIds.get(clickPos);
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", selectedUser})), null);
    }

    public ArrayList<Chemical> getChemicals() {
        ArrayList<Chemical> users = new ArrayList<Chemical>();
        ArrayList<String> userIdList = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals"})));

        for(int i = 0; i < userIdList.size(); i++) {
            Chemical chem = new Chemical();
            chem.name = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", userIdList.get(i), "Name"})));
            chem.actIn = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", userIdList.get(i), "ActIn"})));
            chem.epa = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", userIdList.get(i), "EPA"})));
            users.add(chem);
        }
        return users;
    }

    public void writeToChart(ArrayList<Chemical> chems) {
        String[] entryTexts = new String[chems.size()];
        for(int i = 0; i < chems.size(); i++) {
            Chemical chemVals = chems.get(i); //users.get(i).getUserVals();
            String entryText =
                    chemVals.name + " | " +
                    chemVals.actIn + " | " +
                    chemVals.epa;

            entryTexts[i] = entryText;
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.entry_chart_text_format, R.id.entryText, entryTexts);
        userList.setAdapter(arrayAdapter);
    }
}
