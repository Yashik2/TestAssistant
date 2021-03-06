package com.example.v1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;

public class auth extends BaseActivity implements

        View.OnClickListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAyth;
    private DatabaseReference myRef = database.getReference();

    FirebaseListAdapter mAdapter;





    public boolean sucsesfull = false;
    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private TextInputLayout mEmailField;
    private TextInputLayout mLoginField;
    private TextInputLayout mPasswordField;
    public String email;
    public String login;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    public FirebaseUser user;
    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mEmailField = (TextInputLayout) findViewById(R.id.textField_email);
        mLoginField = (TextInputLayout) findViewById(R.id.textField_login);
        mPasswordField = (TextInputLayout) findViewById(R.id.textField_password);

        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent (auth.this, MainActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    private void createAccount(String email, String password, String login) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(auth.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            sucsesfull = true;
                            Toast.makeText(auth.this, "???? ????????????????????????????????????!", Toast.LENGTH_SHORT).show();
                            myRef.child(user.getUid()).child("email").setValue(email);
                            myRef.child(user.getUid()).child("login").setValue(login);
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password, String login) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(auth.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
    }


    private boolean validateForm() {
        boolean valid = true;

         email = mEmailField.getEditText().getText().toString();
         login = mLoginField.getEditText().getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("???????????????????? ??????????????????!");
            Toast.makeText(this, "?????????????? ?????? ????????", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getEditText().getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("???????????????????? ??????????????????!");
            Toast.makeText(this, "?????????????? ?????? ????????", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        if (TextUtils.isEmpty(login)) {
            mLoginField.setError("???????????????????? ??????????????????!");
            Toast.makeText(this, "?????????????? ?????? ????????", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            mLoginField.setError(null);
        }

        return valid;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getEditText().getText().toString(), mPasswordField.getEditText().getText().toString(), mLoginField.getEditText().getText().toString());
        } else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getEditText().getText().toString(), mPasswordField.getEditText().getText().toString(), mLoginField.getEditText().getText().toString());
//        } else if (i == R.id.sign_out_button) {
//            signOut();
        }
    }
}