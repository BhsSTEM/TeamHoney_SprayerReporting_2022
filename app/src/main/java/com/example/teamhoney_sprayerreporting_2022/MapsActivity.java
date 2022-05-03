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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener
{

    private int interval = 1000; // 1 sec
    private Handler handler;
    private int mode;

    private GoogleMap Map;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new Handler();

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mode = 0;
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
                UpdatePolygons();
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

    private ArrayList<Polygon> UpdatePolygons() {
        ArrayList<Polygon> polygons = new ArrayList<Polygon>();

        ArrayList<String> fieldTags = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields"})));
        for(int i = 0; i < fieldTags.size(); i++) {
            ArrayList<String> fieldDataKeys = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", fieldTags.get(i)})));
            ArrayList<Double> fieldCoords = new ArrayList<Double>();
            for(int j = 0; j < fieldDataKeys.size(); j++) {
                if(fieldDataKeys.get(j).substring(0, 5).equals("coord")) {
                    fieldCoords.add(Double.parseDouble(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", fieldTags.get(i), fieldDataKeys.get(j)})))));
                }
            }
            polygons.add(addPolygon(fieldCoords, Boolean.parseBoolean(MainActivity.dataBase.data.getValueAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", fieldTags.get(i), "clickable"})))), fieldTags.get(i), Map));
        }
        return polygons;
    }

    public void addField(MotionEvent event, String tag) {
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Fields", tag, "coord" + Integer.toString(getCoordCount(tag))})), Float.toString(event.getX()));
        MainActivity.dataBase.write(new ArrayList<String>(Arrays.asList(new String[]{"Fields", tag, "coord" + Integer.toString(getCoordCount(tag) + 1)})), Float.toString(event.getY()));
    }

    public int getCoordCount(String fieldTag) {
        int coordCount = 0;
        ArrayList<String> fieldDataKeys = MainActivity.dataBase.data.getPathsAt(new ArrayList<String>(Arrays.asList(new String[]{"Fields", fieldTag})));

        for(int i = 0; i < fieldDataKeys.size(); i++) {

            if(fieldDataKeys.get(i).substring(0, 5).equals("coord")) {
                coordCount++;
            }
        }
        return coordCount;
    }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            Map = googleMap;

            Map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    Log.d("j", latLng.toString());
                    //addField(latLng., "taco");
                }
            });


            // Add a marker in Sydney and move the camera
        LatLng QC = new LatLng(41.53, -90.51);
        Map.addMarker(new MarkerOptions().position(QC).title("Marker in QC"));
        Map.moveCamera(CameraUpdateFactory.newLatLng(QC));
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        startUpdating();

        //Polygon poly = addPolygon(new ArrayList<Double>(Arrays.asList(new Double[]{10.0, 10.0, 10.0, -10.0, -10.0, -10.0, -10.0, 10.0})), true, "Africa", googleMap);
    }

    @Override
    public void onPolygonClick(@NonNull Polygon polygon) {
        if (polygon.getTag().equals("Africa")) {
            Log.d("Toto", "I bless the rains");
        }
    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
        if (polyline.getTag().equals("Africa")) {
            Log.d("place", "AFRICA");
        }
    }

    /*@Override
    public boolean onTouch(View view, MotionEvent event) {
        Log.d("boo", "boo1");
        addField(event, "taco");
        return false;
    }*/
}