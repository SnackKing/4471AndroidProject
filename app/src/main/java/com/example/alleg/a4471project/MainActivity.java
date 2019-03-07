package com.example.alleg.a4471project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int PERMISSIONS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        setContentView(R.layout.activity_main);
    }
    public void checkPermissions(){
        String[] requests = {
                android.Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_FINE_LOCATION
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
}
