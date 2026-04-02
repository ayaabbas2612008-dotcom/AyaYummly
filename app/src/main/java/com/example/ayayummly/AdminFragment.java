package com.example.ayayummly;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/*
public class AdminFragment extends Fragment {

    private  Button btnAdd, btnAll, btnProfile;




    public AdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false); 
    }

    @Override
    public void onStart() {
        super.onStart();
        btnAdd = getView().findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoAddRecipeFragment();
            }
        });
        btnAll = getView().findViewById(R.id.btnAll);
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {gotoAllRecipeFragment();}
        });
        btnProfile = getView().findViewById(R.id.btnAdminProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoProfileFragment();
            }
        });

    }
    private void gotoProfileFragment() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMain, new ProfileFragment());
        ft.commit();
    }

    private void gotoAddRecipeFragment() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMain, new AddRecipeFragment());
        ft.commit();
    }
    private void gotoAllRecipeFragment() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayoutMain, new AllRecipesFragment());
        ft.commit();
    }
}

 */
public class AdminFragment extends Fragment {

    private Button btnAdd, btnAll, btnProfile, btnLogin, btnSignup, btnForgot;

    public AdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Connect buttons
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAll = view.findViewById(R.id.btnAll);
        btnProfile = view.findViewById(R.id.btnAdminProfile);
        btnLogin = view.findViewById(R.id.btnlogin);
        btnSignup = view.findViewById(R.id.btnSignup);
        btnForgot = view.findViewById(R.id.btnForgot);

        // Add Recipe
        btnAdd.setOnClickListener(v -> {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, new AddRecipeFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        // All Recipes
        btnAll.setOnClickListener(v -> {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, new AllRecipesFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        // Profile
        btnProfile.setOnClickListener(v -> {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, new ProfileFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        // Login
        btnLogin.setOnClickListener(v -> {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, new LoginFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        // Signup
        btnSignup.setOnClickListener(v -> {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, new SignupFragment());
            ft.addToBackStack(null);
            ft.commit();
        });

        // Forgot Password
        btnForgot.setOnClickListener(v -> {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayoutMain, new ForgotPasswordFragment());
            ft.addToBackStack(null);
            ft.commit();
        });
    }
}
