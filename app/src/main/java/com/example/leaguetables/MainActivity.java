package com.example.leaguetables;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvGreeting = findViewById(R.id.tvGreeting);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
            tvGreeting.setText("Üdvözöljük: " + user.getDisplayName());
        } else {
            tvGreeting.setText("Üdvözöljük!");
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_top5) {
                    showTop5Leagues();
                    return true;
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });

    }

    private void showTop5Leagues() {
        final String[] leagues = {"Premier League", "La Liga", "Bundesliga", "Serie A", "Ligue 1"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Top 5 bajnokságok")
                .setItems(leagues, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selected = leagues[which];
                        Toast.makeText(MainActivity.this, "Kiválasztva: " + selected, Toast.LENGTH_SHORT).show();
                        switch (selected) {
                            case "Premier League":
                                startActivity(new Intent(MainActivity.this, PlActivity.class));
                                break;
                            case "La Liga":
                                startActivity(new Intent(MainActivity.this, LaligaActivity.class));
                                break;
                            case "Bundesliga":
                                startActivity(new Intent(MainActivity.this, BundesligaActivity.class));
                                break;
                            case "Serie A":
                                startActivity(new Intent(MainActivity.this, SeriaActivity.class));
                                break;
                            case "Ligue 1":
                                startActivity(new Intent(MainActivity.this, Ligue1Activity.class));
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }
}