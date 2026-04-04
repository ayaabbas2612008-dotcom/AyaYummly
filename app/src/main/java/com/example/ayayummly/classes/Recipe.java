package com.example.ayayummly.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import java.util.ArrayList;


/*public class Recipe implements Parcelable {

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


 */
//gemeni

public class Recipe implements Parcelable {

    // الحقول الأساسية (كما هي في مشروعك)
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
    private String id;
    private String ownerId;
    private boolean isFav; // الحقل القديم (لضمان عدم حدوث أخطاء في الكود الحالي)

    // ⭐ الحقل الجديد للمفضلة الشخصية
    private ArrayList<String> favUsers;

    private ArrayList<Comment> comments;

    // 1. الكونسيركتور الفارغ (ضروري لـ Firebase)
    public Recipe() {
        this.favUsers = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    // 2. الكونسيركتور الكامل (لإضافة وصفة جديدة)
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
        this.isFav = false;
        this.favUsers = new ArrayList<>(); // تهيئة المصفوفة لتكون جاهزة للاستخدام
    }

    // 3. كونسيركتور الـ Parcel (لنقل البيانات بين الشاشات)
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
        favUsers = in.createStringArrayList(); // قراءة مصفوفة المعجبين
    }

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

    // --- Getters & Setters ---
    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

    public String getCookName() { return cookName; }
    public void setCookName(String cookName) { this.cookName = cookName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public int getPrepTime() { return prepTime; }
    public void setPrepTime(int prepTime) { this.prepTime = prepTime; }

    public int getCookTime() { return cookTime; }
    public void setCookTime(int cookTime) { this.cookTime = cookTime; }

    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }

    public String getSteps() { return steps; }
    public void setSteps(String steps) { this.steps = steps; }

    public String getServings() { return servings; }
    public void setServings(String servings) { this.servings = servings; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public boolean isFav() { return isFav; }
    public void setFav(boolean fav) { isFav = fav; }

    // ⭐ Getter & Setter للمصفوفة الجديدة
    public ArrayList<String> getFavUsers() {
        if (favUsers == null) favUsers = new ArrayList<>();
        return favUsers;
    }
    public void setFavUsers(ArrayList<String> favUsers) { this.favUsers = favUsers; }

    public ArrayList<Comment> getComments() {
        if (comments == null) comments = new ArrayList<>();
        return comments;
    }
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }


    @Override
    public int describeContents() { return 0; }

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
        dest.writeStringList(favUsers); // حفظ مصفوفة المعجبين في الـ Parcel
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", favUsers=" + favUsers +
                '}';
    }
}
