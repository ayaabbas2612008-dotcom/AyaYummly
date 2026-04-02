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
    private String notes;
    private String imageUri;
    private String ingredients;
    private String steps;
    private String servings;
    private String id; // هذا لتخزين ID الوصفة من فايربيز، هوية الوصفة

    private String ownerId;// رقم صاحب الوصفة (UID)، هوية صاحب الوصفة

    private boolean isFav; //للمفضلة


    public Recipe() {
    }

    // Constructor

    public Recipe(String recipeName, String cookName, String category, String difficulty,
                  float rating, int prepTime, int cookTime, String ingredients, String steps,
                  String servings, String notes, String imageUri, String id, String ownerId) {

        this.recipeName = recipeName;
        this.cookName = cookName;
        this.category = category;
        this.difficulty = difficulty;
        this.rating = rating;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.notes = notes;
        this.imageUri = imageUri;
        this.id = id;
        this.ownerId = ownerId;
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
        ingredients = in.readString();
        steps = in.readString();
        servings = in.readString();
        notes = in.readString();
        imageUri = in.readString();
        id = in.readString();
        ownerId = in.readString();
        isFav = in.readByte() != 0;


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

    public String getIngredients() { return ingredients; }
    public String getSteps() { return steps; }
    public String getServings() { return servings; }    public String getNotes() { return notes; }
    public String getImageUri() { return imageUri; }

    public String getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public boolean isFav() { return isFav; }

    // Setters
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }
    public void setCookName(String cookName) { this.cookName = cookName; }
    public void setCategory(String category) { this.category = category; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public void setRating(float rating) { this.rating = rating; }
    public void setPrepTime(int prepTime) { this.prepTime = prepTime; }
    public void setCookTime(int cookTime) { this.cookTime = cookTime; }

    public void setIngredients(String ingredients) {this.ingredients = ingredients;}
    public void setSteps(String steps) {this.steps = steps;}
    public void setServings(String servings) {this.servings = servings;}
    public void setNotes(String notes) { this.notes = notes; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public void setId(String id) { this.id = id; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
    public void setFav(boolean fav) { isFav = fav; }



    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' + // أضفنا الـ ID هنا في البداية
                ", recipeName='" + recipeName + '\'' +
                ", cookName='" + cookName + '\'' +
                ", category='" + category + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", rating=" + rating +
                ", prepTime=" + prepTime +
                ", cookTime=" + cookTime +
                ", ingredients='" + ingredients + '\'' +
                ", steps='" + steps + '\'' +
                ", servings='" + servings + '\''+
                ", notes='" + notes + '\'' +
                ", imageUri='" + imageUri + '\''  +
                ", ownerId='" + ownerId + '\'' +

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
        dest.writeString(ingredients);
        dest.writeString(steps);
        dest.writeString(servings);
        dest.writeString(notes);
        dest.writeString(imageUri);
        dest.writeString(id);
        dest.writeString(ownerId);
        dest.writeByte((byte) (isFav ? 1 : 0));

    }


}
