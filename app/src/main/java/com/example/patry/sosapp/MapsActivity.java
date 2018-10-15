package com.example.patry.sosapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private LocationUpdate locationUpdate;

    private GoogleMap mMap;
    private boolean startedUpdatingLocation = false;

    public Handler handler = new Handler();
    public int MY_PERMISSIONS_REQUEST_SEND_SMS_AND_FINE_LOCATION;

    public boolean temp = true;

    public int sendSMSEveryInSeconds = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);





        final Button settingsButton = findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        final Button startButton = findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!startedUpdatingLocation) {
                    locationUpdate = new LocationUpdate(MapsActivity.this, MapsActivity.this);
                    locationUpdate.setmMap(mMap);
                    locationUpdate.StartUpdatingLocation();
                    startButton.setText("Stop");
                    startedUpdatingLocation = true;
                }
                else{
                    locationUpdate.StopUpdatingLocation();
                    startButton.setText("Start");
                    startedUpdatingLocation = false;
                }

            }
        });

        askForSendSMSPermission();
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
            handler.postDelayed(periodicUpdate, sendSMSEveryInSeconds*1000 );
                    // below is whatever you want to do
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
        if( preferences.getBoolean("EnableSMSSend",false)==true && preferences.getString("phoneNumber","")!= "") {
            Location userLocation = locationUpdate.getUserLocation();

            SmsManager.getDefault().sendTextMessage(
                    "+48"+preferences.getString("phoneNumber",""),
                    null,
                    "Latitude: "+userLocation.getLatitude() + " Longitude: " + userLocation.getLongitude() + " Time: " + ConvertTimeToSimpleDateFormat(userLocation.getTime()),
                    null,
                    null);
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
    public String ConvertTimeToSimpleDateFormat(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a");
        return sdf.format(date);
    }

}
