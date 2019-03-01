package com.example.flywillapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        Button accept,reject,call;

        accept=findViewById(R.id.accept);
        reject=findViewById(R.id.reject);
        call=findViewById(R.id.call);


        Intent i=getIntent();
        if(i!=null) {
            final String aid = i.getStringExtra("aid");

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                    Map map=new HashMap();

                    map.put("publicmsg","Path Cleared, please goahead!");
                    reference.updateChildren(map);
                    Toast.makeText(FeedbackActivity.this, "Feedback Sent", Toast.LENGTH_SHORT).show();

                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("requests").child(aid);
                    Map map1=new HashMap();

                    map1.put("status","1");
                    reference1.updateChildren(map1);

                    Intent i=new Intent(FeedbackActivity.this,PublicMapActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                    Map map=new HashMap();

                    map.put("publicmsg","Sorry, path cannot cleared");
                    reference.updateChildren(map);
                    Toast.makeText(FeedbackActivity.this, "Feedback Sent", Toast.LENGTH_SHORT).show();

                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("requests").child(aid);
                    Map map1=new HashMap();

                    map1.put("status","2");
                    reference1.updateChildren(map1);

                    Intent i=new Intent(FeedbackActivity.this,PublicMapActivity.class);
                    startActivity(i);
                    finish();

                }
            });


            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseReference reference33= FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                    Map map=new HashMap();

                    map.put("publicmsg","Called You");
                    reference33.updateChildren(map);

                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("requests").child(aid);
                    Map map1=new HashMap();

                    map1.put("status","3");
                    reference1.updateChildren(map1);
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String n=dataSnapshot.child("phonenumber").getValue().toString();

                            Intent i=new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:"+n));
                            startActivity(i);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });






        }







    }
}
