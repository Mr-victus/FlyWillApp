package com.example.flywillapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        Button accept,reject,call;

        accept=findViewById(R.id.accept);
        reject=findViewById(R.id.reject);
        call=findViewById(R.id.call);

        auth=FirebaseAuth.getInstance();

        Intent i=getIntent();
        if(i!=null) {
            final String aid = i.getStringExtra("aid");

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            int count=Integer.parseInt(dataSnapshot.child("positive").getValue().toString());

                            count=count+1;
                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                            Map map=new HashMap();

                            map.put("publicmsg","Path Cleared, please goahead!");
                            map.put("positive",count);
                            reference.updateChildren(map);
                            Toast.makeText(FeedbackActivity.this, "Feedback Sent", Toast.LENGTH_SHORT).show();




                            DatabaseReference reference2=FirebaseDatabase.getInstance().getReference().child("Users").child("Public").child(auth.getCurrentUser().getUid()).child("status");
                            Map map2=new HashMap();
                            map2.put(aid,"1");
                            reference2.updateChildren(map2);

                            Intent i=new Intent(FeedbackActivity.this,PublicMapActivity.class);
                            startActivity(i);
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            int count=Integer.parseInt(dataSnapshot.child("negative").getValue().toString());

                            count=count+1;

                            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                            Map map=new HashMap();

                            map.put("publicmsg","Sorry, path cannot cleared");
                            map.put("negative",count);
                            reference.updateChildren(map);
                            Toast.makeText(FeedbackActivity.this, "Feedback Sent", Toast.LENGTH_SHORT).show();




                            DatabaseReference reference2=FirebaseDatabase.getInstance().getReference().child("Users").child("Public").child(auth.getCurrentUser().getUid()).child("status");
                            Map map2=new HashMap();
                            map2.put(aid,"2");
                            reference2.updateChildren(map2);

                            Intent i=new Intent(FeedbackActivity.this,PublicMapActivity.class);
                            startActivity(i);
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            });


            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseReference reference33= FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                    Map map=new HashMap();

                    map.put("publicmsg","Called You");
                    reference33.updateChildren(map);


                    DatabaseReference reference2=FirebaseDatabase.getInstance().getReference().child("Users").child("Public").child(auth.getCurrentUser().getUid()).child("status");
                    Map map2=new HashMap();
                    map2.put(aid,"3");
                    reference2.updateChildren(map2);
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
