package com.example.ayayummly;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ayayummly.classes.AllRecipesAdapter;
import com.example.ayayummly.classes.FirebaseServices;
import com.example.ayayummly.classes.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllRecipesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseServices fbs;
    private AllRecipesAdapter myAdapter;
    private ArrayList<Recipe> recipes, filteredList;



    public AllRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_recipes, container, false);
    }

    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        recipes = new ArrayList<>();
        recipes = getRecipes();
    }



        public ArrayList<Recipe> getRecipes()
    {
        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            recipes.clear();
            fbs.getFire().collection("recipes2")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    recipes.add(document.toObject(Recipe.class));
                                }

                                AllRecipesAdapter adapter = new AllRecipesAdapter(getActivity(), recipes);
                                recyclerView.setAdapter(adapter);
                                //addUserToCompany(companies, user);
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
        catch (Exception e)
        {
            Log.e("getCompaniesMap(): ", e.getMessage());
        }

        return recipes;
    }


}