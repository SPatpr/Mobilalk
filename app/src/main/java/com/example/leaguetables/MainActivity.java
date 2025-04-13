package com.example.leaguetables;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private FirebaseUser user;
    TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        welcomeTextView = findViewById(R.id.welcomeTextView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            String displayName = user.getDisplayName();
            Log.d(LOG_TAG, "Authentikált");
            if (displayName != null && !displayName.isEmpty()) {
                welcomeTextView.setText("Üdvözlünk, " + displayName + "!");
            } else {
                welcomeTextView.setText("Üdvözlünk, felhasználó!");
            }
        }else {
            Log.d(LOG_TAG, "Nem authentikált");
            finish();
        }


    }
}