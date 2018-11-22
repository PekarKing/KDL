package com.android.kdl;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainScreen extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DrawerLayout drawerLayout;
    private String email = "";
    private String uId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent)); //status bar or the time bar at the top
        }

        getData();
        initNavigationView();

        Button logOut = findViewById(R.id.logOut);
        logOut.setOnClickListener(this);
        Button toRes = findViewById(R.id.toResults);
        toRes.setOnClickListener(this);
        Button toDialog = findViewById(R.id.toDialog);
        toDialog.setOnClickListener(this);
        Button toOrder = findViewById(R.id.toOrder);
        toOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.logOut:
                auth.signOut();
                intent = new Intent(MainScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.toResults:
//                final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//                DatabaseReference ref = database.child("users");
//
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        email = String.valueOf(dataSnapshot.child(auth.getUid()).child("email").getValue());
//                        uId = String.valueOf(dataSnapshot.child(auth.getUid()).getKey());
//                        TextView eTV = findViewById(R.id.email);
//                        TextView uIdTV = findViewById(R.id.uID);
//                        eTV.setText(email);
//                        uIdTV.setText(uId);
//
//                    }
//
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
//                        Toast.makeText(MainScreen.this, "NET", Toast.LENGTH_SHORT).show();                    }
//                });
                break;
            case R.id.toDialog:
                intent = new Intent(MainScreen.this, ChatActivity.class);
                startActivity(intent);
                Toast.makeText(this, "dialog", Toast.LENGTH_SHORT).show();
                break;
            case R.id.toOrder:
                Toast.makeText(this, "order", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getData() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = String.valueOf(dataSnapshot.child(auth.getUid()).child("email").getValue());
                uId = String.valueOf(dataSnapshot.child(auth.getUid()).getKey());
                TextView tv1 = findViewById(R.id.navEmail);
                TextView tv2 = findViewById(R.id.navUID);
                tv1.setText(email);
                tv2.setText(uId);
            }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainScreen.this, "NET", Toast.LENGTH_SHORT).show();                    }
        });
    }

    private void initNavigationView() {
        drawerLayout = findViewById(R.id.drawMain);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.view_navigation_open, R.string.view_navigation_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.navMain);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                return false;
            }
        });
    }
}
