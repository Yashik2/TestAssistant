package com.example.v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Tests extends AppCompatActivity {
    final Context context = this;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAyth;
    private DatabaseReference myRef = database.getReference();
    FirebaseUser user = mAyth.getInstance().getCurrentUser();
    TextInputLayout inputSearch;
    TextInputEditText edt_search;
    public ArrayList<String> right_answers = new ArrayList<>();
    public ArrayList<String> qestions = new ArrayList<>();
    public HashMap<Integer ,ArrayList<String>> answers_main = new HashMap<>();
    public int key;
    public ListView listView;
    public ArrayAdapter<String> adapter;
    private ArrayAdapter<String> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tests);
        super.onCreate(savedInstanceState);
        inputSearch = (TextInputLayout) findViewById(R.id.search);
        edt_search = (TextInputEditText) findViewById(R.id.edt_search);
        Bundle arguments = getIntent().getExtras();
        key = (int) arguments.get("key");

        listView = (ListView) findViewById(R.id.list);
        int n;
        ArrayList <String> values = new ArrayList<String>();
        ArrayList <String> val = new ArrayList<String>();
        String user_key;

        edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //Когда пользователь вводит какой-нибудь текст:
                Tests.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });


        if (key == 0)
        {
            user_key = user.getUid();
        }
        else
        {
            user_key = "all";
        }
        myRef.child(user_key).child("tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot childDataSnapshot :  dataSnapshot.getChildren()) {
                    values.add(String.valueOf(childDataSnapshot.getKey()));
                }
                HashMap <String, String> right_answer = (HashMap<String, String>) dataSnapshot.child("b3").child("qestion_" + 0+"").getValue();
                val.addAll(values);
                if (val.isEmpty())
                {
                    Intent i = new Intent(Tests.this, MainActivity.class);
                    startActivity(i);
                    Toast toast = Toast.makeText(getApplicationContext(), "У вас нет ни одного теста", Toast.LENGTH_LONG);
                    toast.show();
                }
                else
                {
                    apdate(val);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                item = val.get(position);
                if (key == 0)
                {
                    getRight(item, user.getUid());
                    getAnswers(item, user.getUid());
                    getQestions(item, user.getUid());
                }
                else
                {
                    getRight(item, "all");
                    getAnswers(item, "all");
                    getQestions(item, "all");
                }
            }
        });
    }
    void apdate (ArrayList <String> values)
    {
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
    }
    String item;


    public void getRight (String name, String key)
    {
        ArrayList<String> right_answers = new ArrayList<>();
        myRef.child(key).child("tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long n = dataSnapshot.child(name).getChildrenCount();
                for (long i = 0; i < n; i++)
                {
                    HashMap <String, String> right_answer = (HashMap<String, String>) dataSnapshot.child(name).child("qestion_" + i+"").getValue();
                    right_answers.add(right_answer.get("right_answer"));
                }
                right_help(right_answers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    public void getQestions (String name, String key)
    {
        ArrayList<String> qestions = new ArrayList<>();
        myRef.child(key).child("tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long n = dataSnapshot.child(name).getChildrenCount();
                for (long i = 0; i < n; i++)
                {
                    HashMap <String, String> qestion = (HashMap<String, String>) dataSnapshot.child(name).child("qestion_" + i+"").getValue();
                    qestions.add(qestion.get("qestion"));
                }
                qestoins_help(qestions);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    public void getAnswers (String name, String key)
    {
        HashMap<Integer, ArrayList<String>> answers = new HashMap<>();
        myRef.child(key).child("tests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long n = dataSnapshot.child(name).getChildrenCount();
                for (int i = 0; i < n; i++)
                {
                    ArrayList <String> ar = new ArrayList<>();
                    HashMap <String, String> answer = (HashMap<String, String>) dataSnapshot.child(name).child("qestion_" + i+"").getValue();
                    for (int b = 0; b < 4; b++)
                    {
                        ar.add(answer.get("answer_" + b+""));
                    }
                    Collections.shuffle(ar);
                    answers.put(i, ar);
                }
                getAnswers_help(answers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public void getAnswers_help(HashMap<Integer, ArrayList<String>> answers)
    {
        answers_main.putAll(answers);
    }
    public void qestoins_help(ArrayList<String> qestionss)
    {
        qestions.addAll(qestionss);
        Intent i = new Intent(Tests.this, Activity_of_passing_test.class);
        test1 test1= new test1 (right_answers, qestions, answers_main);
        i.putExtra(test1.class.getSimpleName(), test1);
        i.putExtra("name", item);
        if(key == 0)
        {
            i.putExtra("key", user.getUid()+"");
        }
        else
        {
            i.putExtra("key", "all");
        }
        startActivity(i);
    }
    public void right_help(ArrayList<String> right_answerss)
    {
        right_answers.addAll(right_answerss);
    }
}