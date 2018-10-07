package com.example.jean.chan16.Models;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import lombok.Data;

@Data
public class Post {
    // This is a simple POJO made with Project Lombok
    // For more information go to https://projectlombok.org/
    private String text;
    private String imageUri;
    private String authorName;
    private String authorProfileImageUri;
    private String date;
    private String location;

    public Post() {
    }

    public Post(String text, String imageUri, String authorName, String authorProfileImageUri, String date, String location) {
        this.text = text;
        this.imageUri = imageUri;
        this.authorName = authorName;
        this.authorProfileImageUri = authorProfileImageUri;
        this.date = date;
        this.location = location;
    }

    public Post(String text, String authorName, String authorProfileImageUri, String date, String location) {
        this.text = text;
        this.authorName = authorName;
        this.authorProfileImageUri = authorProfileImageUri;
        this.date = date;
        this.location = location;
    }
}
