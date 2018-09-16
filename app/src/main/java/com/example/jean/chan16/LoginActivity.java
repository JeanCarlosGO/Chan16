package com.example.jean.chan16;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.jean.chan16.Bases.AuthActivity;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AuthActivity implements View.OnClickListener {

    EditText emailInput;
    EditText passwordInput;
    Button btnSignIn;
    Button btnRegister;
    SignInButton btnSignInGoogle;

    final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.user);
        passwordInput = findViewById(R.id.pass);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        btnSignInGoogle = findViewById(R.id.btnSignInGoogle);
        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnSignInGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String email = emailInput.getText().toString();
        String pass = passwordInput.getText().toString();

        switch (v.getId()) {
            case R.id.btnRegister:
                if(!validateEmailAndPassword(emailInput, passwordInput)) {
                    return;
                }
                register(v, email, pass);
                break;

            case R.id.btnSignIn:
                if(!validateEmailAndPassword(emailInput, passwordInput)) {
                    return;
                }
                signInWithEmailAndPassword(v, email, pass);
                break;

            case R.id.btnSignInGoogle:
                signInWithGoogle(v);
                break;
        }
    }

    @Override
    public void updateUI(FirebaseUser user) {
        super.updateUI(user);
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private boolean validateEmailAndPassword(EditText userEmail, EditText password) {
        boolean valid = true;
        String email = emailInput.getText().toString();
        String pass = passwordInput.getText().toString();
        if(TextUtils.isEmpty(email)) {
            userEmail.setError("Required");
            valid = false;
        }
        if(TextUtils.isEmpty(pass)) {
            password.setError("Required");
            valid = false;
        }

        return valid;
    }
}
