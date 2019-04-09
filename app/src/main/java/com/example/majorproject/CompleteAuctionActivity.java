package com.example.majorproject;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CompleteAuctionActivity extends AppCompatActivity {

    EditText username, address, mail, phno, state, city, pin;
    Button conti;
    ProgressBar pbar;
    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_auction);

        username = findViewById(R.id.etuname);
        address = findViewById(R.id.etaddress);
        mail = findViewById(R.id.etmail1);
        phno = findViewById(R.id.etphone);
        state = findViewById(R.id.etstate);
        city = findViewById(R.id.etcity);
        pin = findViewById(R.id.etpin);
        pbar = findViewById(R.id.progressbr);
        conti = findViewById(R.id.btncontinue);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (mail != null) {
            mail.setText(user.getEmail());
        }
        username.setText(user.getDisplayName());


        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make progressbar visible
                pbar.setVisibility(View.VISIBLE);
                pbar.setIndeterminate(true);
                final String uname = username.getText().toString();
                final String addr = address.getText().toString();
                String email = mail.getText().toString();
                final String phone = phno.getText().toString();
                final String etstate = state.getText().toString();
                final String etcity = city.getText().toString();
                final String pincode = pin.getText().toString();
                if (uname.length() == 0 || uname.length() < 3 || uname.length() > 30) {
                    username.setError("Please Enter Valid Username");
                } else if (addr.length() == 0 || addr.length() < 3) {
                    address.setError("Please Enter your Address ");
                } else if (email.length() == 0 || email.length() < 5) {
                    mail.setError("Please Enter Valid E-mail");
                } else if (phone.length() == 0 || phone.length() < 12) {
                    phno.setError("Please Enter Valid Phone Number");
                } else if (etstate.length() == 0 || etstate.length() < 3) {
                    state.setError("Please Enter Vaild State");
                } else if (etcity.length() == 0 || etcity.length() < 2) {
                    city.setError("Please Enter City");
                } else if (pincode.length() == 0 || pincode.length() > 6 || pincode.length() < 6) {
                    pin.setError("Please Enter Valid Pincode");
                }

                if (uname.length() > 0
                        && addr.length() > 0
                        && email.length() > 0
                        && phone.length() > 0
                        && etstate.length() > 0
                        && etcity.length() > 0
                        && pincode.length() > 0) {
                    db.collection("complete_auction")
                            .add(new CompleteAuctionModel(
                                    uname,
                                    addr,
                                    user.getEmail(),
                                    user.getPhoneNumber(),
                                    etstate,
                                    etcity,
                                    pincode,
                                    user.getUid()))
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    pbar.setIndeterminate(false);
                                    pbar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(CompleteAuctionActivity.this, "Success", Toast.LENGTH_LONG).show();
                                        composeEmail(addr, phone, etstate, etcity, pincode);
                                        finish();
                                    } else if (task.getException() != null) {
                                        Toast.makeText(CompleteAuctionActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(CompleteAuctionActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    public void composeEmail(String addr, String phone, String etstate, String etcity, String pincode) {

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String mailContent = String.format("address: %s\n phone: %s\n etstate: %s\n etcity: %s\n pincode:", addr, phone, etstate, etcity, pincode);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, "tanyarai532@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "bidding info");
        intent.putExtra(Intent.EXTRA_TEXT, mailContent);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}