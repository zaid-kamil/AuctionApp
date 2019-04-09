package com.example.majorproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class BidActivity extends AppCompatActivity {
    Button btnadd, btnsub, btnsubmit;
    EditText usernm, prodname, bidamt, finalbid;
    ImageView imageView;
    FirebaseUser user;
    FirebaseFirestore db;
    int bid;
    private String itemId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);
        btnadd = findViewById(R.id.btnincrease);
        btnsub = findViewById(R.id.btndecrease);
        btnsubmit = findViewById(R.id.submit);
        usernm = findViewById(R.id.etunm);
        prodname = findViewById(R.id.etprodname);
        bidamt = findViewById(R.id.etbid);
        finalbid = findViewById(R.id.etamt);
        imageView = findViewById(R.id.img);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        String title = intent.getStringExtra("title");
        String minibid = intent.getStringExtra("minibid");
        bid = Integer.parseInt(minibid);
        String url = intent.getStringExtra("url");
        FirebaseFirestore.getInstance().collection("auctions")
                .whereEqualTo("title", title)
                .whereEqualTo("minibid", minibid).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
                        itemId = snapshot.getId();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BidActivity.this, "not found", Toast.LENGTH_SHORT).show();
            }
        });

        bidamt.setText(minibid);
        prodname.setText(title);
        finalbid.setText(minibid);

        Glide.with(this).load(url).into(imageView);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            usernm.setText(user.getDisplayName());
        }


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernm.getText().toString();
                String prodTitle = prodname.getText().toString();
                final String bidamount = finalbid.getText().toString();
                String fbid = finalbid.getText().toString();
                if (username.length() == 0 || username.length() < 3 || username.length() > 15) {
                    usernm.setError("Enter Valid username");
                } else if (prodTitle.length() == 0 || prodTitle.length() < 3) {
                    prodname.setError("Enter Product title");
                } else if (bidamount.length() == 0) {
                    bidamt.setError("Enter Bid Amount");
                } else if (fbid.length() == 0) {
                    finalbid.setError("Enter your Bid");
                }
                if (username.length() > 0 && prodTitle.length() > 0 && bidamount.length() > 0 && fbid.length() > 0) {
                    db.collection("bidding").add(new biddingModel(
                            username, prodTitle, bidamount, fbid
                    )).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {

                            if (task.isSuccessful()) {
                                DocumentReference auction = FirebaseFirestore.getInstance().collection("auctions").document(itemId);
                                auction.update("minibid", bidamount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(BidActivity.this, "Successfully placed your Bid..", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else if (task.getException() != null) {
                                Toast.makeText(BidActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(BidActivity.this, "Unable To place Bid..", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bid++;
                finalbid.setText(String.valueOf(bid));
            }
        });
        btnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bid--;
                finalbid.setText(String.valueOf(bid));
            }
        });
    }
}


