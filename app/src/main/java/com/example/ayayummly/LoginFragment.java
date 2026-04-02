package com.example.ayayummly;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;



import com.example.ayayummly.classes.FirebaseServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

/*
public class LoginFragment extends Fragment {

    private EditText etUsername, etPassword;
    private TextView tvSignupLink;
    private TextView tvForgotLink;
    private Button btnLogin;
    private FirebaseServices fbs;

    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        // connecting components
        fbs = FirebaseServices.getInstance();
        etUsername = getView().findViewById(R.id.etUsernameLogin);
        etPassword = getView().findViewById(R.id.etPasswordLogin);
        btnLogin = getView().findViewById(R.id.btnLoginLogin);
        tvSignupLink = getView().findViewById(R.id.tvSignupLinkLogin);
        tvSignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSignupFragment();
            }
        });
        tvForgotLink = getView().findViewById(R.id.tvForgotPasswordLogin);
        tvForgotLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {gotoForgotFragment();}
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Data validation
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.trim().isEmpty() && password.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Login procedure
                fbs.getAuth().signInWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getActivity(), "You have successfully logged in!", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to login! Check up!", Toast.LENGTH_LONG).show();

                    }
                });



            }
        });

    }

    private void gotoSignupFragment() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMain, new SignupFragment());
        ft.commit();
    }
    private void gotoForgotFragment() {

        FragmentTransaction ft1 = getActivity().getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.frameLayoutMain, new ForgotPasswordFragment());
        ft1.commit();
    }
}

 */
//كوبايلت
public class LoginFragment extends Fragment {

    private EditText etUsername, etPassword;
    private TextView tvSignupLink, tvForgotLink;
    private Button btnLogin;
    private FirebaseServices fbs;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Firebase
        fbs = FirebaseServices.getInstance();

        // Connect components
        etUsername = view.findViewById(R.id.etUsernameLogin);
        etPassword = view.findViewById(R.id.etPasswordLogin);
        btnLogin = view.findViewById(R.id.btnLoginLogin);
        tvSignupLink = view.findViewById(R.id.tvSignupLinkLogin);
        tvForgotLink = view.findViewById(R.id.tvForgotPasswordLogin);

        // Go to Signup
        tvSignupLink.setOnClickListener(v -> gotoSignupFragment());

        // Go to Forgot Password
        tvForgotLink.setOnClickListener(v -> gotoForgotFragment());

        // Login button
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_LONG).show();
                return;
            }

            fbs.getAuth().signInWithEmailAndPassword(username, password)
                    .addOnSuccessListener(authResult -> {
                        Toast.makeText(getActivity(), "You have successfully logged in!", Toast.LENGTH_LONG).show();

                        // بعد تسجيل الدخول، روّحي على AllRecipesFragment
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frameLayoutMain, new AllRecipesFragment());
                        ft.commit();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Failed to login! Check up!", Toast.LENGTH_LONG).show();
                    });
        });
    }

    private void gotoSignupFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMain, new SignupFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private void gotoForgotFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMain, new ForgotPasswordFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}
