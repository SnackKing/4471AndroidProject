package com.example.alleg.a4471project;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    DatabaseReference mRootRef;
    String uid;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BACKGROUND", "FIRED");
        FirebaseApp.initializeApp(context);
        mRootRef =  FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getUid();

        if ( ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            GPSTracker gps = new GPSTracker(context);

            // Check if GPS enabled
            if(gps.canGetLocation()) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                mRootRef.child("Locations").child(uid).child(currentDateTimeString).setValue(new Location(String.valueOf(latitude),String.valueOf(longitude)));


            } else {
                // Can't get location.
                // GPS or network is not enabled.
                // Ask user to enable GPS/network in settings.
                gps.showSettingsAlert();
            }
        }


    }
}
