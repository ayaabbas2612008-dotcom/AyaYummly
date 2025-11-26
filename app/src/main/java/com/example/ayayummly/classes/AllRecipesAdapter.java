package com.example.ayayummly.classes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayayummly.R;

import java.util.ArrayList;

public class AllRecipesAdapter  extends RecyclerView.Adapter<AllRecipesAdapter.MyViewHolder> {

     Context context;
     ArrayList<Recipe> AllRecipes;
    private FirebaseServices fbs;
    /*
    //الها علاقة بصفحة التفاصيل
    private AllRecipesAdapter.OnItemClickListener itemClickListener;
    */

    public AllRecipesAdapter(Context context, ArrayList<Recipe> AllRecipes) {
        this.context = context;
        this.AllRecipes = AllRecipes;
        this.fbs = FirebaseServices.getInstance();

        /*
        //هاض الكود لازم ازبطه لتطبيقي انا وهاض الكود اله علاقة بالتفاصيل
        this.itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // من هون
                String selectedItem = filteredList.get(position).getNameCar();
                Toast.makeText(getActivity(), "Clicked: " + selectedItem, Toast.LENGTH_SHORT).show();
                //لهون الاستاذ كان مخفي الاسطر ببرويكته
                Bundle args = new Bundle();
                args.putParcelable("car", carsList.get(position)); // or use Parcelable for better performance
                CarDetailsFragment cd = new CarDetailsFragment();
                cd.setArguments(args);
                FragmentTransaction ft= ((MainActivity)context).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout,cd);
                ft.commit();
            }
        } ;
        */

    }



    @NonNull
    @Override
    public AllRecipesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return  new AllRecipesAdapter.MyViewHolder(v);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // العناصر من الـ XML
        ImageView ivItemImage;
        TextView tvRecipeName, tvCookAndCategory, tvDifficulty, tvTime, tvDescription, tvNotes;
       RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // ربط كل العناصر من الـ XML
            ivItemImage = itemView.findViewById(R.id.ivItemImage);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvCookAndCategory = itemView.findViewById(R.id.tvCookAndCategory);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull AllRecipesAdapter.MyViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return AllRecipes.size();
    }

    /*
    //الها علاقة بصفحة التفاصيل
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    //الها علاقة بصفحة التفاصيل
    public void setOnItemClickListener(AllRecipesAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
    */
}
