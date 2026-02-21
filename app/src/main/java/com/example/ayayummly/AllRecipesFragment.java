package com.example.ayayummly;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

        recyclerView = getView().findViewById(R.id.rvRecipeList);
        Button btnAddRecipe = getView().findViewById(R.id.btnAddRecipe);

        fbs = FirebaseServices.getInstance();
        //fbs.setUserChangeFlag(false);
        recipes = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*
        اصلي
        recipes = getRecipes();
        myAdapter = new AllRecipesAdapter(getActivity(), recipes);
        */
        myAdapter = new AllRecipesAdapter(getActivity(), recipes);
        recyclerView.setAdapter(myAdapter);
        getRecipes(); // نستدعيها بدون return


        filteredList = new ArrayList<>();

        // فتح صفحة إضافة وصفة
        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();

                ft.replace(R.id.frameLayout, new AddRecipeFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });




        //هاي مع التفاصيل
        // كود الضغط على العناصر
        myAdapter.setOnItemClickListener(new AllRecipesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                String selectedRecipe = recipes.get(position).getRecipeName();
                Toast.makeText(getActivity(), "Clicked: " + selectedRecipe, Toast.LENGTH_SHORT).show();

                //هضول جزء من تعون التفاصيل
                Bundle args = new Bundle();
                args.putParcelable("recipe", recipes.get(position));
                RecipeDetailsFragment recipeDetails = new RecipeDetailsFragment();
                recipeDetails.setArguments(args);

                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,recipeDetails);
                // ft.addToBackStack(null); // مهم عشان ترجعي للقائمة
                ft.commit();

                //ملاحظة انا مش عاملة هون ولا السيرتش ولا البروفايل ولا الاشياء الي الاستاذ عاملهن بالوي كار
            }
        });



    }

//كوبايلت بس زبطت الحمد لله
    public void getRecipes() {

        try {
            fbs.getFire().collection("recipes")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            recipes.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                recipes.add(document.toObject(Recipe.class));
                            }

                            myAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getActivity(), "Error loading recipes", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e) {
            Log.e("getCompaniesMap(): ", e.getMessage());
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


//اصلي
/*
        public ArrayList<Recipe> getRecipes()
        {

            ArrayList<Recipe> recipes = new ArrayList<>();

            try {
            recipes.clear();
            fbs.getFire().collection("recipes")
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
                                adapter.notifyDataSetChanged();


                                //addUserToCompany(companies, user);
                            } else {
                                //Log.e("AllRestActivity: readData()", "Error getting documents.", task.getException());
                                Toast.makeText(getActivity(), "Error loading recipes", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            }
            catch (Exception e)
            {
            Log.e("getCompaniesMap(): ", e.getMessage());
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            return recipes;
        }



 */

}
