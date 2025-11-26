package com.example.ayayummly;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ayayummly.classes.FirebaseServices;
import com.example.ayayummly.classes.Recipe;
import com.example.ayayummly.classes.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRecipeFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 1001;
    private EditText etRecipeName, etCookName, etPrepTime, etCookTime, etDescription, etNotes;
    Spinner spCategory, spDifficulty;
    private RatingBar ratingBar;
    ImageView imageViewAddRecipe;
    private Button btnSave;
    private FirebaseServices fbs;
    private String imageUri = null;
    private Utils utils;

    String[] categories = {
            "Select Category",
            "Breakfast",
            "Lunch",
            "Dinner",
            "Dessert",
            "Drinks",
            "Snacks",
            "Salads",
            "Soups",
            "Baking",
            "Other..."
    };

    String[] difficulties = {
            "Select Difficulty",
            "Easy",
            "Medium",
            "Hard",
            "Expert",
            "Other..."
    };



    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            Uri imageUri = result.getData().getData();
            imageViewAddRecipe.setImageURI(imageUri);
        }
    });


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddRecipeFragment newInstance(String param1, String param2) {
        AddRecipeFragment fragment = new AddRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        imageViewAddRecipe = view.findViewById(R.id.imageView);
        imageViewAddRecipe.setOnClickListener(v -> openGallery());

        return view;
    }






    @Override
    public void onStart()
    {
        super.onStart();
        init();
    }
    private void init(){
        fbs = FirebaseServices.getInstance();
        utils = Utils.getInstance();
        etRecipeName = getView().findViewById(R.id.etRecipeName);
        etCookName = getView().findViewById(R.id.etCookName);
        etPrepTime = getView().findViewById(R.id.etPrepTime);
        etCookTime = getView().findViewById(R.id.etCookTime);
        etDescription = getView().findViewById(R.id.etDescription);
        etNotes = getView().findViewById(R.id.etNotes);
        spCategory = getView().findViewById(R.id.spCategory);
        spDifficulty = getView().findViewById(R.id.spDifficulty);
        ratingBar = getView().findViewById(R.id.ratingBar);
        btnSave = getView().findViewById(R.id.btnSave);
        imageViewAddRecipe = getView().findViewById(R.id.imageView);

        setupSpinners(); // منادي على الدالة عشان تشتغل الـ Spinners



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // adding to firestore  'Recipe' collection
                addToFirestore();

            }
        });
        imageViewAddRecipe.setOnClickListener(v -> openGallery());
    }


    // دالة جديدة لإعداد الـ Spinners
    private void setupSpinners() {
        // Spinner للفئة
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.categories_array,
                android.R.layout.simple_spinner_item
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                // يمكنك استخدام selectedCategory لاحقاً
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Spinner للصعوبة
        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.difficulties_array,
                android.R.layout.simple_spinner_item
        );
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDifficulty.setAdapter(difficultyAdapter);

        spDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDifficulty = parent.getItemAtPosition(position).toString();
                // يمكنك استخدام selectedDifficulty لاحقاً
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Toast.makeText(getActivity(), "Setup Spinners called!", Toast.LENGTH_SHORT).show();

    }

    private void addToFirestore(){
        // === 1. Read all fields ===
        String recipeName = etRecipeName.getText().toString().trim();
        String cookName = etCookName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();
        String difficulty = spDifficulty.getSelectedItem().toString();

        float rating = ratingBar.getRating();

        // الحصول على رابط الصورة من FirebaseServices
        String imageUri = fbs.getSelectedImageURL() != null ? fbs.getSelectedImageURL().toString() : "";

        int prepTime = 0;
        int cookTime = 0;

        // Convert numbers safely
        try {
            prepTime = Integer.parseInt(etPrepTime.getText().toString());
        } catch (Exception e) {}

        try {
            cookTime = Integer.parseInt(etCookTime.getText().toString());
        } catch (Exception e) {}

        // === 2. Validate required fields ===
        if (recipeName.isEmpty() || cookName.isEmpty() || description.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        // تحقق من الـ Spinners
        if (category.equals("Select Category")) {
            Toast.makeText(getActivity(), "Please select a category!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (difficulty.equals("Select Difficulty")) {
            Toast.makeText(getActivity(), "Please select a difficulty level!", Toast.LENGTH_SHORT).show();
            return;
        }

        // === 3. Create Recipe object ===
        Recipe recipe = new Recipe(
                recipeName,
                cookName,
                category,
                difficulty,
                rating,
                prepTime,
                cookTime,
                description,
                notes,
                imageUri
        );

        // === 4. Upload to Firebase ===
        fbs.getFire().collection("recipes")
                .add(recipe)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(), "Recipe added successfully!", Toast.LENGTH_SHORT).show();
                        clearForm();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Failed to add recipe: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearForm() {
        etRecipeName.setText("");
        etCookName.setText("");
        etPrepTime.setText("");
        etCookTime.setText("");
        etDescription.setText("");
        etNotes.setText("");
        ratingBar.setRating(3.0f);
        spCategory.setSelection(0);
        spDifficulty.setSelection(0);
        imageViewAddRecipe.setImageResource(android.R.drawable.ic_menu_gallery);

        // مسح الصورة المختارة
        fbs.setSelectedImageURL(null);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

//هاض الكود دبكراتتد هيك اشي انه جوجل كان ها الكود الها بس هي خلص طلعته من مسؤليتها واخترعت كود جديد بس الكود بشتغل وكلو تمام فخلص
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            imageViewAddRecipe.setImageURI(selectedImageUri);
            utils.uploadImage(getActivity(), selectedImageUri);
        }
    }
}

