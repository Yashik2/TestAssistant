package com.example.v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Making_test extends AppCompatActivity {
    Toast toast;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAyth;
    private DatabaseReference myRef = database.getReference();

    FirebaseListAdapter mAdapter;


    FirebaseUser user = mAyth.getInstance().getCurrentUser();

    TextInputLayout textField_answer1, textField_qestion, textField_right_answer;
    Button bt_next_qestion, bt_end, bt_entr, bt_next_answer, bt_save;
    public String right_answer;
    public ArrayList<String> answers = new ArrayList<>();
    public ArrayList<String> qestions = new ArrayList<>();
    public HashMap<Integer ,ArrayList<String>> answers_main = new HashMap<>();
    public ArrayList<String> right_answers = new ArrayList<>();
    public  int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        final Context context = this;
        final Intent[] intent = new Intent[1];
        setContentView(R.layout.activity_making_test);
        textField_right_answer = (TextInputLayout) findViewById(R.id.textField_right_answer);
        textField_answer1 = (TextInputLayout) findViewById(R.id.textField_answer);
        TextInputLayout textField_answer2 = (TextInputLayout) findViewById(R.id.textField_password);
        TextInputLayout textField_answer3 = (TextInputLayout) findViewById(R.id.textField_answer3);
        TextInputLayout textField_answer4 = (TextInputLayout) findViewById(R.id.textField_answer4);
        textField_qestion = (TextInputLayout) findViewById(R.id.textField_of_qestion);
        bt_end = (Button) findViewById(R.id.bt_end);
        bt_next_qestion = (Button) findViewById(R.id.bt_next_qestion);
        //bt_entr = (Button) findViewById(R.id.bt_entr);
        bt_next_answer = (Button) findViewById(R.id.bt_next_answer);
        bt_save = (Button) findViewById(R.id.bt_save);

        bt_next_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answers.add(textField_answer1.getEditText().getText().toString());
                answers.add(textField_answer2.getEditText().getText().toString());
                answers.add(textField_answer3.getEditText().getText().toString());
                answers.add(textField_answer4.getEditText().getText().toString());

                textField_answer1.getEditText().setText("");
                textField_answer2.getEditText().setText("");
                textField_answer3.getEditText().setText("");
                textField_answer4.getEditText().setText("");

            }
        });


        bt_next_qestion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                qestions.add(textField_qestion.getEditText().getText().toString());
                right_answer = textField_right_answer.getEditText().getText().toString();
                right_answers.add(textField_right_answer.getEditText().getText().toString());

                ArrayList<String> answers_help = new ArrayList<>(answers);

                answers_main.put(i, answers_help);
                answers.removeAll(answers);
                textField_qestion.getEditText().setText("");
                textField_right_answer.getEditText().setText("");
                i++;
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                final EditText input = new EditText(context);
                alert.setTitle("Сохранение")
                        .setMessage("Введите название теста")
                        .setIcon(R.drawable.test_icon_foreground)
                        .setView(input)
                        .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Закрываем окно
                                if (input.getText().toString() != null)
                                {
                                    Intent i = new Intent(Making_test.this, MainActivity.class);
                                    String name = input.getText().toString();
                                    for (int b = 0; b < qestions.size(); b++)
                                    {
                                        for (int h = 0; h < 4; h++)
                                        {
                                            myRef.child(user.getUid()).child("tests").child(name).child("qestion_" + b + "").child("answer_"+h+"").setValue(answers_main.get(b).get(h));
                                        }
                                        myRef.child(user.getUid()).child("tests").child(name).child("qestion_" + b + "").child("qestion").setValue(qestions.get(b));
                                        myRef.child(user.getUid()).child("tests").child(name).child("qestion_" + b + "").child("right_answer").setValue(right_answers.get(b));
                                    }
                                    Toast.makeText( getApplicationContext(), "Тест сохранён", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                    startActivity(i);
                                }
                               else
                                {
                                    Toast.makeText( getApplicationContext(), "Введите название теста", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNeutralButton("Сохранить и опубликовать", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (input.getText().toString() != null)
                                {
                                    Intent i = new Intent(Making_test.this, MainActivity.class);
                                    for (int b = 0; b < qestions.size(); b++)
                                    {
                                        String name = input.getText().toString();
                                        for (int h = 0; h < 4; h++)
                                        {
                                            myRef.child(user.getUid()).child("tests").child(name).child("qestion_" + b + "").child("answer_"+h+"").setValue(answers_main.get(b).get(h));
                                        }
                                        myRef.child(user.getUid()).child("tests").child(name).child("qestion_" + b + "").child("qestion").setValue(qestions.get(b));
                                        myRef.child(user.getUid()).child("tests").child(name).child("qestion_" + b + "").child("right_answer").setValue(right_answers.get(b));
                                    }
                                    for (int b = 0; b < qestions.size(); b++)
                                    {
                                        String name = input.getText().toString();
                                        for (int h = 0; h < 4; h++)
                                        {
                                            myRef.child("all").child("tests").child(name).child("qestion_" + b + "").child("answer_"+h+"").setValue(answers_main.get(b).get(h));
                                        }
                                        myRef.child("all").child("tests").child(name).child("qestion_" + b + "").child("qestion").setValue(qestions.get(b));
                                        myRef.child("all").child("tests").child(name).child("qestion_" + b + "").child("right_answer").setValue(right_answers.get(b));
                                    }
                                    Toast.makeText( getApplicationContext(), "Тест сохранён", Toast.LENGTH_SHORT).show();
                                    startActivity(i);
                                }
                                else
                                {
                                    Toast.makeText( getApplicationContext(), "Введите название теста", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Закрываем окно
                                dialog.cancel();
                            }
                        });
                alert.show();

                InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                imm.showSoftInput(input, InputMethodManager.SHOW_FORCED);
            }
        });

        bt_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Making_test.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}