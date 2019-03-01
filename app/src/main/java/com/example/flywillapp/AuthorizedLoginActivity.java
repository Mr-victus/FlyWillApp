package com.example.flywillapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AuthorizedLoginActivity extends AppCompatActivity {
    private EditText mEmail,mPassword;
    private Button mSignIn,mSignUp;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_login);

        mAuth = FirebaseAuth.getInstance();
        /*firebaseAuthListener = new  FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent=new Intent(AuthorizedLoginActivity.this,AuthorizedMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };*/

        mEmail=(EditText) findViewById(R.id.Email);
        mPassword=(EditText) findViewById(R.id.Password);
        mSignIn=(Button)findViewById(R.id.SignIn);
        mSignUp=(Button)findViewById(R.id.SignUp);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email= mEmail.getText().toString();
                final String password= mPassword.getText().toString();

                Intent i=new Intent(AuthorizedLoginActivity.this,AuthorizedSignupActivity.class);
                startActivity(i);
                /*mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(AuthorizedLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(AuthorizedLoginActivity.this,"sign up error",Toast.LENGTH_SHORT).show();
                        }else{


                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(user_id);
                            Map map=new HashMap<>();
                            Toast.makeText(AuthorizedLoginActivity.this,"DONNEE",Toast.LENGTH_SHORT).show();
                            map.put("status","true");
                            current_user_db.updateChildren(map);
                            Intent intent=new Intent(AuthorizedLoginActivity.this,AuthorizedMapActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                });*/
            }
        });
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email= mEmail.getText().toString();
                final String password= mPassword.getText().toString();
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(AuthorizedLoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(AuthorizedLoginActivity.this, "sign in error", Toast.LENGTH_SHORT).show();
                        }
                        else {


                            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(mAuth.getCurrentUser().getUid());
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        Intent intent=new Intent(AuthorizedLoginActivity.this,AuthorizedMapActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {

                                        Toast.makeText(AuthorizedLoginActivity.this,"Signin Error",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
       // mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
