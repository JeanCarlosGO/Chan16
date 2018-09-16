package com.example.jean.chan16.Bases;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.jean.chan16.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public abstract class AuthActivity extends BaseActivity {

    public FirebaseAuth mAuth;
    public GoogleSignInClient mGoogleSignInClient;

    final String TAG = "Auth";
    private final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        updateUI(user);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = (GoogleSignInAccount) task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                updateUI(null);
            }
        }
    }

    public void signInWithEmailAndPassword(View view, String email, String password) {
        Log.d(TAG, "sigIn: " + email);

        showProgressDialog("Authenticating...");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());

                        showMessage(view, "Error: ${task.exception?.message}");
                        updateUI(null);
                    }
                });
    }

    public void signInWithGoogle(View view) {
        Log.d(TAG, "signIn");

        showProgressDialog("Authenticating...");

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void register(View view, String email, String password) {
        Log.d(TAG, "register: " + email);

        showProgressDialog("Registering...");

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "createUserWithEmail:success");
                updateUI(mAuth.getCurrentUser());
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.getException());

                showMessage(view, "Error: " + Objects.requireNonNull(task.getException()).getMessage());
                updateUI(null);
            }
        });
    }

    public void signOut() {
        Log.d(TAG, "signOut");
        showProgressDialog("Signing Out...");

        // Firebase Sign Out
        mAuth.signOut();

        // Google Sign Out
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> updateUI(null));
            //mGoogleSignInClient.revokeAccess()
            return;
        }
        updateUI(null);
    }

    // Authenticate Firebase with Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        showProgressDialog("Authenticating...");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }

                    hideProgressDialog();
                });
    }

    public void updateUI(FirebaseUser user) {
        hideProgressDialog();
    }
}
