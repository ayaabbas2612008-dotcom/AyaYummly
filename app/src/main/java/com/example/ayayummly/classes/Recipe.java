package com.example.ayayummly.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Recipe implements Parcelable {

    private String recipeName;
    private String cookName;
    private String category;
    private String difficulty;
    private float rating;
    private int prepTime;
    private int cookTime;
    private String description;
    private String notes;
    private String imageUri;

    public Recipe() {
    }

    // Constructor
    public Recipe(String recipeName, String cookName, String category, String difficulty,
                  float rating, int prepTime, int cookTime, String description,
                  String notes, String imageUri) {
        this.recipeName = recipeName;
        this.cookName = cookName;
        this.category = category;
        this.difficulty = difficulty;
        this.rating = rating;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.description = description;
        this.notes = notes;
        this.imageUri = imageUri;
    }

    // Constructor from Parcel
    protected Recipe(Parcel in) {
        recipeName = in.readString();
        cookName = in.readString();
        category = in.readString();
        difficulty = in.readString();
        rating = in.readFloat();
        prepTime = in.readInt();
        cookTime = in.readInt();
        description = in.readString();
        notes = in.readString();
        imageUri = in.readString();
    }

    // CREATOR
    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    // Getters
    public String getRecipeName() { return recipeName; }
    public String getCookName() { return cookName; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public float getRating() { return rating; }
    public int getPrepTime() { return prepTime; }
    public int getCookTime() { return cookTime; }
    public String getDescription() { return description; }
    public String getNotes() { return notes; }
    public String getImageUri() { return imageUri; }

    // Setters
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }
    public void setCookName(String cookName) { this.cookName = cookName; }
    public void setCategory(String category) { this.category = category; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setRating(float rating) { this.rating = rating; }
    public void setPrepTime(int prepTime) { this.prepTime = prepTime; }
    public void setCookTime(int cookTime) { this.cookTime = cookTime; }
    public void setDescription(String description) { this.description = description; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeName='" + recipeName + '\'' +
                ", cookName='" + cookName + '\'' +
                ", category='" + category + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", rating=" + rating +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", description='" + description + '\'' +
                ", notes='" + notes + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Write all fields to Parcel
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(recipeName);
        dest.writeString(cookName);
        dest.writeString(category);
        dest.writeString(difficulty);
        dest.writeFloat(rating);
        dest.writeInt(prepTime);
        dest.writeInt(cookTime);
        dest.writeString(description);
        dest.writeString(notes);
        dest.writeString(imageUri);
    }
}
