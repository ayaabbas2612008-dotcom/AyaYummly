package com.example.ayayummly;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayayummly.classes.FirebaseServices;
import com.example.ayayummly.classes.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    // TextViews
    private TextView tvTitle, tvChefName, tvCategory;
    private TextView tvPrepTime, tvCookTime, tvTotalTime;
    private TextView tvLevel, tvCookingTip, tvServings;
    private TextView tvIngredients, tvSteps, tvNotes;
    private TextView tvPlaceholder;


    // ImageView
    private ImageView ivRecipePhoto;

    // RatingBar
    private RatingBar ratingBar;

    // Buttons (FloatingActionButtons)
    private FloatingActionButton btnFav, btnShare;
    private Button btnStartCooking;
    private Button btnStopCooking;



    // Recipe object
    private Recipe myRecipe;

    // For image enlarge animation
    private boolean isEnlarged = false;
    private boolean isFav = false;

    // Timer
    private CountDownTimer cookingTimer;
    private long timeLeftMillis;
    private boolean isTimerRunning = false;
    private boolean isPaused = false;



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
        init();// تعبئة البيانات+ ربط العناصر + استلام الوصفة

        //  وها للصورة
        //  نسخة محسّنة من الكود بحيث الصورة تكبر وتصغر بأنيميشن سلس بدل ما يتغير حجمها فجأة
        //  وهاض الجديد
        ivRecipePhoto.setOnClickListener(v -> {
            if (isEnlarged) {
                ivRecipePhoto.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .start();
            } else {
                ivRecipePhoto.animate()
                        .scaleX(1.8f)
                        .scaleY(1.8f)
                        .setDuration(300)
                        .start();
            }

            isEnlarged = !isEnlarged;
        });

        /*
        //هاض تاع الصورة القديم
        // تكبير وتصغير صورة الوصفة
        ivRecipePhoto.setOnClickListener(v -> {
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

            // تغيير حالة الصورة
            isEnlarged = !isEnlarged;
        });

         */

        // زر المفضلة
        btnFav.setOnClickListener(v -> {
            btnFav.animate()
                    .scaleX(1.3f)
                    .scaleY(1.3f)
                    .setDuration(150)
                    .withEndAction(() -> {
                        btnFav.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(150)
                                .start();
                    })
                    .start();

            if (isFav) {
                btnFav.setImageResource(R.drawable.ic_favorite_border);
            } else {
                btnFav.setImageResource(R.drawable.ic_favorite);
            }

            isFav = !isFav;
        });


        // زر المشاركة
        btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");

            String text = "🍽 " + myRecipe.getRecipeName() +
                    "\n\nIngredients:\n" + myRecipe.getIngredients() +
                    "\n\nSteps:\n" + myRecipe.getSteps();

            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(intent, "Share recipe via"));
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
        // تعبئة البيانات+ ربط العناصر + استلام الوصفة

        fbs = FirebaseServices.getInstance();

        // العنوان + الشيف + التصنيف
        tvTitle = getView().findViewById(R.id.tvTitle);
        tvChefName = getView().findViewById(R.id.tvChefName);
        tvCategory = getView().findViewById(R.id.tvCategory);

        // أوقات الطبخ
        tvPrepTime = getView().findViewById(R.id.tvPrepTime);
        tvCookTime = getView().findViewById(R.id.tvCookTime);
        tvTotalTime = getView().findViewById(R.id.tvTotalTime);

        // Level + Tip + Servings
        tvLevel = getView().findViewById(R.id.tvLevel);
        tvCookingTip = getView().findViewById(R.id.tvCookingTip);
        tvServings = getView().findViewById(R.id.tvServings);

        // المكوّنات + الخطوات + الملاحظات
        tvIngredients = getView().findViewById(R.id.tvIngredients);
        tvSteps = getView().findViewById(R.id.tvSteps);
        tvNotes = getView().findViewById(R.id.tvNotes);

        // الصورة
        ivRecipePhoto = getView().findViewById(R.id.ivRecipe);
        // الـ Placeholder
        tvPlaceholder = getView().findViewById(R.id.tvPlaceholder);


        // التقييم
        ratingBar = getView().findViewById(R.id.ratingBar);

        // أزرار المشاركة والمفضلة
        btnFav = getView().findViewById(R.id.btnFav);
        btnShare = getView().findViewById(R.id.btnShare);

        btnStartCooking = getView().findViewById(R.id.btnStartCooking);
        btnStopCooking = getView().findViewById(R.id.btnStopCooking);


        // استلام الوصفة من الـ Bundle
        Bundle args = getArguments();
        if (args != null) {
            myRecipe = args.getParcelable("recipe");
        }

            if (myRecipe != null) {
                // تعبئة البيانات

                // العنوان + الشيف + التصنيف
                tvTitle.setText(myRecipe.getRecipeName());
                tvChefName.setText(myRecipe.getCookName());
                tvCategory.setText(myRecipe.getCategory());

                // أوقات الطبخ
                tvPrepTime.setText(myRecipe.getPrepTime() + " min");
                tvCookTime.setText(myRecipe.getCookTime() + " min");
                // حساب الوقت الكلي
                int totalTime = myRecipe.getPrepTime() + myRecipe.getCookTime();
                tvTotalTime.setText(totalTime + " min");

                // Level + Servings
                tvLevel.setText(myRecipe.getDifficulty());
                tvServings.setText(myRecipe.getServings() + " servings");

                // المكوّنات + الخطوات + الملاحظات
                tvIngredients.setText(myRecipe.getIngredients());
                tvSteps.setText(myRecipe.getSteps());
                tvNotes.setText(myRecipe.getNotes());

                // التقييم
                ratingBar.setRating(myRecipe.getRating());

                 // الصورة
                // ✔ الصورة بتنعرض إذا موجودة
                //✔ والـ Placeholder بطلع إذا مش موجودة
                // صورة افتراضية من drawable
                if (myRecipe.getImageUri() == null || myRecipe.getImageUri().isEmpty()) {
                    ivRecipePhoto.setVisibility(View.GONE);
                    tvPlaceholder.setVisibility(View.VISIBLE);
                } else {
                    // تحميل صورة الوصفة من الإنترنت
                    tvPlaceholder.setVisibility(View.GONE);
                    ivRecipePhoto.setVisibility(View.VISIBLE);
                    Picasso.get().load(myRecipe.getImageUri()).into(ivRecipePhoto);
                }

                // التايمر
                setupCookingTimer(totalTime);
            }
        }

    private void setupCookingTimer(int totalMinutes) {

        // تحويل الوقت الأصلي لملي ثانية
        timeLeftMillis = totalMinutes * 60 * 1000;

        // زر Start / Pause / Resume
        btnStartCooking.setOnClickListener(v -> {

            // الحالة 1: Start
            if (!isTimerRunning && !isPaused) {
                startTimer(timeLeftMillis);
                btnStartCooking.setText("Pause");
                isTimerRunning = true;
            }

            // الحالة 2: Pause
            else if (isTimerRunning) {
                cookingTimer.cancel();
                isTimerRunning = false;
                isPaused = true;
                btnStartCooking.setText("Resume");
            }

            // الحالة 3: Resume
            else if (isPaused) {
                startTimer(timeLeftMillis);
                isPaused = false;
                isTimerRunning = true;
                btnStartCooking.setText("Pause");
            }
        });

        // زر الإيقاف الكامل (Reset)
        btnStopCooking.setOnClickListener(v -> {

            if (cookingTimer != null) {
                cookingTimer.cancel();
            }

            isTimerRunning = false;
            isPaused = false;

            // إعادة الوقت الأصلي
            timeLeftMillis = totalMinutes * 60 * 1000;

            // رجوع الزر للوضع الطبيعي
            btnStartCooking.setText("Start Cooking");

            Toast.makeText(getActivity(), "⛔ Timer stopped", Toast.LENGTH_SHORT).show();
        });
    }
    private void startTimer(long millis) {

        cookingTimer = new CountDownTimer(millis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;

                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;

                btnStartCooking.setText("Time left: " + minutes + ":" + String.format("%02d", seconds));
            }

            @Override
            public void onFinish() {
                btnStartCooking.setText("Done!");
                isTimerRunning = false;
                isPaused = false;

                // Toast لطيف
                Toast.makeText(getActivity(), "⏰ Time is up!", Toast.LENGTH_LONG).show();

                // Animation لطيفة
                btnStartCooking.animate()
                        .rotationBy(5).setDuration(60)
                        .withEndAction(() ->
                                btnStartCooking.animate()
                                        .rotationBy(-5).setDuration(60)
                                        .start()
                        ).start();

                // رجوع الزر للوضع الطبيعي
                btnStartCooking.setText("Start Cooking");
            }
        }.start();
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