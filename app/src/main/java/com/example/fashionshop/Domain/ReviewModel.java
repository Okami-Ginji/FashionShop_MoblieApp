package com.example.fashionshop.Domain;

public class ReviewModel {
    private String reviewer;
    private String comment;

    public ReviewModel() {
        // Required for Firebase
    }

    public ReviewModel(String reviewer, String comment) {
        this.reviewer = reviewer;
        this.comment = comment;
    }

    public String getReviewer() {
        return reviewer;
    }
    public String getComment() {
        return comment;
    }
}

