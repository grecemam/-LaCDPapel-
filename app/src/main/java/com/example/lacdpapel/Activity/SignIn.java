package com.example.lacdpapel.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lacdpapel.Models.User;
import com.example.lacdpapel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class
SignIn extends AppCompatActivity {
    Button signin;
    EditText loginInput, passwordInput;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userRef = db.collection("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        loginInput = findViewById(R.id.email_et);
        passwordInput = findViewById(R.id.password_et);
        signin = findViewById(R.id.signin_btn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = loginInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
                    loginInput.setError("Неверный формат почты");

                } else if (password.length() < 6) {
                    passwordInput.setError("Меньше 6 символов");
                } else {
                    registerUser(login, password);
                }
            }
        });


    }
    private void registerUser(String login, String password) {
        firebaseAuth.createUserWithEmailAndPassword(login, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                        User user = new User(currentUser.getUid(), currentUser.getEmail());
                        userRef.add(user)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(SignIn.this, currentUser.getEmail() + "зареган", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignIn.this, glavnaya.class);
                                        intent.putExtra("user_email", currentUser.getEmail());

                                        startActivity(intent);
                                        finish();
                                        saveLoginStatus(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }
    private void saveLoginStatus(boolean isLoggedIn) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }
}
