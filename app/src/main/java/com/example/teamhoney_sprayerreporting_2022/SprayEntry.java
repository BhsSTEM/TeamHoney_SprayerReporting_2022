package com.example.teamhoney_sprayerreporting_2022;

import java.util.ArrayList;
import java.util.Arrays;

public class SprayEntry {
    public String address;
    public String appMethod;
    public String chemical;
    public String date;
    public String field;
    public String user;
    public String weather;
    public boolean IdOrVal;     //false for id state, true for val state

    public SprayEntry() {
        IdOrVal = false;
    }

    public SprayEntry getEntryVals() {
        SprayEntry entryVals = new SprayEntry();
        if(IdOrVal == false) {
            entryVals.date = this.date;
            entryVals.appMethod = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"ApplicationMethod", appMethod})));
            entryVals.chemical = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Chemicals", chemical, "Name"})));
            entryVals.address = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Addresses", address})));
            entryVals.field = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", field, "Name"})));
            entryVals.user = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", user, "Name"})));
            entryVals.weather = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Weather", weather})));
            entryVals.IdOrVal = true;
        }
        return entryVals;
    }
}
