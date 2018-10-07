package com.example.jean.chan16;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;

import com.example.jean.chan16.Bases.DataActivity;
import com.example.jean.chan16.Fragments.DashboardFragment;
import com.example.jean.chan16.Fragments.PostFragment;
import com.example.jean.chan16.Fragments.ProfileFragment;
import com.example.jean.chan16.Models.Post;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MainActivity extends DataActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        PostFragment.OnCreatePostListener,
        DashboardFragment.DataProvider {

    List<Post> mValues;

    PostFragment postFragment;
    DashboardFragment dashboardFragment;
    ProfileFragment profileFragment;

    // Views
    ConstraintLayout container;
    BottomNavigationView navigation;

    private static final String TAG = "Main";
    private static final String ARG_USER = "user";
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init Fragments
        postFragment = new PostFragment();
        dashboardFragment = new DashboardFragment();
        profileFragment = new ProfileFragment();

        container = findViewById(R.id.container);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        bundle = new Bundle();
        bundle.putParcelable(ARG_USER, mAuth.getCurrentUser());

        loadContent(profileFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.navigation_post:
                fragment = postFragment;
                break;
            case R.id.navigation_dashboard:
                fragment = dashboardFragment;
                break;
            case R.id.navigation_profile:
                fragment = profileFragment;
                break;
            default:
                fragment = null;
        }

        if(fragment != null) {
            fragment.setArguments(bundle);
            return loadContent(fragment);
        }

        return false;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        super.onDataChange(dataSnapshot);

        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            Post post = postSnapshot.getValue(Post.class);

            mValues.add(post);
        }
    }

    @Override
    public void onCreatePost(Post post) {
        Uri postImageUri = Uri.parse(post.getImageUri());
        Uri postAuthorProfileImageUri = Uri.parse(post.getAuthorProfileImageUri());

        if(post.getImageUri() != null) {
            StorageReference mStoreRef = mStorage.child(STORAGE_PATH_UPLOADS +
                                                        System.currentTimeMillis() + "." +
                                                        getFileExtension(postImageUri));
            mStoreRef.putFile(postImageUri).addOnSuccessListener((task) -> {
                String postId = mPosts.push().getKey();
                mPosts.child(postId).setValue(post);

            }).addOnFailureListener((e) -> {
                hideProgressDialog();
                Log.w(TAG, "Failed to make Post: " + e.getMessage());
                showMessage(container, "Failed to make Post: + e.getMessage()");

            }).addOnProgressListener((it) -> {
                showProgressDialog("Posting...");
            });

            loadContent(dashboardFragment);
        }
    }

    private boolean loadContent(Fragment fragment) {
        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainContent, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void updateUI(FirebaseUser user) {
        super.updateUI(user);

        if(user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public List<Post> getData() {
        return mValues;
    }
}
