package com.example.jean.chan16.Models;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import lombok.Data;

@Data
public class Post {
    // This is a simple POJO made with Project Lombok
    // For more information go to https://projectlombok.org/
    private String text;
    private Uri imageUri;
    private FirebaseUser author;
    private String date;
    private String location;

    public Post() {
    }

    public Post(String text, Uri imageUri, FirebaseUser author, String date, String location) {
        this.text = text;
        this.imageUri = imageUri;
        this.author = author;
        this.date = date;
        this.location = location;
    }
}
