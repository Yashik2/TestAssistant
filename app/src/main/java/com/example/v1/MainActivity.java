package com.example.v1;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    final int REQ_C = 0;
    TextView login_tv;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAyth;
    private DatabaseReference myRef = database.getReference();
    FirebaseUser user = mAyth.getInstance().getCurrentUser();
    private AppBarConfiguration mAppBarConfiguration;
    Button bt;
    ImageView imageView;
    auth auth = new auth();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.test);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);
        View navHeader = navigationView.getHeaderView(0);
        login_tv = (TextView) navHeader.findViewById(R.id.login_tv);
        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                login_tv.setText(dataSnapshot.child("login").getValue()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.topAppBar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.item1) {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
        } else if (id == R.id.item2) {
            Intent i = new Intent(MainActivity.this, Making_test.class);
            startActivity(i);

        } else if (id == R.id.item3) {
            Intent i = new Intent(MainActivity.this, Tests.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.putExtra("key", 0);
            startActivity(i);
        } else if (id == R.id.item4) {
            //тестовый тест
            Intent i = new Intent(MainActivity.this, Tests.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.putExtra("key", 1);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}