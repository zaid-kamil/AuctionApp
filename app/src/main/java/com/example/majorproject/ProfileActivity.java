package com.example.majorproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity {
    EditText uname,email,Phone,Loc,history;
    Button btnedit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        uname=findViewById(R.id.etUserName);
        email=findViewById(R.id.etmail);
        Phone=findViewById(R.id.etphoneno);
        Loc=findViewById(R.id.etLocation);
        history=findViewById(R.id.history);
        btnedit=findViewById(R.id.editbtn);


    }
}
