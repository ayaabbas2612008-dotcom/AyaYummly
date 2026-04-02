package com.example.ayayummly;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ayayummly.classes.FirebaseServices;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileFragment extends Fragment {

    private FirebaseServices fbs;
    private TextView tvUserName, tvUserEmail, tvAccountCreated;

    public ProfileFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fbs = FirebaseServices.getInstance();

        // ربط العناصر
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvAccountCreated = view.findViewById(R.id.tvAccountCreated);

        View btnMyRecipes = view.findViewById(R.id.btnMyRecipes);
        View btnFavorites = view.findViewById(R.id.btnFavorites);
        View btnLogout = view.findViewById(R.id.btnLogout);

        // جلب المستخدم الحالي
        FirebaseUser user = fbs.getAuth().getCurrentUser();

        if (user != null) {

            // الإيميل
            tvUserEmail.setText(user.getEmail());

            // استخراج الاسم من الإيميل
            String email = user.getEmail();
            String name = email.substring(0, email.indexOf("@"));
            tvUserName.setText("Welcome, " + name);

            // تاريخ إنشاء الحساب
            long creationTime = user.getMetadata().getCreationTimestamp();
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(creationTime));
            tvAccountCreated.setText("Member since: " + date);

        } else {
            tvUserName.setText("Guest");
            tvUserEmail.setText("Not logged in");
            tvAccountCreated.setText("Member since: -");
        }

        // زر My Recipes
        btnMyRecipes.setOnClickListener(v -> {
            MyRecipesFragment frag = new MyRecipesFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, frag)
                    .addToBackStack(null)
                    .commit();
        });
/// ///////////////////////////////////
        // زر Favorites
        btnFavorites.setOnClickListener(v -> {
            FavoriteFragment frag = new FavoriteFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, frag)
                    .addToBackStack(null)
                    .commit();
        });

        // زر Logout
        btnLogout.setOnClickListener(v -> {
            fbs.getAuth().signOut();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new LoginFragment())
                    .commit();

        });
    }
}
