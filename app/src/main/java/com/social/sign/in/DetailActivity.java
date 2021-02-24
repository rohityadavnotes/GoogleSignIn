package com.social.sign.in;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.social.sign.in.utilities.ActivityUtils;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    public static final String GOOGLE_ACCOUNT_DETAILS = "google_account_details";

    private CircleImageView profileCircleImageView;
    private TextView displayNameTextView;
    private TextView emailTextView;
    private TextView familyNameTextView;
    private TextView givenNameTextView;
    private TextView googleIdTextView;
    private TextView idTokenTextView;
    private TextView serverAuthCodeTextView;
    private MaterialButton signOutMaterialButton, revokeAccessMaterialButton;

    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleUsersDetail googleUsersDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initializeView();
        initializeObject();
        initializeEvent();
        sendAccountDetails();
    }

    protected void initializeView() {
        profileCircleImageView      = findViewById(R.id.pictureCircleImageView);
        displayNameTextView         = findViewById(R.id.displayNameTextView);
        emailTextView               = findViewById(R.id.emailTextView);
        familyNameTextView          = findViewById(R.id.familyNameTextView);
        givenNameTextView           = findViewById(R.id.givenNameTextView);
        googleIdTextView            = findViewById(R.id.googleIdTextView);
        idTokenTextView             = findViewById(R.id.idTokenTextView);
        serverAuthCodeTextView      = findViewById(R.id.serverAuthCodeTextView);
        signOutMaterialButton       = findViewById(R.id.signOutMaterialButton);
        revokeAccessMaterialButton  = findViewById(R.id.revokeAccessMaterialButton);
    }

    protected void initializeObject() {
        googleUsersDetail = getIntent().getParcelableExtra(GOOGLE_ACCOUNT_DETAILS);

        getGoogleSignInOptions();
        googleSignInClient  = GoogleSignIn.getClient(DetailActivity.this, googleSignInOptions);
    }

    protected void initializeEvent() {
        signOutMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        revokeAccessMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revokeAccess();
            }
        });
    }

    private void sendAccountDetails() {

        if (googleUsersDetail != null)
        {
            Uri photoUrl = googleUsersDetail.getPhotoUrl();
            System.out.println("Photo Url = "+photoUrl);

            Glide.with(getApplicationContext())
                    .load(photoUrl)
                    .error(R.drawable.placeholder)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(profileCircleImageView);

            String displayName = googleUsersDetail.getDisplayName();
            System.out.println("Display Name = "+displayName);
            displayNameTextView.setText("Display Name : "+displayName);

            String email = googleUsersDetail.getEmail();
            System.out.println("Email = "+email);
            emailTextView.setText("Email : "+email);

            String familyName = googleUsersDetail.getFamilyName();
            System.out.println("Family Name = "+familyName);
            familyNameTextView.setText("Family Name : "+familyName);

            String givenName = googleUsersDetail.getGivenName();
            System.out.println("Give Name = "+givenName);
            givenNameTextView.setText("Give Name : "+givenName);

            String googleId = googleUsersDetail.getGoogleId();
            System.out.println("Google Id = "+googleId);
            googleIdTextView.setText("Google Id : "+googleId);

            String idToken = googleUsersDetail.getIdToken();
            System.out.println("Id Token = "+idToken);
            idTokenTextView.setText("Id Token : "+idToken);

            String serverAuthCode = googleUsersDetail.getServerAuthCode();
            System.out.println("Server Auth Code = "+serverAuthCode);
            serverAuthCodeTextView.setText("Server Auth Code : "+idToken);
        }
        else
        {
            ActivityUtils.launchActivityWithClearBackStack(DetailActivity.this, SignInActivity.class);
        }
    }


    /*
     ***********************************************************************************************
     ******************************************* Helper methods ************************************
     ***********************************************************************************************
     */
    public GoogleSignInOptions getGoogleSignInOptions(){
        if(googleSignInOptions == null)
        {
            /*
             * Request only the user's ID token, which can be used to identify the user securely to
             * your backend. This will contain the user's basic profile (name, profile picture URL, etc)
             * so you should not need to make an additional call to personalize your application.
             */
            googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.server_client_id))
                    .requestEmail()
                    .build();
        }

        return googleSignInOptions;
    }

    /**
     * Method to do google sign out this code clears which account is connected to the app.
     * To sign in again, the user must choose their account again.
     */
    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        /* After signOut success we navigate the user back to SignInActivity */
                        if(task.isComplete())
                        {
                            ActivityUtils.launchActivityWithClearBackStack(DetailActivity.this, SignInActivity.class);
                        }
                        else
                        {
                            Toast.makeText(DetailActivity.this, "There was some error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * DISCONNECT ACCOUNTS,
     *
     * method to revoke access from this app
     * call this method after successful sign out
     *
     * It is highly recommended that you provide users that signed in with Google the ability to disconnect
     * their Google account from your app. If the user deletes their account, you must delete the information
     * that your app obtained from the Google APIs
     */
    private void revokeAccess() {
        googleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        /* After revokeAccess success we navigate the user back to SignInActivity */
                        if(task.isComplete())
                        {
                            ActivityUtils.launchActivityWithClearBackStack(DetailActivity.this, SignInActivity.class);
                        }
                        else
                        {
                            Toast.makeText(DetailActivity.this, "There was some error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}