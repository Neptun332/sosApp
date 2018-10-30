package com.example.patry.sosapp.Activities;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.patry.sosapp.DataBase.Entities.Tour;
import com.example.patry.sosapp.DataBase.Entities.TourCoords;
import com.example.patry.sosapp.DataBase.ViewModels.TourCoordsViewModel;
import com.example.patry.sosapp.DataBase.ViewModels.TourViewModel;
import com.example.patry.sosapp.Dialogs.TourNameDialog;
import com.example.patry.sosapp.R;
import com.example.patry.sosapp.Services.LocationUpdateService;
import com.example.patry.sosapp.TourAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TourNameDialog.TourNameDialogListener {

    public static final String CHANNEL_ID = "exampleServiceChannel";

    private GoogleMap mMap;
    private boolean startedUpdatingLocation = false;


    public int MY_PERMISSIONS_REQUEST_SEND_SMS_AND_FINE_LOCATION;



    private BroadcastReceiver broadcastReceiverPostion;
    private BroadcastReceiver broadcastReceiverList;

    public static boolean isServiceRunning =false;

    private AsyncTask asyncTask;

    private int ZOOM_LVL = 16;
    private LatLng userPosition;
    private Location userLocation;

    private long startDate;
    private Timestamp timestamp;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    private TourViewModel tourViewModel;
    private TourCoordsViewModel tourCoordsViewModel;
    private final TourAdapter adapter = new TourAdapter();


    private static Tour tour;
    private long currenTourId;
    private ArrayList<TourCoords> tourCoordsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createNotificationChannel();

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
                if (!startedUpdatingLocation) {
                    timestamp = new Timestamp(System.currentTimeMillis());
                    startDate = timestamp.getTime();
                    tour = new Tour("", startDate, sdf.format(timestamp));
                    tourViewModel.insert(tour);

                    checkAndStartService();
                    startButton.setText("Stop");
                    startedUpdatingLocation = true;
                    mMap.setMyLocationEnabled(true);



                } else {
                    currenTourId = tourViewModel.getCurrentTourId();
                    stopService(new Intent(MapsActivity.this, LocationUpdateService.class));
                    asyncTask.cancel(true);
                    startButton.setText("Start");
                    startedUpdatingLocation = false;

                    OpenSaveDialog();

                }

            }
        });

        final Button toursButton = findViewById(R.id.tours);
        toursButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, ToursActivity.class);
                startActivity(intent);
            }
        });

        askForSendSMSPermission();

        tourViewModel = ViewModelProviders.of(this).get(TourViewModel.class);

        tourCoordsViewModel = ViewModelProviders.of(this).get(TourCoordsViewModel.class);

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




    public void askForSendSMSPermission() {
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


    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiverPostion == null){
            broadcastReceiverPostion = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    userPosition =  (LatLng) intent.getExtras().get("coordinates");
                    Toast.makeText(MapsActivity.this,userPosition.latitude + " " + userPosition.longitude, Toast.LENGTH_LONG).show();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition,ZOOM_LVL));

                }
            };
        }

        registerReceiver(broadcastReceiverPostion,new IntentFilter("location_update"));

        if(broadcastReceiverList == null){
            broadcastReceiverList = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    tourCoordsList = intent.getParcelableArrayListExtra("coordList");
                }
            };
        }
        registerReceiver(broadcastReceiverList,new IntentFilter("location_list"));
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        Intent i = new Intent(getApplicationContext(),LocationUpdateService.class);
        stopService(i);

    }

             private void checkAndStartService() {
                 final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                 asyncTask = new AsyncTask<Void, Void, Boolean>() {
                     boolean isServiceRunning = false;

                     @Override
                     protected Boolean doInBackground(Void... params) {
                         for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                             if (LocationUpdateService.class.getName().equals(service.service.getClassName())) {
                                 isServiceRunning = true;
                                 break;
                             }
                         }
                         return isServiceRunning;
                     }

                     @Override
                     protected void onPostExecute(Boolean aBoolean) {
                         super.onPostExecute(aBoolean);
                         Log.i("onPostExecute", "Service running = " + aBoolean);
                         if (!aBoolean) {
                             if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                 startForegroundService(new Intent(MapsActivity.this, LocationUpdateService.class));
                             }
                             else{
                                 startService(new Intent(MapsActivity.this, LocationUpdateService.class));
                             }

                         }

                     }
                 }.execute();
             }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void OpenSaveDialog(){
        TourNameDialog tourNameDialog = new TourNameDialog();
        tourNameDialog.show(getSupportFragmentManager(),"Tour name dialog");
    }


    @Override
    public void SaveTourName(String tourName) {
            tour.setName(tourName);
            tour.setId((int)currenTourId);
            tourViewModel.update(tour);
            saveCoordsofTour();

    }

    public static Tour getTour() {
        return tour;
    }

    public  void saveCoordsofTour() {

        if (tourCoordsList != null) {
            for (int i = 0; i < tourCoordsList.size(); i++) {
                tourCoordsList.get(i).setTourId((int) currenTourId);
            }
            tourCoordsViewModel.insertList(tourCoordsList,(int) currenTourId);
        }
    }


}
