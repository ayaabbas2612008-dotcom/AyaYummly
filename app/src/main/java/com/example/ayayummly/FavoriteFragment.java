package com.example.ayayummly;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ayayummly.classes.FirebaseServices;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import com.example.ayayummly.classes.AllRecipesAdapter;
import com.example.ayayummly.classes.Recipe;
public class FavoriteFragment extends Fragment {

    private RecyclerView rvFavorite;
    private FirebaseServices fbs;
    private ArrayList<Recipe> favList = new ArrayList<>();
    private AllRecipesAdapter adapter;

    public FavoriteFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fbs = FirebaseServices.getInstance();
        rvFavorite = view.findViewById(R.id.rvFavorite);
        rvFavorite.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadFavorites();
    }

    private void loadFavorites() {
        String myId = fbs.getAuth().getUid();
        if (myId == null) return;

        fbs.getFire().collection("recipes")
                .whereArrayContains("favUsers", myId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    favList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Recipe r = doc.toObject(Recipe.class);
                        favList.add(r);
                    }

                    // التعديل هون: إذا الـ adapter لسا مش معمول، بنعمله. إذا موجود بس بنحدثه.
                    if (adapter == null) {
                        adapter = new AllRecipesAdapter(getActivity(), favList);
                        rvFavorite.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    if (getContext() != null)
                        Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
/*
    private void loadFavorites() {
        fbs.getFire().collection("recipes")
                .whereEqualTo("fav", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    favList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Recipe r = doc.toObject(Recipe.class);
                        favList.add(r);
                    }

                    adapter = new AllRecipesAdapter(getActivity(), favList);
                    rvFavorite.setAdapter(adapter);
                });
    }


 */

}
