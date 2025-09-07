package com.example.lacdpapel.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lacdpapel.BasketManager;
import com.example.lacdpapel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    TextView regis;
    EditText loginInput, passwordInput;
    Button login;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userRef = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginInput = findViewById(R.id.email_et);
        passwordInput = findViewById(R.id.password_et);
        regis = findViewById(R.id.regis);
        login = findViewById(R.id.login_btn);
        BasketManager.removeALL(this);
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignIn.class);
                startActivity(intent);
            }
        });


        if (isLoggedIn()) {
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            Intent intent = new Intent(MainActivity.this, glavnaya.class);
            intent.putExtra("user_email", currentUser.getEmail());
            startActivity(intent);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
                    loginInput.setError("Неверный формат почты");
                } else if (password.length() < 6) {
                    passwordInput.setError("Меньше 6 символов");
                } else {
                    signinUser(login, password);
                }
            }
        });

    }
    private void signinUser(String login, String password) {
        firebaseAuth.signInWithEmailAndPassword(login, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        Intent intent = new Intent(MainActivity.this, glavnaya.class);
                        intent.putExtra("user_email", currentUser.getEmail());

                        startActivity(intent);
                        finish();
                        Toast.makeText(MainActivity.this, "Вход успешен", Toast.LENGTH_SHORT).show();
                        saveLoginStatus(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Ошибка входа: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginStatus", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }
    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }

}