package com.geo.myapplication;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

/**
 * Created by zoom2manoj on 7/17/2017.
 */

public class GeoService extends Service{



    private static final String TAG = GeoService.class.getSimpleName();


    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    private static final int REQUEST_CHECK_SETTINGS = 0x1;


    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;


    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;


    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";


    private FusedLocationProviderClient mFusedLocationClient;


    private SettingsClient mSettingsClient;


    private LocationRequest mLocationRequest;


    private LocationSettingsRequest mLocationSettingsRequest;


    private LocationCallback mLocationCallback;


    private Location mCurrentLocation;

    public GeoService(){

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocationUpdates();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
              updateUI();

;
            }
        };
    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

     private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    private void startLocationUpdates() {



        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback, Looper.myLooper());

        updateUI();
    }

    private void stopLocationUpdates() {

       mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void updateUI() {

        if (mCurrentLocation!=null) {
            double value = mCurrentLocation.getLatitude();
            Log.d(TAG, "" + String.valueOf(value));
        }
    }
}



