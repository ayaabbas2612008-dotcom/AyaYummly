package com.example.ayayummly;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.ayayummly.classes.FirebaseServices;
import com.example.ayayummly.classes.Recipe;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailsFragment extends Fragment {

    private static final int PERMISSION_SEND_SMS = 1;
    private static final int REQUEST_CALL_PERMISSION = 2;
    private FirebaseServices fbs;

        // عناصر الواجهة (Views)
        private TextView tvTitle, tvDescription, tvIngredients, tvSteps, tvNotes;
        private TextView tvTime, tvLevel, tvServings; // معلومات سريعة
        private ImageView ivRecipePhoto;
        private RatingBar ratingBar;
        private Button btnShare, btnFav;

    private Recipe myRecipe;
    private boolean isEnlarged = false;

    private static final String ARG_RECIPE = "recipe";










    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeDetailsFragment newInstance(String param1, String param2) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public void onStart() {
        super.onStart();
        init();
//  وها للصورة
//  نسخة محسّنة من الكود بحيث الصورة تكبر وتصغر بأنيميشن سلس بدل ما يتغير حجمها فجأة
        ImageView ivRecipePhoto = getView().findViewById(R.id.ivRecipe);

        ivRecipePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startHeight = ivRecipePhoto.getHeight();
                int endHeight;

                if (isEnlarged) {
                    endHeight = 500;   // حجم صغير
                } else {
                    endHeight = 2200; // حجم كبير
                }

                // أنيميشن لتغيير الحجم تدريجياً
                ValueAnimator animator = ValueAnimator.ofInt(startHeight, endHeight);
                animator.setDuration(500); // نصف ثانية
                animator.addUpdateListener(animation -> {
                    int animatedValue = (int) animation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = ivRecipePhoto.getLayoutParams();
                    layoutParams.height = animatedValue;
                    ivRecipePhoto.setLayoutParams(layoutParams);
                });
                animator.start();

                // نقلب حالة الصورة (مكبرة/مصغرة)
                isEnlarged = !isEnlarged;
            }
        });
//بشبه كود الاستاذ
//        ImageView ivRecipePhoto = getView().findViewById(R.id.ivRecipe);
//
//        ivRecipePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ViewGroup.LayoutParams layoutParams = ivRecipePhoto.getLayoutParams();
//                if (isEnlarged) {
//                    layoutParams.height = 500;   // حجم صغير
//                } else {
//                    layoutParams.height = 2200; // حجم كبير
//                }
//                ivRecipePhoto.setLayoutParams(layoutParams);
//
//                // نقلب حالة الصورة (مكبرة/مصغرة)
//                isEnlarged = !isEnlarged;
//            }
//        });
    }

    private void init() {

        fbs= FirebaseServices.getInstance();

        tvTitle = getView().findViewById(R.id.tvTitle);
        tvDescription = getView().findViewById(R.id.tvDescription);
        tvIngredients = getView().findViewById(R.id.tvIngredients);
        tvSteps = getView().findViewById(R.id.tvSteps);
        tvNotes = getView().findViewById(R.id.tvNotes);

        tvTime = getView().findViewById(R.id.tvTime);
        tvLevel = getView().findViewById(R.id.tvLevel);
        tvServings = getView().findViewById(R.id.tvServings);

        ivRecipePhoto = getView().findViewById(R.id.ivRecipe);
        ratingBar = getView().findViewById(R.id.ratingBar);

        btnFav = getView().findViewById(R.id.btnFav);
        btnShare = getView().findViewById(R.id.btnShare);

        // استلام الوصفة من الـ Bundle
        Bundle args = getArguments();
        if (args != null) {
            myRecipe = args.getParcelable("recipe");

            if (myRecipe != null) {
                // تعبئة البيانات
                tvTitle.setText(myRecipe.getRecipeName());
                tvDescription.setText(myRecipe.getDescription());
                tvIngredients.setText(myRecipe.getIngredients());
                tvSteps.setText(myRecipe.getSteps());
                tvNotes.setText(myRecipe.getNotes());

                // معلومات سريعة
                tvTime.setText("⏱ " + myRecipe.getPrepTime() + " min");
                tvLevel.setText("🔥 " + myRecipe.getDifficulty());
                tvServings.setText("🍽 " + myRecipe.getServings());

                // التقييم
                ratingBar.setRating(myRecipe.getRating());

                // الصورة
                if (myRecipe.getImageUri() == null || myRecipe.getImageUri().isEmpty()) {
                    Picasso.get().load(R.drawable.ic_placeholder).into(ivRecipePhoto);
                } else {
                    Picasso.get().load(myRecipe.getImageUri()).into(ivRecipePhoto);
                }
            }

            }




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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_details, container, false);
    }
}