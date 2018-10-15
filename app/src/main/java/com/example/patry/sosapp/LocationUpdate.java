package com.example.patry.sosapp;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class LocationUpdate implements LocationListener {


    private final Context context;
    private final Activity activity;



    private Location userLocation = new Location("");
    private LocationManager locationManager;

    private LatLng userPosition;
    private GoogleMap mMap;
    private float zoomlvl = 16;

    public LocationUpdate(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);


    }


    @Override
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        userLocation = location;
        userPosition = new LatLng(userLocation.getLatitude(),userLocation.getLongitude());
        drawUserPosition();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }


    public void drawUserPosition(){

        mMap.addCircle(new CircleOptions().radius(1).center(userPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition,zoomlvl));

    }

    public LatLng getUserPosition() {
        return userPosition;
    }

    public Location getUserLocation() {
        return userLocation;
    }
    public void StartUpdatingLocation(){
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
    }
    public void StopUpdatingLocation(){
        locationManager.removeUpdates(LocationUpdate.this);
    }
}
