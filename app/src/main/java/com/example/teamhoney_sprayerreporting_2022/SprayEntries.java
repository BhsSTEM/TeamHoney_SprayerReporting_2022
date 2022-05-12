package com.example.teamhoney_sprayerreporting_2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class SprayEntries extends AppCompatActivity {

    private LinearLayout layout;
    private Handler handler;
    private int interval = 1000;     //1 sec
    private ListView entryList;
    private int selectedEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        layout = new LinearLayout(this);
        setContentView(R.layout.activity_spray_entries);
        layout.setOrientation(LinearLayout.VERTICAL);
        handler = new Handler();
        entryList = (ListView)findViewById(R.id.EntryListView);
        selectedEntry = 0;
        Intent mapIntent = new Intent(this, MapsActivity.class);

        startUpdating();

        Button addBtn = (Button) findViewById(R.id.AddBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindow addPopup = openAddPopup(view);
                Button addEntryBtn = (Button) addPopup.getContentView().findViewById(R.id.addBtn);
                addEntryBtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        addEntry(addPopup);
                        addPopup.dismiss();
                    }
                });
            }
        });

        Button mapBtn = (Button) findViewById(R.id.MapBtn);

        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(mapIntent);
            }
        });

        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int clickPos, long l) {
                PopupWindow infoPopup = openInfoPopup(view, clickPos);

                Button deleteBtn = infoPopup.getContentView().findViewById(R.id.deleteBtn);

                deleteBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        deleteEntry();
                        infoPopup.dismiss();
                    }
                });
            }
        });

        if(!MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", Integer.toString(MainActivity.currUserId), "Admin"}))).equals("true")) {
            Button backBtn = findViewById(R.id.goBackButton);
            backBtn.setText("Sign out");
        }
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
                updateEntries();
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

    public void updateEntries() {
        ArrayList<SprayEntry> entries = getEntries();
        writeToChart(entries);
    }

    public ArrayList<SprayEntry> getEntries() {
        ArrayList<SprayEntry> entries = new ArrayList<SprayEntry>();
        ArrayList<String> entryIdList = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries"})));

        for(int i = 0; i < entryIdList.size(); i++) {
            SprayEntry entry = new SprayEntry();
            entry.address = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", entryIdList.get(i), "Address"})));
            entry.appMethod = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", entryIdList.get(i), "AppMethod"})));
            entry.chemical = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", entryIdList.get(i), "Chemical"})));
            entry.date = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", entryIdList.get(i), "Date"})));
            entry.field = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", entryIdList.get(i), "Field"})));
            entry.user = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", entryIdList.get(i), "User"})));
            entries.add(entry);
        }
        return entries;
    }

    public void writeToChart(ArrayList<SprayEntry> entries) {
        String[] entryTexts = new String[entries.size()];
        for(int i = 0; i < entries.size(); i++) {
            SprayEntry entryVals = entries.get(i).getEntryVals();
            String entryText =
            entryVals.date + " " +
            entryVals.user + " " +
            entryVals.field + " " +
            entryVals.chemical;
            entryTexts[i] = entryText;
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.entry_chart_text_format, R.id.entryText, entryTexts);
        entryList.setAdapter(arrayAdapter);
    }

    public void updateAddPopup(PopupWindow popupWindow) {
        Spinner fieldSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.fieldSpinner);
        Spinner appMethodSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.appMethodSpinner);
        Spinner chemSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.chemSpinner);

        ArrayList<String> itemIds = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields"})));
        ArrayList<String> itemNames = new ArrayList<String>();
        for(int i = 0; i < itemIds.size(); i++) {
            itemNames.add(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", itemIds.get(i), "Name"}))));
        }
        ArrayAdapter<String> fieldAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemNames);
        fieldAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fieldSpinner.setAdapter(fieldAdapter);

        itemIds = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"ApplicationMethod"})));
        itemNames = new ArrayList<String>();
        for(int i = 0; i < itemIds.size(); i++) {
            itemNames.add(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"ApplicationMethod", itemIds.get(i)}))));
        }
        ArrayAdapter<String> appMethodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemNames);
        appMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appMethodSpinner.setAdapter(appMethodAdapter);

        itemIds = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals"})));
        itemNames = new ArrayList<String>();
        for(int i = 0; i < itemIds.size(); i++) {
            itemNames.add(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", itemIds.get(i), "Name"}))));
        }
        ArrayAdapter<String> chemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemNames);
        chemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chemSpinner.setAdapter(chemAdapter);
    }

    public void updateInfoPopup(PopupWindow popupWindow, int entryId) {
        TextView dateText = popupWindow.getContentView().findViewById(R.id.dateText);
        TextView userText = popupWindow.getContentView().findViewById(R.id.userText);
        TextView chemText = popupWindow.getContentView().findViewById(R.id.chemText);
        TextView actInText = popupWindow.getContentView().findViewById(R.id.actInText);
        TextView EPAText = popupWindow.getContentView().findViewById(R.id.EPAText);
        TextView fieldText = popupWindow.getContentView().findViewById(R.id.fieldText);
        TextView appMethodText = popupWindow.getContentView().findViewById(R.id.appMethodText);

        ArrayList<SprayEntry> entries = getEntries();

        SprayEntry entryVals = entries.get(entryId).getEntryVals();

        dateText.setText("Date: " + entryVals.date);
        userText.setText("Entered by: " + entryVals.user);
        chemText.setText("Chemical: " + entryVals.chemical);
        actInText.setText(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", entries.get(entryId).chemical, "ActIn"}))));
        EPAText.setText(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", entries.get(entryId).chemical, "EPA"}))));
        fieldText.setText("Field: " + entryVals.field);
        appMethodText.setText("Method: " + entryVals.appMethod);
    }

    public void addEntry(PopupWindow popupWindow) {
        Spinner fieldSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.fieldSpinner);
        Spinner appMethodSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.appMethodSpinner);
        Spinner chemSpinner = (Spinner) popupWindow.getContentView().findViewById(R.id.chemSpinner);
        EditText rateText = popupWindow.getContentView().findViewById(R.id.rateText);
        EditText windText = popupWindow.getContentView().findViewById(R.id.windText);
        EditText tempText = popupWindow.getContentView().findViewById(R.id.tempText);
        EditText humidText = popupWindow.getContentView().findViewById(R.id.humidText);

        int entryId = getAvailableEntryId();
        String currentDate = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());

        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(entryId), "Field"})), Integer.toString(fieldSpinner.getSelectedItemPosition()));
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(entryId), "AppMethod"})), Integer.toString(appMethodSpinner.getSelectedItemPosition()));
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(entryId), "Chemical"})), Integer.toString(chemSpinner.getSelectedItemPosition()));
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(entryId), "appDensity"})), rateText.getText().toString());
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(entryId), "windSpeed"})), windText.getText().toString());
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(entryId), "temperature"})), tempText.getText().toString());
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(entryId), "temperature"})), humidText.getText().toString());
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(entryId), "Date"})), currentDate);
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(entryId), "User"})), Integer.toString(MainActivity.currUserId));
    }

    public int getAvailableEntryId() {
        ArrayList<String> entries = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries"})));
        boolean entryExists = true;
        int i = -1;

        while(entryExists) {
            i++;
            if(!entries.contains(Integer.toString(i))) {
                entryExists = false;
            }
        }
        return i;
    }

    public PopupWindow openAddPopup(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_popup, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        updateAddPopup(popupWindow);

        return popupWindow;
    }

    public PopupWindow openInfoPopup(View view, int entryId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.info_popup, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        updateInfoPopup(popupWindow, entryId);
        selectedEntry = entryId;

        return popupWindow;
    }

    public void deleteEntry() {
        ArrayList<SprayEntry> entries = getEntries();
        for(int i = selectedEntry; i < entries.size() - 1; i++) {
            MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(i), "AppMethod"})), MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(i + 1), "AppMethod"}))));
            MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(i), "Chemical"})), MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(i + 1), "Chemical"}))));
            MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(i), "Date"})), MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(i + 1), "Date"}))));
            MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(i), "Field"})), MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(i + 1), "Field"}))));
            MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(i), "User"})), MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(i + 1), "User"}))));
        }
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Entries", Integer.toString(entries.size() - 1)})), null);
    }

    public void goBack(View view) {
        super.finish();
    }
}