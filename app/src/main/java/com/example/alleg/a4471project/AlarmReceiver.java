package com.example.alleg.a4471project;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Debug;
import android.os.SystemClock;
import android.provider.CallLog;
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
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED){
            getCallDetails(context);
        }
        if ( ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
          getLocationDetails(context);
        }



    }
    private  void getLocationDetails(Context context){
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
    private void getCallDetails(Context context) {

        StringBuffer sb = new StringBuffer();
        if ( ContextCompat.checkSelfPermission( context, Manifest.permission.READ_CALL_LOG ) == PackageManager.PERMISSION_GRANTED ) {
            Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                    null, null, null);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
            sb.append("Call Details :");
            while (managedCursor.moveToNext()) {
                String phNumber = managedCursor.getString(number);
                String callType = managedCursor.getString(type);
                String callDate = managedCursor.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = managedCursor.getString(duration);
                String dir = null;
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                Call_Log call = new Call_Log(phNumber, callDayTime, callDuration, dir);
                Log.d("BACKGROUND", "CREATED CALL");
                mRootRef.child("Contacts").child(uid).child(callDayTime.toString()).setValue(call);

            }
            managedCursor.close();
        }

    }
}
