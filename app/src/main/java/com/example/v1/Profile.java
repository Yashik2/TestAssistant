package com.example.v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Profile extends AppCompatActivity {
    Button bt_sign_out;
    TextView tv_email;
    String email;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAyth;
    private DatabaseReference myRef = database.getReference();
    FirebaseUser user = mAyth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bt_sign_out = (Button) findViewById(R.id.bt_sign_out);
        tv_email = (TextView) findViewById(R.id.email);
        Bundle arguments = getIntent().getExtras();

        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tv_email.setText(dataSnapshot.child("email").getValue()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}

        });

        bt_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this, auth.class);
                startActivity(intent);
            }
        });
    }
}