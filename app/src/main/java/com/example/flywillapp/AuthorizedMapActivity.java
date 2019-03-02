package com.example.flywillapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

//, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener
public class AuthorizedMapActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    FirebaseAuth auth;
    LocationRequest mLocationRequest;

    Marker marker;
    int c=1;

    private Button mLogout,request,police;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorized_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        auth=FirebaseAuth.getInstance();
        mLogout = (Button)findViewById(R.id.logout);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AuthorizedMapActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);


        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if(marker!=null) {
                    marker.remove();
                }

                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(auth.getCurrentUser().getUid());
                Map map=new HashMap();
                map.put("lat",""+latLng.latitude);
                map.put("lon",""+latLng.longitude);
                reference.updateChildren(map);
                if(c==1) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    c=c+1;
                }
                marker=mMap.addMarker(new MarkerOptions().position(latLng).title("my place").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
        });


        DatabaseReference reference2=FirebaseDatabase.getInstance().getReference().child("Users").child("Public");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {

                    //Toast.makeText(PublicMapActivity.this,snapshot.child("lat").getValue().toString(),Toast.LENGTH_SHORT).show();

                    LatLng latLng=new LatLng(Double.parseDouble(snapshot.child("lat").getValue().toString()),Double.parseDouble(snapshot.child("lon").getValue().toString()));

                    mMap.addMarker(new MarkerOptions().position(latLng).title("Traffic "));




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference reference3=FirebaseDatabase.getInstance().getReference().child("Users").child("Police");
        reference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {

                    //Toast.makeText(PublicMapActivity.this,snapshot.child("lat").getValue().toString(),Toast.LENGTH_SHORT).show();

                    LatLng latLng=new LatLng(Double.parseDouble(snapshot.child("lat").getValue().toString()),Double.parseDouble(snapshot.child("lon").getValue().toString()));

                    mMap.addMarker(new MarkerOptions().position(latLng).title("Police "));




                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        request=findViewById(R.id.request);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("requests").child(auth.getCurrentUser().getUid());
                Map map=new HashMap();
                map.put("msg","Please Clear the Path");
                map.put("status","0");
                reference.updateChildren(map);
                Toast.makeText(AuthorizedMapActivity.this, "Notification Sent", Toast.LENGTH_SHORT).show();

            }
        });
        police=findViewById(R.id.police);

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("requests to police").child(auth.getCurrentUser().getUid());
                Map map=new HashMap();
                map.put("msg","Please Clear the Path");
                map.put("status","0");
                reference.updateChildren(map);
                Toast.makeText(AuthorizedMapActivity.this, "Notification Sent", Toast.LENGTH_SHORT).show();

            }
        });


        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child("Authorized").child(auth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {





                    if(dataSnapshot.child("publicmsg").getValue()!=null) {

                        Toast.makeText(AuthorizedMapActivity.this, dataSnapshot.child("publicmsg").getValue().toString(), Toast.LENGTH_LONG).show();

                    }
                    if(dataSnapshot.child("policemsg").getValue()!=null) {

                        Toast.makeText(AuthorizedMapActivity.this, dataSnapshot.child("policemsg").getValue().toString(), Toast.LENGTH_LONG).show();

                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new  GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

         LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

         mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
         mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

         String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AuthorizedAvailable");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.setLocation(userId, new GeoLocation(location.getLatitude(),location.getLongitude()));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AuthorizedAvailable");

        GeoFire geoFire = new GeoFire(ref);
        geoFire.removeLocation(userId);
    }*/
}
