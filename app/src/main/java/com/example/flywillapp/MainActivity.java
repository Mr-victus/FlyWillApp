package com.example.flywillapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mAuthorized,mPublic,police;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuthorized = (Button) findViewById(R.id.Authorized);
        mPublic = (Button) findViewById(R.id.Public);
        police=findViewById(R.id.police);
        mAuthorized.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AuthorizedLoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PublicLoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,PoliceLoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
