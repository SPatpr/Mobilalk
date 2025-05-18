package com.example.leaguetables;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getName();
    private static final int SECRET_KEY = 99;

    EditText userNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText confirmEditText;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

         userNameEditText = findViewById(R.id.usernameEditText);
         emailEditText = findViewById(R.id.emailEditText);
         passwordEditText = findViewById(R.id.passwordEditText);
         confirmEditText = findViewById(R.id.confirmPasswordEditText);

        auth = FirebaseAuth.getInstance();


    }

    public void register(View view) {
        String username = userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirm = confirmEditText.getText().toString();


        if (!password.equals(confirm)) {
            Log.e(LOG_TAG, "Nem egyenlő jelszó");
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG, "Sikeres Regisztráció");

                    user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();
                        user.updateProfile(profileUpdates);
                    }
                    SharedPreferences prefs = getSharedPreferences("user_session", MODE_PRIVATE);
                    prefs.edit().putBoolean("isLoggedIn", true).apply();
                    startmain();
                }else{
                    Log.d(LOG_TAG, "Sikertelen regisztráció");
                    Toast.makeText(RegistrationActivity.this, "Sikertelen regisztráció:" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    public void log(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void startmain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


}