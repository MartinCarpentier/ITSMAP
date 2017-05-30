package com.example.norgaard.barty.Activities.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.norgaard.barty.Activities.Maps.MapsActivity;
import com.example.norgaard.barty.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends FragmentActivity {

    private static final int RC_SIGN_IN = 101;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth firebaseAuth;
    private TextView currentUser;
    private Button btnNoThanks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        currentUser = (TextView) findViewById(R.id.textViewCurrentUser);
        firebaseAuth = FirebaseAuth.getInstance();
        btnNoThanks = (Button) findViewById(R.id.buttonNoThanks);
        // The following is heavily inspired by
        // https://developers.google.com/identity/sign-in/android/sign-in

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.oauth2_secret_key))
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(this.getClass().getSimpleName(), getString(R.string.error_google_api_failed_connection_key));
                        Toast.makeText(LoginActivity.this,
                                getString(R.string.error_google_api_failed_connection_key),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        btnNoThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent proceedWithoutLogin = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(proceedWithoutLogin);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUser(currentUser);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(this.getClass().getSimpleName(), "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);

        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(this.getApplicationContext(), R.string.info_not_signed_in_key, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(FirebaseUser user) {

        if (user == null){
            currentUser.setText(R.string.info_not_logged_in_key);
        }
        else {
            currentUser.setText(user.getDisplayName());
            Intent intent = new Intent(this, MapsActivity.class);

            startActivity(intent);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(this.getClass().getSimpleName(), "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(this.getClass().getSimpleName(), "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(this.getClass().getSimpleName(), "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.info_auth_failed_key,
                                    Toast.LENGTH_SHORT).show();
                            updateUser(null);
                        }
                        // ...
                    }
                });
    }
}
