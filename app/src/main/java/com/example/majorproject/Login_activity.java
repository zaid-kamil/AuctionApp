package com.example.majorproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_activity extends AppCompatActivity {
    EditText email;
    EditText pass;
    TextView forgotpassbtn,newacbtn;
    FirebaseAuth firebaseAuth;
    ProgressBar myProgressBar1;

    /*performing functions of onclick in xml file ie. for forgot password*/
    public void forgotPassword(View v){
        startActivity(new Intent(Login_activity.this,ForgotPassword.class));
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        firebaseAuth=FirebaseAuth.getInstance();
        myProgressBar1=findViewById(R.id.pBar1);
        email=findViewById(R.id.etemail);
        pass=findViewById(R.id.etpass);
        forgotpassbtn=findViewById(R.id.btnforgotpass);
        newacbtn=findViewById(R.id.btnnewac);
        findViewById(R.id.btnlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*password and email validation*/
                if (pass.getText().length()==0)
                {
                    pass.setError("Please Enter Password");
                }
                else if(email.getText().length()==0)
                {
                    email.setError("Please Enter Email");
                }


                /*when having entered the email&password progress bar will be shown*/
                else{
                        myProgressBar1.setVisibility(View.VISIBLE);
                        myProgressBar1.setIndeterminate(true);

                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        /*having completed with successful login progress bar will end */
                        myProgressBar1.setIndeterminate(false);
                        myProgressBar1.setVisibility(View.GONE);
                        if(task.isSuccessful()){
                            //sign in successfull
                             Toast.makeText(getApplicationContext(),"Verify & SignIn ",Toast.LENGTH_SHORT).show();
                            FirebaseUser  user=firebaseAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            //sign in unsuccessfull
                            /*+task= means if there is a problem to sign in the problem will be displayed*/
                            Toast.makeText(getApplicationContext(),"SignIn Unsuccessful"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });}


            }
        });
        /*if don't have an account perform onclick to sign up to next activity ie. Sign*/
        newacbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Login_activity.this,Sign.class);
                startActivity(in);
            }
        });
    }

    public void updateUI(FirebaseUser user){
        if (user!=null){
            if (user.isEmailVerified()){
                startActivity(new Intent(Login_activity.this,PhoneAuthAcitivity.class));
                finish();
            }
            else{
                user.sendEmailVerification();
                //show user a msg that verfication is sent to mail
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
