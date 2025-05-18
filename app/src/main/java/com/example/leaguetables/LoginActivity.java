package com.example.leaguetables;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = LoginActivity.class.getName();
    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    private FirebaseAuth auth;

    private FirebaseUser user;
    TextView userNameText;
    TextView passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameText = findViewById(R.id.editTextEmail);
        passwordText = findViewById(R.id.editTextPassword);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        auth = FirebaseAuth.getInstance();


    }

    public void reg(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);

        startActivity(intent);
    }

    public void login(View view) {
        EditText userName = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);

        String userNameStr = userName.getText().toString();
        String passwordStr = password.getText().toString();

        auth.signInWithEmailAndPassword(userNameStr, passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(LOG_TAG, "Sikeres belépés");
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("username", userNameStr);
                    editor.apply();
                    startmain();
                }else{
                    Log.d(LOG_TAG, "Sikertelen belépés");
                    Toast.makeText(LoginActivity.this, "Sikertelen belépés: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public void startmain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}