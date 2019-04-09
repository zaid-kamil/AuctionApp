package com.example.majorproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Sign extends AppCompatActivity {
    EditText firstname;
    EditText lastname;
    EditText email1;
    EditText pass1;
    TextView alreadyaccount;
    FirebaseAuth firebaseAuth;
    ProgressBar myProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        myProgressBar = findViewById(R.id.pBar);
        firebaseAuth = FirebaseAuth.getInstance();
        firstname = findViewById(R.id.etfname);
        lastname = findViewById(R.id.etlname);
        email1 = findViewById(R.id.etemail1);
        pass1 = findViewById(R.id.etpass1);
        alreadyaccount = findViewById(R.id.alreadyAc);
        findViewById(R.id.btnsignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*validation*/
                final String fname = firstname.getText().toString();
                final String lname = lastname.getText().toString();
                if (fname.length() == 0 || fname.length() < 3) {
                    firstname.setError("Enter First name");
                } else if (lname.length() == 0 || lname.length() < 3) {
                    lastname.setError("Enter last name");
                } else if (pass1.getText().length() == 0) {
                    pass1.setError("Please Enter Password");
                } else if (email1.getText().length() == 0) {
                    email1.setError("Please Enter Email");
                }
                // else if(!email1.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]"))
                else if (!Patterns.EMAIL_ADDRESS.matcher(email1.getText().toString()).matches()) {
                    email1.setError("Invalid Email");
                } else {
                    myProgressBar.setVisibility(View.VISIBLE);
                    myProgressBar.setIndeterminate(true);
                    /*addoncomplete will add the email and password on completeing the task*/

                    firebaseAuth.createUserWithEmailAndPassword(email1.getText().toString(), pass1.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    myProgressBar.setIndeterminate(false);
                                    myProgressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        //signup successful
                                        Toast.makeText(getApplicationContext(), "Account created Successfully!!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Sign.this, Login_activity.class));
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        if (user != null) {
                                            user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(fname + " " + lname).build());
                                        }
                                        updateUI(user);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Account not Created due to" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }
            }
        });
        /*if already have an account perform onclick to next activity ie. Login*/
        alreadyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign.this, Login_activity.class));
            }
        });
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            if (user.isEmailVerified()) {
                startActivity(new Intent(Sign.this, Login_activity.class));
                finish();
            } else {
                user.sendEmailVerification();
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
