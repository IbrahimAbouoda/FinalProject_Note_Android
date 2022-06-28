package com.example.staynoted.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.staynoted.R;
import com.example.staynoted.fragments.LoginFragment;
import com.example.staynoted.fragments.SignupFragment;
import com.example.staynoted.listeners.LoginStatusListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements LoginStatusListener {

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SignupFragment signupFragment = new SignupFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, signupFragment)
                .commit();

        mFirebaseAuth = FirebaseAuth.getInstance();
//
        mFirebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });


    }

    @Override
    public void onLoginStatusChanged(int statusCode) {

        switch (statusCode) {
            case LOGIN:
                SignupFragment signupFragment = new SignupFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, signupFragment)
                        .commit();
                break;
            case SIGNUP:
                LoginFragment loginFragment = new LoginFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, loginFragment)
                        .commit();
                break;
        }
    }
}