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
import android.widget.EditText;
import android.widget.ImageButton;
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

    private EditText etSearch;
    private ImageButton btnCategoryFilter;

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
        recyclerView = getView().findViewById(R.id.rvRecipeList);
        etSearch = getView().findViewById(R.id.etSearch); // ربط خانة البحث
        btnCategoryFilter = getView().findViewById(R.id.btnCategoryFilter); // ربط أيقونة الكتاب
        /*
        اصلي
        recipes = getRecipes();
        myAdapter = new AllRecipesAdapter(getActivity(), recipes);
        */
        myAdapter = new AllRecipesAdapter(getActivity(), recipes);
        recyclerView.setAdapter(myAdapter);
        getRecipes(); // نستدعيها بدون return


        filteredList = new ArrayList<>();

        // --- كود البحث ---
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRecipes(s.toString()); // استدعاء ميثود الفلترة
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // --- كود أيقونة التصنيفات (Category) ---
        btnCategoryFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryPopup(v); // ميثود لإظهار قائمة التصنيفات
            }
        });


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

    private void showCategoryPopup(View v) {
        androidx.appcompat.widget.PopupMenu popup = new androidx.appcompat.widget.PopupMenu(getActivity(), v);
        // سحب التصنيفات من strings.xml
        String[] categories = getResources().getStringArray(R.array.categories_array);

        popup.getMenu().add("All");
        for (String category : categories) {
            if (!category.equals("Select Category")) {
                popup.getMenu().add(category);
            }
        }

        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("All")) {
                myAdapter.filterList(recipes);
            } else {
                filterByCategory(item.getTitle().toString());
            }
            return true;
        });
        popup.show();
    }
    private void filterByCategory(String category) {
        ArrayList<Recipe> filtered = new ArrayList<>();
        for (Recipe item : recipes) {
            // تأكدي أن موديل الـ Recipe يحتوي على ميثود getCategory()
            if (item.getCategory() != null && item.getCategory().equals(category)) {
                filtered.add(item);
            }
        }
        myAdapter.filterList(filtered);
    }

    private void filterRecipes(String text) {
        ArrayList<Recipe> filtered = new ArrayList<>();
        for (Recipe item : recipes) {
            if (item.getRecipeName().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(item);
            }
        }
        // يجب أن يكون لديك ميثود في الـ Adapter لتحديث البيانات
        myAdapter.filterList(filtered);
    }

//كوبايلت بس زبطت الحمد لله 2
public void getRecipes() {

    try {
        fbs.getFire().collection("recipes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        recipes.clear();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Recipe r = document.toObject(Recipe.class);

                            // ⭐ إذا الوصفة ما فيها id → ضيفه تلقائيًا
                            if (document.get("id") == null) {
                                fbs.getFire().collection("recipes")
                                        .document(document.getId())
                                        .update("id", document.getId());
                            }

                            // ⭐ إذا الوصفة ما فيها fav → ضيفه تلقائيًا
                            if (document.get("fav") == null) {
                                fbs.getFire().collection("recipes")
                                        .document(document.getId())
                                        .update("fav", false);
                            }

                            recipes.add(r);
                        }

                        myAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "Error loading recipes", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    catch (Exception e) {
        Log.e("getRecipes(): ", e.getMessage());
        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

    /*
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


     */

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
