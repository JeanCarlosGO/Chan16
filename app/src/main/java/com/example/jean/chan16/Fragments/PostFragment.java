package com.example.jean.chan16.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.jean.chan16.Bases.LocationFragment;
import com.example.jean.chan16.Models.Post;
import com.example.jean.chan16.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class PostFragment extends LocationFragment {

    private FirebaseUser user;
    private OnCreatePostListener listener;
    Uri imageUri;

    // Views
    View view;
    EditText postText;
    FloatingActionButton btnPost;
    ImageButton btnPickImage;
    ImageView postImage;
    View divider;

    private static final String ARG_USER = "user";
    private static final String TAG = "PostFragment";
    private static final int GALLERY_CODE = 234;
    private static final String INTENT_TYPE = "images/*";

    public PostFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        view = inflater.inflate(R.layout.fragment_create_post, container, false);
        postText = view.findViewById(R.id.postText);
        postImage = view.findViewById(R.id.postImage);
        divider = view.findViewById(R.id.divider);
        btnPickImage = view.findViewById(R.id.btnPickImage);
        btnPost = view.findViewById(R.id.btnPost);

        Glide.with(this).load(imageUri).into(postImage);

        btnPickImage.setOnClickListener((v) -> openGallery());

        btnPost.setOnClickListener((v) -> {
            if(TextUtils.isEmpty(postText.getText().toString())) {
                postText.setError("Required");
                return;
            }

            Post post = new Post(postText.getText().toString(),
                                imageUri.toString(),
                                user.getDisplayName(),
                                user.getPhotoUrl().toString(),
                                getCurrentDate(),
                                getAddressName());

            listener.onCreatePost(post);
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        user = (getArguments() != null)
                ? getArguments().getParcelable(ARG_USER)
                : FirebaseAuth.getInstance().getCurrentUser();

        imageUri = getUriToResource(Objects.requireNonNull(getContext()), R.drawable.ic_img_placeholder_yellow_24dp);

        if (context instanceof OnCreatePostListener) {
            listener = (OnCreatePostListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCreatePostListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            Image mImg = images.get(0);
            imageUri = Uri.parse(mImg.getPath());

            Glide.with(this).load(imageUri).into(postImage);

            postImage.setVisibility(View.VISIBLE);
            divider.setVisibility(View.VISIBLE);
        }
    }

    public void openGallery() {

        ImagePicker.with(this)
                    .setCameraOnly(false)
                    .setMultipleMode(false)
                    .setShowCamera(true)
                    .setDoneTitle("Done")
                    .setAlwaysShowDoneButton(true)
                    .setKeepScreenOn(true)
                    .start();
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault()).format(new Date());
    }

    public  interface OnCreatePostListener {
        void onCreatePost(Post post);
    }
}
