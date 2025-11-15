package com.example.ayayummly.classes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayayummly.R;

import java.util.ArrayList;

public class AllRecipesAdapter  extends RecyclerView.Adapter<AllRecipesAdapter.MyViewHolder> {

     Context context;
     ArrayList<Recipe> AllRecipes;
    private FirebaseServices fbs;

    public AllRecipesAdapter(Context context, ArrayList<Recipe> carsList) {
        this.context = context;
        this.AllRecipes = AllRecipes;
        this.fbs = FirebaseServices.getInstance();

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View v) {
            super(v);
        }
    }
    @NonNull
    @Override
    public AllRecipesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return  new AllRecipesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllRecipesAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
