package com.example.ayayummly;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ayayummly.classes.FirebaseServices;
import com.example.ayayummly.classes.Recipe;
import com.example.ayayummly.classes.Utils;
import com.squareup.picasso.Picasso;


public class EditRecipeFragment extends Fragment {
    // تعريف العناصر (UI Elements)
    private EditText etRecipeName, etCookName, etPrepTime, etCookTime, etNotes;
    private EditText etServings, etIngredients, etSteps;
    private Spinner spCategory, spDifficulty;
    private RatingBar ratingBar;
    private ImageView imageViewEditRecipe;
    private Button btnUpdate, btnChooseImage;
    private ProgressBar progressBar;

    private FirebaseServices fbs;
    private Utils utils;
    private String uploadedImageUrl = ""; // لتخزين رابط الصورة (سواء القديم أو الجديد بعد الرفع)
    private Recipe recipeToEdit; // الكائن الذي سنستلمه للتعديل

    public EditRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_recipe, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. استلام كائن الوصفة من صفحة التفاصيل (عن طريق الـ Bundle)
        if (getArguments() != null) {
            recipeToEdit = getArguments().getParcelable("recipe");
            Toast.makeText(getContext(), "Recipe: " + recipeToEdit.getRecipeName(), Toast.LENGTH_SHORT).show();

        }

        // إذا لم تصل البيانات، نخرج من الصفحة لمنع الـ Crash
        if (recipeToEdit == null) {
            Toast.makeText(getActivity(), "Error: Recipe not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        init(view);
    }

    private void init(View view) {
        fbs = FirebaseServices.getInstance();
        utils = Utils.getInstance();

        // ربط العناصر البرمجية بالـ IDs الموجودة في الـ XML
        etRecipeName = view.findViewById(R.id.etRecipeName);
        etCookName = view.findViewById(R.id.etCookName);
        etPrepTime = view.findViewById(R.id.etPrepTime);
        etCookTime = view.findViewById(R.id.etCookTime);
        etServings = view.findViewById(R.id.etServings);
        etIngredients = view.findViewById(R.id.etIngredients);
        etSteps = view.findViewById(R.id.etSteps);
        etNotes = view.findViewById(R.id.etNotes);
        spCategory = view.findViewById(R.id.spCategory);
        spDifficulty = view.findViewById(R.id.spDifficulty);
        ratingBar = view.findViewById(R.id.ratingBar);
        btnUpdate = view.findViewById(R.id.btnUpdateRecipe);
        btnChooseImage = view.findViewById(R.id.btnChooseImage);
        imageViewEditRecipe = view.findViewById(R.id.imageView);
        progressBar = view.findViewById(R.id.progressBar);

        setupSpinners(); // تعبئة القوائم المنسدلة (Categories & Difficulty)

        // 2. تعبئة الحقول بالبيانات "القديمة" القادمة من قاعدة البيانات
        etRecipeName.setText(recipeToEdit.getRecipeName());
        etCookName.setText(recipeToEdit.getCookName());
        etPrepTime.setText(String.valueOf(recipeToEdit.getPrepTime()));
        etCookTime.setText(String.valueOf(recipeToEdit.getCookTime()));
        etIngredients.setText(recipeToEdit.getIngredients());
        etSteps.setText(recipeToEdit.getSteps());
        etNotes.setText(recipeToEdit.getNotes());
        etServings.setText(recipeToEdit.getServings());
        ratingBar.setRating(recipeToEdit.getRating());

        // الاحتفاظ برابط الصورة القديم في البداية
        uploadedImageUrl = recipeToEdit.getImageUri();
        if (uploadedImageUrl != null && !uploadedImageUrl.isEmpty()) {
            Picasso.get().load(uploadedImageUrl).into(imageViewEditRecipe);
        }

        // اختيار القيم الصحيحة في الـ Spinner بناءً على بيانات الوصفة
        setSpinnerValue(spCategory, recipeToEdit.getCategory());
        setSpinnerValue(spDifficulty, recipeToEdit.getDifficulty());

        // تشغيل الأزرار
        btnUpdate.setOnClickListener(v -> updateInFirestore());
        btnChooseImage.setOnClickListener(v -> openGallery());
        imageViewEditRecipe.setOnClickListener(v -> openGallery());
    }

    // وظيفة: تحديث البيانات في Firestore
    private void updateInFirestore() {
        // إظهار دائرة التحميل وتعطيل الزر لمنع الضغط المتكرر
        progressBar.setVisibility(View.VISIBLE);
        btnUpdate.setEnabled(false);

        String name = etRecipeName.getText().toString().trim();
        String cook = etCookName.getText().toString().trim();

        // التحقق من الحقول الإجبارية
        if (name.isEmpty() || cook.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            btnUpdate.setEnabled(true);
            Toast.makeText(getActivity(), "Name and Cook are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // تأمين الـ Spinner: إذا مش مختار شي، بنعطيه قيمة افتراضية
        String category = "Other"; // قيمة احتياطية
        if (spCategory.getSelectedItem() != null) {
            category = spCategory.getSelectedItem().toString();
        }

        String difficulty = "Easy"; // قيمة احتياطية
        if (spDifficulty.getSelectedItem() != null) {
            difficulty = spDifficulty.getSelectedItem().toString();
        }



        // إنشاء كائن الوصفة "الجديد" مع الاحتفاظ بالـ ID القديم
        /*
        Recipe updatedRecipe = new Recipe(
                name, cook,
                category,
                difficulty,
                ratingBar.getRating(),
                getSafeInt(etPrepTime), // استخدام دالة الأمان
                getSafeInt(etCookTime),
                etIngredients.getText().toString(),
                etSteps.getText().toString(),
                etServings.getText().toString(),
                etNotes.getText().toString(),
                uploadedImageUrl,
                recipeToEdit.getId() // أهم سطر: الحفاظ على الـ ID نفسه
        );

         */

        Recipe updatedRecipe = new Recipe(
                name,
                cook,
                category,
                difficulty,
                ratingBar.getRating(),
                getSafeInt(etPrepTime),
                getSafeInt(etCookTime),
                etIngredients.getText().toString(),
                etSteps.getText().toString(),
                etServings.getText().toString(),
                etNotes.getText().toString(),
                uploadedImageUrl,
                recipeToEdit.getId(),
                recipeToEdit.getOwnerId()
        );


        // إرسال البيانات لـ Firestore باستخدام الـ ID المحدد
        fbs.getFire().collection("recipes")
                .document(recipeToEdit.getId()) // نحدد أي وثيقة سنعدل
                .set(updatedRecipe) // استبدال البيانات القديمة بالجديدة
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Recipe Updated!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack(); // العودة للخلف بعد النجاح
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnUpdate.setEnabled(true);
                    Toast.makeText(getActivity(), "Update Failed!", Toast.LENGTH_SHORT).show();
                });
    }

    // دالة أمان: تمنع الـ Crash إذا كان حقل الأرقام فارغاً
    private int getSafeInt(EditText et) {
        String val = et.getText().toString().trim();
        return val.isEmpty() ? 0 : Integer.parseInt(val);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> catAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.categories_array, android.R.layout.simple_spinner_item);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(catAdapter);

        ArrayAdapter<CharSequence> diffAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.difficulties_array, android.R.layout.simple_spinner_item);
        diffAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDifficulty.setAdapter(diffAdapter);
    }


    // دالة مساعدة: تجعل الـ Spinner يختار القيمة المخزنة مسبقاً
    private void setSpinnerValue(Spinner spinner, String value) {
        // أضفنا <CharSequence> هنا لتطابق تعريف الـ Adapter الأصلي
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();

        if (adapter != null && value != null) {
            int pos = adapter.getPosition(value);
            if (pos >= 0) {
                spinner.setSelection(pos);
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    // مراقب اختيار الصورة ورفعها
    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    imageViewEditRecipe.setImageURI(uri); // عرض الصورة مؤقتاً للمستخدم
                    progressBar.setVisibility(View.VISIBLE); // تشغيل التحميل أثناء الرفع للسيرفر

                    utils.uploadImage(getActivity(), uri, new Utils.ImageUploadCallback() {
                        @Override
                        public void onUploadSuccess(String url) {
                            progressBar.setVisibility(View.GONE);
                            uploadedImageUrl = url; // تحديث الرابط بالرابط الجديد من Firebase Storage
                            Toast.makeText(getActivity(), "New Image Uploaded!", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onUploadFailure(Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Image upload failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

}
