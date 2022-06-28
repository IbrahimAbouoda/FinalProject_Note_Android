package com.example.staynoted.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.staynoted.R;
import com.example.staynoted.listeners.LoginStatusListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignupFragment extends Fragment {

    LoginStatusListener mLoginStatusListener;
    private TextInputEditText etEmail, etPassword;
    private Button btnSignUp;
    private TextView tvSignIn;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;
    private String email;
    private String password;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginStatusListener) {
            mLoginStatusListener = (LoginStatusListener) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        tvSignIn = view.findViewById(R.id.tvSignIn);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
//
        tvSignIn.setOnClickListener(view1 -> mLoginStatusListener.onLoginStatusChanged(LoginStatusListener.LOGIN));

        btnSignUp.setOnClickListener(view12 -> {
            email = etEmail.getText().toString();
            password = etPassword.getText().toString();
            if (validateData()) {
                createAccount();
            } else {
                Toast.makeText(getActivity(), "invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean validateData() {
        return !etEmail.getText().toString().isEmpty() && etPassword.getText().toString().length() > 8;
    }

    private void createAccount() {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    Toast.makeText(getActivity(), "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    if (task.getException().toString().contains("already in use")) {
                        mLoginStatusListener.onLoginStatusChanged(LoginStatusListener.SIGNUP);
                    }
                    return;
                }

                HashMap<String, String> data = new HashMap<>();

                String uid = mFirebaseAuth.getCurrentUser().getUid();

                data.put("email", email);
                data.put("uid", uid);

                mFirebaseFirestore.collection("users")
                        .document(uid)
                        .collection("details")
                        .add(data)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (!task.isSuccessful()) {
                                    return;
                                }
                                Toast.makeText(getActivity(), "Account created successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


}