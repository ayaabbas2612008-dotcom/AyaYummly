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
        recipes = getRecipes();
        myAdapter = new AllRecipesAdapter(getActivity(), recipes);
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




        //هاي بدون التفاصيل
        // كود الضغط على العناصر
        myAdapter.setOnItemClickListener(new AllRecipesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                // ما في فتح لصفحة التفاصيل
                String selectedRecipe = recipes.get(position).getRecipeName();
                Toast.makeText(getActivity(), "Clicked: " + selectedRecipe, Toast.LENGTH_SHORT).show();

                //هضول جزء من تعون التفاصيل
                Bundle args = new Bundle();

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                ft.commit();

                //هاي مع التفاصيل بس ضل ابني صفحة التفاصيل كفراجمينت
                 /*

                Bundle args = new Bundle();
                args.putParcelable("recipe", recipes.get(position)); // تأكدي إنه الكلاس Recipe implements Parcelable
                RecipeDetailsFragment recipeDetails = new RecipeDetailsFragment();
                recipeDetails.setArguments(args);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout, recipeDetails);
                ft.addToBackStack(null); // مهم عشان ترجعي للقائمة
                ft.commit();
           //وهاي الي تحت هون هي تاعت الاستاذ:

        myAdapter.setOnItemClickListener(new CarListAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click here
                String selectedItem = cars.get(position).getNameCar();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                Bundle args = new Bundle();
                args.putParcelable("car", cars.get(position)); // or use Parcelable for better performance
                CarDetailsFragment cd = new CarDetailsFragment();
                cd.setArguments(args);
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,cd);
                ft.commit();
            }
        });

               */
                //ملاحظة انا مش عاملة هون ولا السيرتش ولا البروفايل ولا الاشياء الي الاستاذ عاملهن بالوي كار
            }
        });



    }


    /*
    //هاي اصلا لازم احطها بصفحة فراجمن اسمها ريسيبي ديتاليز ولازم اعمللها اكس ام ال
    public class RecipeDetailsFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_recipe_details, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            if (getArguments() != null) {
                Recipe recipe = getArguments().getParcelable("recipe");
                // اربطي البيانات هنا
            }
        }
    }
    */



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


}