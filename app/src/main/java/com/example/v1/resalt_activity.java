package com.example.v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class resalt_activity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAyth;
    private DatabaseReference myRef = database.getReference();
    FirebaseUser user = mAyth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resalt);
        ListView listView = (ListView) findViewById(R.id.results);
        ArrayList <String> otv = new ArrayList<>();
        Bundle arguments = getIntent().getExtras();
        ArrayList<Boolean> answers_true = (ArrayList<Boolean>) arguments.get("email");
        myRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (int i = 0; i < (int) dataSnapshot.getChildrenCount(); i++) {
                    if (answers_true.get(i))
                    {
                        otv.add(i + ": Всё верно!");
                    }
                    else
                    {
                        //otv.add(i + ": Неправильный ответ. Верный ответ: ", dataSnapshot.child())
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}