package com.ates.bookguide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextInputEditText emailEditPass;
    Button btnSubmitPass;
    Toolbar toolbar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailEditPass = findViewById(R.id.emailEditPass);
        btnSubmitPass = findViewById(R.id.submitBtnPass);
        toolbar = findViewById(R.id.toolbarPass);
        mAuth = FirebaseAuth.getInstance();
        
        btnSubmitPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditPass.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(ForgotPasswordActivity.this, "Lütfen E-mail adresinizi giriniz", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "Mail adresinize şifre sıfırlama bağlantısı gönderildi", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPasswordActivity.this,SignInActivity.class));
                            }
                            else {
                                String message = task.getException().getMessage();
                                Toast.makeText(ForgotPasswordActivity.this, "Şifre sıfırlama bağlantısı gönderme başarısız", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}