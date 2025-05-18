package com.example.leaguetables;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        loadProfile();

        findViewById(R.id.btnSave).setOnClickListener(v -> saveProfile());

        findViewById(R.id.btnDelete).setOnClickListener(v -> confirmDeletion());

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_top5) {
                showTop5Leagues();
                return true;
            } else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }

    private void loadProfile() {
        String name = currentUser.getDisplayName();
        if (!TextUtils.isEmpty(name)) etUsername.setText(name);
        // Password field remains empty for security
    }

    private void saveProfile() {
        String newName  = etUsername.getText().toString().trim();
        String newPwd   = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(newName)) {
            Toast.makeText(this, "Kérlek töltsd ki a felhasználónevet!", Toast.LENGTH_SHORT).show();
            return;
        }

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build();
        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                        Toast.makeText(this, "Név frissítve", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Név frissítése sikertelen: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                });

        if (!TextUtils.isEmpty(newPwd)) {
            // Prompt re-authentication
            promptReauthentication(() -> {
                currentUser.updatePassword(newPwd)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful())
                                Toast.makeText(this, "Jelszó frissítve", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(this, "Jelszó frissítése sikertelen: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });
        }
    }

    private void promptReauthentication(Runnable onSuccess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Újra hitelesítés szükséges");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Jelszó");
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String pwd = input.getText().toString().trim();
            reauthenticate(pwd, onSuccess);
        });
        builder.setNegativeButton("Mégse", null);
        builder.show();
    }

    private void reauthenticate(String password, Runnable onSuccess) {
        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), password);
        currentUser.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        onSuccess.run();
                    } else {
                        Toast.makeText(this, "Újra hitelesítés sikertelen: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void confirmDeletion() {
        new AlertDialog.Builder(this)
                .setTitle("Profil törlése")
                .setMessage("Biztosan törölni szeretnéd a profilodat? Ez végleges lesz.")
                .setPositiveButton("Igen", (dialog, which) -> deleteProfile())
                .setNegativeButton("Mégse", null)
                .show();
    }

    private void deleteProfile() {
        currentUser.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Profil sikeresen törölve", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Profil törlése sikertelen: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity.this, "Kiválasztva: " + selected, Toast.LENGTH_SHORT).show();
                        switch (selected) {
                            case "Premier League":
                                startActivity(new Intent(ProfileActivity.this, PlActivity.class));
                                break;
                            case "La Liga":
                                startActivity(new Intent(ProfileActivity.this, LaligaActivity.class));
                                break;
                            case "Bundesliga":
                                startActivity(new Intent(ProfileActivity.this, BundesligaActivity.class));
                                break;
                            case "Serie A":
                                startActivity(new Intent(ProfileActivity.this, SeriaActivity.class));
                                break;
                            case "Ligue 1":
                                startActivity(new Intent(ProfileActivity.this, Ligue1Activity.class));
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }
}
