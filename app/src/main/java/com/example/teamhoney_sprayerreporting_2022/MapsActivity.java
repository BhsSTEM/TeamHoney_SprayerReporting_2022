package com.example.teamhoney_sprayerreporting_2022;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.teamhoney_sprayerreporting_2022.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener
{

    private int interval = 1000; // 1 sec
    private Handler handler;
    private int mode;

    private GoogleMap Map;
    private ActivityMapsBinding binding;
    private int availableFieldId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler = new Handler();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mode = 0;

        Intent entriesIntent = new Intent(this, SprayEntries.class);

        Button addFieldBtn = findViewById(R.id.addFieldBtn);
        Button deleteFieldBtn = findViewById(R.id.deleteFieldBtn);
        Button mapBackButton = findViewById(R.id.mapToEntriesBtn);

        addFieldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Button addFieldBtn = findViewById(R.id.addFieldBtn);
                EditText mapEditText = findViewById(R.id.mapEditText);
                if(mode != 0) {
                    mode = 0;
                    addFieldBtn.setText("Add field");
                    deleteFieldBtn.setText("Delete field");
                    MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Fields", Integer.toString(availableFieldId), "Name"})), mapEditText.getText().toString());
                    findAvailableFieldId();
                } else {
                    mode = 1;
                    addFieldBtn.setText("Save field");
                    deleteFieldBtn.setText("Delete field");
                    findAvailableFieldId();
                }
            }
        });

        deleteFieldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Button addFieldBtn = findViewById(R.id.addFieldBtn);
                EditText mapEditText = findViewById(R.id.mapEditText);
                if(mode != 0) {
                    mode = 0;
                    addFieldBtn.setText("Add field");
                    deleteFieldBtn.setText("Delete field");
                } else {
                    mode = 2;
                    addFieldBtn.setText("Add Field");
                    deleteFieldBtn.setText("Exit delete mode");
                }
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
                Map.clear();
                updatePolygons();
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

    private Polygon addPolygon(ArrayList<Double> points, boolean clickable, String tag, GoogleMap googleMap) {

        List<LatLng> latLngs = new ArrayList<LatLng>();

        for(int i = 0; i < points.size(); i += 2) {
            latLngs.add(new LatLng(points.get(i), points.get(i + 1)));
        }
        latLngs.add(new LatLng(points.get(0), points.get(1)));

        Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .addAll(latLngs));
        googleMap.setOnPolygonClickListener(this);
        polygon.setTag(tag);
        return polygon;
    }

    private ArrayList<Polygon> updatePolygons() {
        ArrayList<Polygon> polygons = new ArrayList<Polygon>();

        ArrayList<String> fieldIds = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields"})));
        for(int i = 0; i < fieldIds.size(); i++) {
            ArrayList<String> fieldDataKeys = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", fieldIds.get(i)})));
            ArrayList<Double> fieldCoords = new ArrayList<Double>();
            for(int j = 0; j < fieldDataKeys.size(); j++) {
                if(fieldDataKeys.get(j).length() >= 5 && fieldDataKeys.get(j).substring(0, 5).equals("coord")) {
                    fieldCoords.add(Double.parseDouble(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", fieldIds.get(i), fieldDataKeys.get(j)})))));
                }
            }
            if(fieldCoords.size() >= 4) {
                polygons.add(addPolygon(fieldCoords, Boolean.parseBoolean(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", fieldIds.get(i), "clickable"})))), fieldIds.get(i), Map));
            }
            else if(mode == 0) {
                MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Fields", Integer.toString(i)})), null);
            }
        }
        return polygons;
    }

    public void addField(LatLng point, String id, String name) {
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Fields", id, "coord" + Integer.toString(getCoordCount(id) + 10)})), Double.toString(point.latitude));
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Fields", id, "coord" + Integer.toString(getCoordCount(id) + 11)})), Double.toString(point.longitude));
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Fields", id, "Name"})), name);
    }

    public int getCoordCount(String fieldTag) {
        int coordCount = 0;
        ArrayList<String> fieldDataKeys = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", fieldTag})));

        for(int i = 0; i < fieldDataKeys.size(); i++) {

            if(fieldDataKeys.get(i).length() > 4 && fieldDataKeys.get(i).substring(0, 5).equals("coord")) {
                coordCount++;
            }
        }
        return coordCount;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Map = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng QC = new LatLng(41.53, -90.51);;
        Map.moveCamera(CameraUpdateFactory.newLatLng(QC));
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        startUpdating();

        Map.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                EditText addFieldText = findViewById(R.id.mapEditText);
                if(mode == 1) {
                    addField(point, Integer.toString(availableFieldId), addFieldText.getText().toString());
                }
            }
        });
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
        if(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", (String) polygon.getTag(), "Name"}))).equals("Africa")) {
            Log.d("Toto", "I bless the rains");
        }
        if(mode == 0) {
            EditText editText = findViewById(R.id.mapEditText);
            editText.setText(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", (String) polygon.getTag(), "Name"}))));
        }
        if(mode == 2) {
            MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Fields", (String) polygon.getTag()})), null);
        }
    }

    public void findAvailableFieldId() {
        ArrayList<String> fieldIds = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields"})));
        for(int i = 0; i < fieldIds.size() + 1; i++) {
            if(fieldIds != null && !fieldIds.contains(Integer.toString(i))) {
                availableFieldId = i;
                i = fieldIds.size();
            }
        }
    }

    public void goBack(View view) {
        super.finish();
    }
}