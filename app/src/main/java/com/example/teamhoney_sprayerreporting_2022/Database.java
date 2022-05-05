package com.example.teamhoney_sprayerreporting_2022;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//new ArrayList<String>(Arrays.asList(new String[]{"Crops"})); allows creation of an arbitrary arraylist for passing in a path
/*
Creates a listener to automatically update local storage to reflect the realtime database
stores the info in a DataStorage object
Also handles writing new data to the database which is automatically loaded back to local storage
 */

/*      Example database usage
        private Database base;
        base = new Database();
        base.write(new ArrayList<String>(Arrays.asList(new String[]{"Crops", "0"})), "Corn");
        base.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Crops", "0"})));
        base.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Users"})));
 */
public class Database {
    private FirebaseDatabase database;  //The actual database object
    private DatabaseReference ref;      //allows for navigating the tree and read and write access
    public DataStorage data;            //stores the data read from the database. Allows for value retrieval and path structure info
    public boolean updated;             //set to true whenever a database change happens, used to determine when to update gui stuff in the app

    public Database() {
        database = FirebaseDatabase.getInstance();      //create a database object and reference to the root
        ref = database.getReference();
        data = new DataStorage();                    //create a dataStorage object
        updated = false;

        ValueEventListener postListener = new ValueEventListener() {    //create a listener to detect changes to the realtime database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {       //runs whenever something changes in the database
                data.clearData();                                       //clear out out-of-date data
                updateStorage(dataSnapshot);                            //read data from the dataSnapshot given by the listener
                updated = true;                                         //updateStorage cycles through every path and writes the data to storage
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {      //runs if something cancels the query
                Log.d("tag", "failed to retrieve");
            }
        };
        ref.addValueEventListener(postListener);                        //run the listener
    }

    private void updateStorage(DataSnapshot dataSnapshot) {     //recursively navigates the whole json tree in dataSnapshot and writes it into data
        if(dataSnapshot.getChildrenCount() == 0) {              //if there is no child path
            ArrayList<String> path = new ArrayList<String>(Arrays.asList(dataSnapshot.getRef().toString().substring(55).split("/", -1)));   //record the path as an ArrayList
            data.addValue(path, dataSnapshot.getValue().toString());  //write the path and value into data
        }
        else {                                                          //if there is at least one child path
            for (DataSnapshot child : dataSnapshot.getChildren()) {     //for each path, call updateStorage
                updateStorage(child);
            }
        }
    }

    //either edit a value or add a new tag-value entry. To modify or add a value, pass in the path and new value
    //to delete a value or path, pass the the path and null for the value. If there are no values in a path, the path will be deleted
    public void write(ArrayList<String> path, String value) {
        DatabaseReference tempRef = ref;            //a temporary reference to the specific path
        for(int i = 0; i < path.size(); i++) {      //cycle through each path step until arriving at the desired location
            tempRef = tempRef.child(path.get(i));
        }
        tempRef.setValue(value);                    //write the value to the specified path
    }

    public void setUpdated(boolean in) {            //can set to true to force-update stuff in the app
        updated = in;                               //should set to false after all of the code to check for an update has run once
    }
}
