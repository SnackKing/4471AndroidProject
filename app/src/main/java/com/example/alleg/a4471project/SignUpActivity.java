package com.example.alleg.a4471project;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference database;

    // UI references.
    private EditText mNameView;
    private EditText mPhoneView;
    private EditText mDobView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private Button signup;
    private String newUserNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
        }

        // Set up the login form.
        mEmailView = findViewById(R.id.email);

        mPasswordView = findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_form);
        mNameView = findViewById(R.id.name);
        mPhoneView = findViewById(R.id.phone);
        mDobView = findViewById(R.id.dob);
        signup = findViewById(R.id.email_sign_up_button);
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addUser(mEmailView.getText().toString(), mPasswordView.getText().toString(), mPhoneView.getText().toString(), mNameView.getText().toString(), mDobView.getText().toString());
            }
        });

    }
    public void addUser(String email, final String password, final String phone, final String name, final String dob){
        if(validate(email,password,phone, name, dob)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                writeNewUser(user.getUid(), dob, name, phone, password);
                                newUserNum = phone;
                                deleteInDesiredIfPresent();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("SIGNUP", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }
    }
    public void deleteInDesiredIfPresent(){
        ValueEventListener signup = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot desired: dataSnapshot.getChildren()){
                    String phone = desired.getKey();
                    if(newUserNum.equals(phone)){
                        database.child("Desired").child(phone).removeValue();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        database.child("Desired").addValueEventListener(signup);

    }
    private boolean validate(String email, String password, String phone, String name, String dob){
        boolean isValid = true;
        if(name.length() == 0){
            mNameView.setError("Name can't be empty");
            isValid = false;
        }
        if(phone.length() == 0){
            mPhoneView.setError("Phone can't be empty");
            isValid = false;
        }
        if(dob.length() == 0){
            mDobView.setError("Date of birth can't be empty");
            isValid = false;
        }
        if(email.length() == 0){
            mEmailView.setError("Email can't be empty");
            isValid = false;
        }
        if(password.length() == 0){
            mPasswordView.setError("Password can't be empty");
            isValid = false;
        }
        return isValid;

    }

    private void writeNewUser(String userId, String dateOfBirth, String name, String phoneNumber, String password){
        LocalDateTime joinDate = LocalDateTime.now();
        User user = new User(dateOfBirth, name, phoneNumber, joinDate, userId, password);

        database.child("users").child(userId).setValue(user);
    }


}
