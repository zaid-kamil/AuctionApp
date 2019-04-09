package com.example.majorproject;

import android.content.Intent;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ComplaintActivity extends AppCompatActivity {

    EditText feedback;
    Button btnsubmit;
    ProgressBar progressBar;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        feedback=findViewById(R.id.etfeedback);
        btnsubmit=findViewById(R.id.btnsubmit);
        progressBar=findViewById(R.id.progbar);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Feedback = feedback.getText().toString();
                if (Feedback.length() == 0 || Feedback.length() < 5) {
                    feedback.setError("Please Enter Your Feedback");
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(true);
                }
                if (Feedback.length() > 0) {
                    db.collection("customer_feedback").add(new ComplaintModel(Feedback)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            progressBar.setVisibility(View.GONE);
                            progressBar.setIndeterminate(false);
                            if (task.isSuccessful()) {
                                Toast.makeText(ComplaintActivity.this, "Successfully Done!!", Toast.LENGTH_LONG).show();
                                finish();
                            } else if (task.getException() != null) {
                                Toast.makeText(ComplaintActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                } else {
                    Toast.makeText(ComplaintActivity.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

}
