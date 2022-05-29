package com.example.v1;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class Activity_of_passing_test extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAyth;
    private DatabaseReference myRef = database.getReference();
    FirebaseUser user = mAyth.getInstance().getCurrentUser();

    ArrayList <String> otv = new ArrayList<>();

    public int QestionsCount;

    public ArrayList<Boolean> answers_true = new ArrayList<>();
    Button bt_exit, bt_next;
    TextView tv_q;
    RadioGroup radioGroup;
    int j = 0;
    ListView listView;
    public int res = 0;
    int q = 0;
    //public ListView listView;
    private ArrayAdapter<String> mAdapter;
    public test1 test;
    String name;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_of_passing_test);
        Bundle arguments = getIntent().getExtras();
        test = (test1) arguments.getSerializable(test1.class.getSimpleName());
        name = arguments.getString("name");
        key = arguments.getString("key");
        listView = (ListView) findViewById(R.id.listView);
        // создаем объект теста
        //test = new Test();
        Making_test mk = new Making_test();
        // устанавливаем название теста
        tv_q = findViewById(R.id.tv_q);
        tv_q.setText(test.getQestions().get(0));



        radioGroup = (RadioGroup) findViewById(R.id.button_layout);
        // создаем кнопки по количеству ответов в вопросе
        add_button(q);

        final Context context = this;
        bt_exit = (Button) findViewById(R.id.bt_end);
        bt_next = (Button) findViewById(R.id.bt_next);
        // нажание на кнопку "Закончить"
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(q); // проверка правильности ответа
                // создание намерения, для отправки результата в MainActivity
                myRef.child(key).child("tests").child(name).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        setQestionsCount((int) dataSnapshot.getChildrenCount());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}

                });
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                final EditText input = new EditText(context);
                alert
                        .setMessage("Ваш результат: "+res+"/"+answers_true.size())
                        .setIcon(R.drawable.test_icon_foreground)
                        .setPositiveButton("Посмотреть результаты", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Закрываем окно


                                bt_next.setVisibility(View.INVISIBLE);
                                bt_exit.setVisibility(View.INVISIBLE);
                                radioGroup.setVisibility(View.INVISIBLE);
                                tv_q.setVisibility(View.INVISIBLE);
                                result_list(name);
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("закончить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Закрываем окно
                                Intent i = new Intent(Activity_of_passing_test.this, MainActivity.class);
                                startActivity(i);
                            }
                        });
                alert.show();
            }
        });

        // нажание на кнопку "Следующий"
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(q);// проверка правильности ответа
                // очищаем группу от кнопок
                radioGroup.removeAllViews();
                // переходим на следующий вопрос
                q++;
                // создаем кнопки по количеству ответов в вопросе
                add_button(q);
                // если пришли к последнему вопросу, но кнопка "Следующий" недоступна для клика
                if (q==test.qestions.size()-1){
                    bt_next.setEnabled(false);
                }
            }
        });

    }
    public void apdate (ArrayList <String> a)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, a);

        listView.setAdapter(adapter);
    }
    public void checkAnswer(int q){
        // цикл по количеству ответов в вопросе
        boolean help = false;
        for (int i =0; i< 4; i++) {
            // берем кнопку по Id
            RadioButton btnTag = (RadioButton) findViewById(1000+i);
            // проверяем выбрана ли эта кнопка и номер правильного ответа
            if (btnTag.isChecked() && test.getRight_answers().get(q).equals(btnTag.getText())){
                help = true;
                answers_true.add(help);
                res++;
                break;
            }
        }
        if (!help)
        {
            answers_true.add(help);
        }
    }

    private void add_button(int q){
        tv_q.setText(test.getQestions().get(q));
        // цикл по количеству ответов в вопросе
        for (int i =0; i < 4; i++) {
            // новая радиокнопка
            RadioButton btnTag = new RadioButton(this);
            // устанавливаем в ней значение
            btnTag.setText(test.getAnswers().get(q).get(i));
            // даем ей Id (лучше не давать значения близкие к 0, они могут использоваться системой)
            btnTag.setId(1000+i);
            // добавляем кнопку в группу
            radioGroup.addView(btnTag);
        }
    }
    public void result_list (String name)
    {
        myRef.child(key).child("tests").child(name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (int i = 0; i < (int) dataSnapshot.getChildrenCount(); i++) {

                    if (answers_true.get(i))
                    {
                        otv.add(i+1 + ": Всё верно!");
                    }
                    else
                    {
                        HashMap <String, String> a = (HashMap<String, String>) dataSnapshot.child("qestion_"+i).getValue();
                        otv.add(i+1 + ": Неправильный ответ. Верный ответ: " + a.get("right_answer"));
                    }
                }
                apdate(otv);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    public void setQestionsCount (int count)
    {
        QestionsCount = count;
    }
}