package com.example.flywillapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

public class FeedBackPoliceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_police);

        Button accept, reject, call;

        accept = findViewById(R.id.accept);
        reject = findViewById(R.id.reject);
        call = findViewById(R.id.call);


        Intent i = getIntent();
        if (i != null) {
            final String aid = i.getStringExtra("aid");

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                    Map map = new HashMap();

                    map.put("policemsg", "Path Cleared, please goahead!");
                    reference.updateChildren(map);
                    Toast.makeText(FeedBackPoliceActivity.this, "Feedback Sent", Toast.LENGTH_SHORT).show();

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("requests to police").child(aid);
                    Map map1 = new HashMap();

                    map1.put("status", "1");
                    reference1.updateChildren(map1);

                    Intent i = new Intent(FeedBackPoliceActivity.this, PublicMapActivity.class);
                    startActivity(i);
                    finish();
                }
            });

            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                    Map map = new HashMap();

                    map.put("policemsg", "Sorry, path cannot cleared");
                    reference.updateChildren(map);
                    Toast.makeText(FeedBackPoliceActivity.this, "Feedback Sent", Toast.LENGTH_SHORT).show();

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("requests to police").child(aid);
                    Map map1 = new HashMap();

                    map1.put("status", "2");
                    reference1.updateChildren(map1);

                    Intent i = new Intent(FeedBackPoliceActivity.this, PublicMapActivity.class);
                    startActivity(i);
                    finish();

                }
            });


            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseReference reference33 = FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                    Map map = new HashMap();

                    map.put("policemsg", "Called You");
                    reference33.updateChildren(map);

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("requests to police").child(aid);
                    Map map1 = new HashMap();

                    map1.put("status", "3");
                    reference1.updateChildren(map1);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(aid);

                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String n = dataSnapshot.child("phonenumber").getValue().toString();

                            Intent i = new Intent(Intent.ACTION_CALL);
                            i.setData(Uri.parse("tel:" + n));
                            if (ActivityCompat.checkSelfPermission(FeedBackPoliceActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
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
