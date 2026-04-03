package com.example.ayayummly.classes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayayummly.MainActivity;
import com.example.ayayummly.R;
import com.example.ayayummly.RecipeDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AllRecipesAdapter  extends RecyclerView.Adapter<AllRecipesAdapter.MyViewHolder> {

     Context context;
     ArrayList<Recipe> AllRecipes;
    private FirebaseServices fbs;

    private AllRecipesAdapter.OnItemClickListener itemClickListener;


    public AllRecipesAdapter(Context context, ArrayList<Recipe> AllRecipes) {
        this.context = context;
        this.AllRecipes = AllRecipes;
        this.fbs = FirebaseServices.getInstance();
        this.itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                /*
                 String selectedRecipe = AllRecipes.get(position).getRecipeName();
                Toast.makeText(context, "Clicked: " + selectedRecipe, Toast.LENGTH_SHORT).show();
                 */
                Bundle args = new Bundle();
                args.putParcelable("recipe", AllRecipes.get(position)); // or use Parcelable for better performance
                RecipeDetailsFragment recipeDetails = new RecipeDetailsFragment();
                recipeDetails.setArguments(args);
                FragmentTransaction ft= ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,recipeDetails);
                ft.commit();
            }
        } ;
    }

    @NonNull
    @Override
    public AllRecipesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return  new AllRecipesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllRecipesAdapter.MyViewHolder holder, int position) {
            // 1. 🎯 يجيب الوصفة من القائمة
            Recipe recipe = AllRecipes.get(position);

            /*
              User u = fbs.getCurrentUser();
        if (u != null)
        {
            if (u.getFavorites().contains(car.getId()))
                Picasso.get().load(R.drawable.favcheck).into(holder.ivFavourite);
            else
                Picasso.get().load(R.drawable.ic_fav).into(holder.ivFavourite);
        }
             */

            // 2. 🏷️ يملأ البيانات في العناصر
            holder.tvRecipeName.setText(recipe.getRecipeName());
            holder.tvCookAndCategory.setText(recipe.getCookName() + " • " + recipe.getCategory());
            holder.tvDifficulty.setText(recipe.getDifficulty());
            // ⏱️ الوقت
            holder.tvPrepTime.setText(recipe.getPrepTime() + " min");
            holder.tvCookTime.setText(recipe.getCookTime() + " min");
            holder.ratingBar.setRating(recipe.getRating());

//-----------------------------دفحص هاي------------------------------------------------
      /*
      //هاض اذا بدي احط الملاحظات بالكارد تاعت الريسايكل فيو
        // الملاحظات - إذا موجودة
            if (recipe.getNotes() != null && !recipe.getNotes().isEmpty()) {
                holder.tvNotes.setText(recipe.getNotes());
                holder.tvNotes.setVisibility(View.VISIBLE);
            } else {
                holder.tvNotes.setVisibility(View.GONE);
            }

       */
//-----------------------------دفحص هاي------------------------------------------------
          /*
            // يربط الضغط على اسم الوصفة
            holder.tvRecipeName.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(position);
                }
            });*/

        // يربط الضغط على كل البطاقة-الايتم-الكارد
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
            itemClickListener.onItemClick(position);
        }
        });


            //  🖼️ يحمل صورة الوصفة
        String img = recipe.getImageUri();

     // حماية من null أو قيم فاضية
        if (img == null || img.trim().isEmpty() || img.equals("null")) {
            img = null; // Picasso رح يستخدم placeholder
        }

        Picasso.get()
                .load(img)
                .placeholder(R.drawable.placeholder_recipe1)
                .error(R.drawable.placeholder_recipe1)
                .fit()
                .centerCrop()
                .into(holder.ivItemImage);

        //انا هون مش حاطه الاشياء تعون الفافورت المفضلة وهيك

    }



    @Override
    public int getItemCount() {
        return AllRecipes.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // العناصر من الـ XML
        ImageView ivItemImage;
        TextView tvRecipeName, tvCookAndCategory, tvDifficulty, tvPrepTime, tvCookTime;
       // TextView tvNotes;
        //هاض اذا بدي احط الملاحظات بالكارد تاعت الريسايكل فيو


        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // ربط كل العناصر من الـ XML
            ivItemImage = itemView.findViewById(R.id.ivItemImage);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvCookAndCategory = itemView.findViewById(R.id.tvCookAndCategory);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            tvPrepTime = itemView.findViewById(R.id.tvPrepTime);
            tvCookTime = itemView.findViewById(R.id.tvCookTime);
            //tvNotes = itemView.findViewById(R.id.tvNotes);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }




    //الها علاقة بصفحة التفاصيل
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //الها علاقة بصفحة التفاصيل
    public void setOnItemClickListener(AllRecipesAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void filterList(ArrayList<Recipe> filteredList) {
        this.AllRecipes = filteredList;
        notifyDataSetChanged();
    }

}
