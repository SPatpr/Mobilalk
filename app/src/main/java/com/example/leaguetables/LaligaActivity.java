package com.example.leaguetables;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

public class LaligaActivity extends AppCompatActivity {

    private static final String PREF_KEY = LoginActivity.class.getPackage().toString();
    private SharedPreferences preferences;
    private FirebaseFirestore db;
    private TeamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String user = preferences.getString("username", "");
        if (user.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_laliga);

        RecyclerView rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TeamAdapter();
        rv.setAdapter(adapter);


        db = FirebaseFirestore.getInstance();

        db.collection("standings").document("laliga")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        if (snapshot != null && snapshot.exists()) {
                            List<Map<String, Object>> table = (List<Map<String, Object>>) snapshot.get("table");
                            List<Team> teams = new ArrayList<>();
                            for (Map<String, Object> map : table) {
                                String name = (String) map.get("team");
                                String league = "laliga";
                                int points = ((Long) map.get("points")).intValue();
                                int goalsAgainst = ((Long) map.get("goalsAgainst")).intValue();
                                int goalDifference = ((Long) map.get("goalDifference")).intValue();
                                int goalsFor = ((Long) map.get("goalsFor")).intValue();
                                Team team = new Team(name, league, points, goalsAgainst, goalDifference, goalsFor);
                                teams.add(team);
                            }
                            adapter.submitList(teams);
                        }
                    }
                });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_top5) {
                    showTop5Leagues();
                    return true;
                } else if (id == R.id.nav_profile) {
                    startActivity(new Intent(LaligaActivity.this, ProfileActivity.class));
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
                        Toast.makeText(LaligaActivity.this, "Kiválasztva: " + selected, Toast.LENGTH_SHORT).show();
                        switch (selected) {
                            case "Premier League":
                                startActivity(new Intent(LaligaActivity.this, PlActivity.class));
                                break;
                            case "La Liga":
                                startActivity(new Intent(LaligaActivity.this, LaligaActivity.class));
                                break;
                            case "Bundesliga":
                                startActivity(new Intent(LaligaActivity.this, BundesligaActivity.class));
                                break;
                            case "Serie A":
                                startActivity(new Intent(LaligaActivity.this, SeriaActivity.class));
                                break;
                            case "Ligue 1":
                                startActivity(new Intent(LaligaActivity.this, Ligue1Activity.class));
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }
}
