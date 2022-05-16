package com.example.teamhoney_sprayerreporting_2022;

import java.util.ArrayList;
import java.util.Arrays;

public class userFetch {
    public String address;
    public String admin;
    public String email;
    public String name;
    public String password;

    public userFetch() {

    }

    public userFetch getUserVals() {
        userFetch entryVals = new userFetch();

        entryVals.admin = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Admin", admin})));
        entryVals.email = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Email", email})));
        entryVals.address = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Addresses", address})));
        entryVals.name = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Users", name, "Name"})));
        entryVals.password = MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Password", password})));

        return entryVals;
    }
}
