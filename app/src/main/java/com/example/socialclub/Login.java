package com.example.socialclub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn,mforgetPassword;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mforgetPassword=findViewById(R.id.forgetPassword);
        mEmail=findViewById(R.id.Email);
        mPassword=findViewById(R.id.password);
        mLoginBtn=findViewById(R.id.loginbutton);
        mCreateBtn=findViewById(R.id.createText);
        fAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);

        mLoginBtn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required.");
                return;
            }

            if (password.length() < 6) {
                mPassword.setError("Password must be >=6 characters");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            //authenticate the user

            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{
                    Toast.makeText(Login.this,"Error!"+ Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            });
        });

        mCreateBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Register.class)));

        mforgetPassword.setOnClickListener(v -> {
            EditText resetMail=new EditText(v.getContext());
            AlertDialog.Builder passwordResetDialog= new AlertDialog.Builder(v.getContext());
            passwordResetDialog.setTitle("Reset Password?");
            passwordResetDialog.setMessage("Enter your email to receive reset link");
            passwordResetDialog.setView(resetMail);

            passwordResetDialog.setPositiveButton("Yes", (dialog, which) -> {
                String mail=resetMail.getText().toString();
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid -> Toast.makeText(Login.this,"Reset link sent to your email",Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(Login.this,"Error!Reset link is not sent"+e.getMessage(),Toast.LENGTH_SHORT).show());

            });
            passwordResetDialog.setNegativeButton("No", (dialog, which) -> {

            });
            passwordResetDialog.create().show();

        });
    }
}