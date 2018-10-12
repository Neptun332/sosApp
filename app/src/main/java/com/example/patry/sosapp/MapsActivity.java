package com.example.patry.sosapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.internal.maps.zzt;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Provider;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Handler handler = new Handler();
    public int MY_PERMISSIONS_REQUEST_SEND_SMS_AND_FINE_LOCATION;

    public boolean temp = true;
    public Location userLocation = new Location("");
    public float zoomlvl = 16;

    public double userLat;
    public double userLng;

   // Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Button button = findViewById(R.id.SendSMSButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //SmsManager.getDefault().sendTextMessage("+48510602840",null,"SMS send Test", null ,null );
            }
        });



        askForSendSMSPermission();
        processLocation();


        handler.post(periodicUpdate);

    }



        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }



    private Runnable periodicUpdate = new Runnable () {
        @Override
        public void run() {
            // scheduled another events to be in 10 seconds later
            handler.postDelayed(periodicUpdate, 10*1000 );
                    // below is whatever you want to do
            Button button = findViewById(R.id.SendSMSButton);
            if(temp){
            button.setText("@string/asdasd");
            temp = false;
            }
            else{
                button.setText("@string/weszłotu");
                temp =true;
            }

        }
    };

    public void askForSendSMSPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.SEND_SMS)) {


            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_SEND_SMS_AND_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            // Permission has already been granted

        }
    }




    public void processLocation(){

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
// Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                userLocation = location;
                LatLng userPosition = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
              //  marker.remove();
                mMap.addCircle(new CircleOptions().radius(1).center(userPosition));

                userLat = userPosition.latitude;
                userLng = userPosition.longitude;

                TextView temp = findViewById(R.id.textView);
                TextView temp2 = findViewById(R.id.textView2);
                temp.setText(userLat + "");
                temp2.setText(userLng + "");
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition,zoomlvl));

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);

    }


}
