package com.example.jean.chan16.Fragments;


import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.jean.chan16.Bases.BaseFragment;
import com.example.jean.chan16.MainActivity;
import com.example.jean.chan16.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class ProfileFragment extends BaseFragment {

    FirebaseUser user;

    // Views
    View view;
    ImageView userImage;
    TextView userName;
    Button btnSignOut;

    private static final String TAG = "ProfileFragment";
    private static final String ARG_USER = "user";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        user = (getArguments() != null)
                ? getArguments().getParcelable(ARG_USER)
                : FirebaseAuth.getInstance().getCurrentUser();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        userImage = view.findViewById(R.id.profileUserImage);
        userName = view.findViewById(R.id.profileUserName);
        btnSignOut = view.findViewById(R.id.profileBtnSignOut);

        // Set User Avatar Image
        Uri imageUri = (user.getPhotoUrl() != null)
                        ? user.getPhotoUrl()
                        : getUriToResource(Objects.requireNonNull(getContext()),R.drawable.ic_account_circle_primary_64dp);
        Glide.with(view).load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(userImage);

        // Set User Name
        userName.setText(user.getDisplayName());

        btnSignOut.setOnClickListener((v) -> {
            Log.d(TAG, TAG + ":btnSignOut.OnClick");
            ((MainActivity)Objects.requireNonNull(getActivity())).signOut();
        });

        return view;
    }

}
