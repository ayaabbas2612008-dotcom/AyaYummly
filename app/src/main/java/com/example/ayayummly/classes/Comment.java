package com.example.ayayummly.classes;

public class Comment {
    private String commentText;
    private String userName;
    private String userId;
    private String recipeId;

    // Constructor فاضي - أساسي للفايربيس
    public Comment() {}

    public Comment(String commentText, String userName, String userId, String recipeId) {
        this.commentText = commentText;
        this.userName = userName;
        this.userId = userId;
        this.recipeId = recipeId;
    }

    // Getters & Setters
    public String getCommentText() { return commentText; }
    public void setCommentText(String commentText) { this.commentText = commentText; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRecipeId() { return recipeId; }
    public void setRecipeId(String recipeId) { this.recipeId = recipeId; }
}