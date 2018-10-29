package com.example.patry.sosapp.Services;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.patry.sosapp.Activities.MapsActivity;
import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.Entities.TourCoords;
import com.example.patry.sosapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LocationUpdateService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener  {

    private String TAG = "LocationUpdateService";

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;

    private Context context;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private ArrayList<TourCoords> tourCoordsList = new ArrayList<>();
    private Tour tour;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
    @Override
    public void onCreate() {
        tour = MapsActivity.getTour();
        super.onCreate();
        MapsActivity.isServiceRunning = true;
    }

    @Override
    public void onDestroy() {

        Intent i = new Intent("location_list");
        i.putExtra("coordList",  tourCoordsList );
        sendBroadcast(i);

        MapsActivity.isServiceRunning = false;
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        Log.i(TAG, "onStartCommand");
        if (checkPermsion(context)) {
            setupLocationService(context);
        }

        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, MapsActivity.CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_gps_fixed_black_24dp)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return Service.START_STICKY;
    }

    public boolean checkPermsion(Context context) {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void setupLocationService(Context context) {
        if (checkPlayServices()) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            createLocationRequest();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest().create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connected to onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connected to onConnectionFailed");
    }

    @Override
    public void onLocationChanged(Location location) {

        Location userLocation = location;
        if (userLocation != null) {
            Intent i = new Intent("location_update");
            i.putExtra("coordinates",  new LatLng(location.getLatitude(),location.getLongitude()) );
            sendBroadcast(i);
            long time = location.getTime();
            TourCoords tourCoords = new TourCoords(
                    tour.getId(),
                    location.getLatitude(),
                    location.getLongitude(),
                    time,
                    sdf.format(time));
            tourCoordsList.add(tourCoords);
        }



    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    private void startLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
//            if (Prefs.getUserLat(context).trim().length() > 0 && Prefs.getUserLong(context).trim().length() > 0) {
//            } else {
//                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                        mGoogleApiClient);
//                if (mLastLocation != null) {
//                    Prefs.setUserLat(context, String.valueOf(mLastLocation.getLatitude()));
//                    Prefs.setUserLong(context, String.valueOf(mLastLocation.getLongitude()));
//                }
//            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }
}



