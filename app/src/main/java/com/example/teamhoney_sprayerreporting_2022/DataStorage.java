package com.example.teamhoney_sprayerreporting_2022;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class DataStorage
{
    ArrayList<ArrayList<String>> paths; //ArrayList of paths for each value, each path is an ArrayList of steps
    ArrayList<String> values;           //ArrayList of each value

    public DataStorage() {              //create the ArrayLists
        paths = new ArrayList<ArrayList<String>>();
        values = new ArrayList<String>();
    }

    public void addValue(ArrayList<String> path, String value) {    //Add the given data to the ArrayLists
            paths.add(path);
            values.add(value);
    }

    public ArrayList<String> getPathsAt(ArrayList<String> path) {   //returns a String[] of paths in a specific location
        ArrayList<String> comparePath;
        ArrayList<String> pathsInPath = new ArrayList<String>();

        if(path.get(0).equals("Root")) {
            for(int i = 0; i < paths.size(); i++) {
                if(!pathsInPath.contains(paths.get(i).get(0))) {
                    pathsInPath.add(paths.get(i).get(0));
                }
            }
        }
        else {
            for (int i = 0; i < paths.size(); i++) {
                if (path.size() < paths.get(i).size()) {
                    comparePath = new ArrayList<String>(paths.get(i).subList(0, path.size()));
                    if (comparePath.equals(path) && !pathsInPath.contains(paths.get(i).get(path.size()))) {
                        pathsInPath.add(paths.get(i).get(path.size()));
                    }
                }
            }
        }
        return pathsInPath;
    }

    public String getValueAt(ArrayList<String> path) {      //returns a String value from a specific path
        for(int i = 0; i < paths.size(); i++) {
            if (paths.get(i).equals(path)) {
                return values.get(i);
            }
        }
        return "";                                          //return "" if no value is found
    }

    public void clearData()                                 //clear the lists of all info
    {
        paths.clear();
        values.clear();
    }

    public void printData() {                               //prints out all the saved data
        for(int i = 0; i < paths.size(); i++) {
            Log.d((String.join("/", paths.get(i))), values.get(i));
        }
    }
}
