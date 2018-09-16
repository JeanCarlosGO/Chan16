package com.example.jean.chan16.Bases;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public abstract class DataActivity extends AuthActivity implements ValueEventListener {

    public FirebaseDatabase mDb;
    public DatabaseReference mPosts;
    public StorageReference mStorage;

    private static final String TAG = "DataActivity";
    public static final String STORAGE_PATH_UPLOADS = "posts/";
    public static final String DATABASE_PATH_UPLOADS = "posts/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = FirebaseDatabase.getInstance();
        mPosts = mDb.getReference("post");
        mStorage = FirebaseStorage.getInstance().getReference();

        DatabaseReference mPostsRef = mDb.getReference(DATABASE_PATH_UPLOADS);
        mPostsRef.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        Object value = dataSnapshot.getValue();
        Log.d(TAG,"Value: " + value);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.w(TAG,"Failed to read value", databaseError.toException());
    }
}
