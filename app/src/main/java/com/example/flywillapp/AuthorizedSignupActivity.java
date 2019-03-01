package com.example.flywillapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AuthorizedSignupActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_signup);

        auth=FirebaseAuth.getInstance();
        final EditText email,password,eid,phone;
        Button btn;

        email=findViewById(R.id.Email);
        password=findViewById(R.id.Password);
        btn=findViewById(R.id.SignUp);

        eid=findViewById(R.id.Eid);
        phone=findViewById(R.id.phone);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                auth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(auth.getCurrentUser().getUid());
                            Map map=new HashMap();
                            map.put("status","true");
                            map.put("email",email.getText().toString());
                            map.put("EID",eid.getText().toString());
                            map.put("phonenumber",phone.getText().toString());


                            reference.updateChildren(map);

                            Intent i=new Intent(AuthorizedSignupActivity.this,AuthorizedMapActivity.class);
                            startActivity(i);
                        }


                    }
                });

            }
        });
    }
}
