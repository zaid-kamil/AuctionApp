package com.example.majorproject;

import android.media.Image;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AddProduct extends AppCompatActivity {
    TextInputEditText productname,discript,price;
    Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        productname=findViewById(R.id.Pdname);
        discript=findViewById(R.id.PdDiscription);
        price=findViewById(R.id.Pdprice);
        save=findViewById(R.id.btnsave);

    }
}
