package com.social.sign.in;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.social.sign.in.utilities.LogcatUtils;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private static final int RC_SIGN_IN                         = 9001;

    private SignInButton signInButton;

    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initializeView();
        initializeObject();
        initializeEvent();
    }

    protected void initializeView() {
        signInButton                = findViewById(R.id.signInButton);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
    }

    protected void initializeObject() {
        getGoogleSignInOptions();
        googleSignInClient  = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    protected void initializeEvent() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog();
                googleSignIn();
            }
        });
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if(account != null)
        {
            Toast.makeText(SignInActivity.this, "Signed in as : "+account.getDisplayName(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(SignInActivity.this, "Signed out : ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            /* Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...); */
            if (requestCode == getRcSignIn())
            {
                /* The Task returned from this call is always completed, no need to attach a listener. */
                hideProgressDialog();

                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount googleSignInResult = null;
                try
                {
                    googleSignInResult = task.getResult(ApiException.class);
                    if (googleSignInResult != null)
                    {
                        handleSignInResult(googleSignInResult);
                        /* updateUI(googleSignInAccount); */
                    }
                }
                catch (ApiException apiException)
                {
                    /*
                     * The ApiException status code indicates the detailed failure reason.
                     * Please refer to the GoogleSignInStatusCodes class reference for more information.
                     */
                    LogcatUtils.errorMessage(TAG, "signInResult:failed code=" + apiException.getStatusCode());
                    Toast.makeText(SignInActivity.this, "Login failed : "+apiException.toString(), Toast.LENGTH_SHORT).show();
                    /* updateUI(null); */
                    apiException.printStackTrace();
                }
            }
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

    private void handleSignInResult(GoogleSignInAccount googleSignInAccount) {
        /*
         * Signed in successfully, show authenticated UI.
         * updateUI(googleSignInAccount);
         */
        GoogleUsersDetail googleUsersDetail = new GoogleUsersDetail();
        googleUsersDetail.setPhotoUrl(googleSignInAccount.getPhotoUrl());
        googleUsersDetail.setDisplayName(googleSignInAccount.getDisplayName());
        googleUsersDetail.setEmail(googleSignInAccount.getEmail());
        googleUsersDetail.setFamilyName(googleSignInAccount.getFamilyName());
        googleUsersDetail.setGivenName(googleSignInAccount.getGivenName());
        googleUsersDetail.setGoogleId(googleSignInAccount.getId());
        googleUsersDetail.setIdToken(googleSignInAccount.getIdToken());
        googleUsersDetail.setServerAuthCode(googleSignInAccount.getServerAuthCode());

        Intent intent = new Intent(SignInActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.GOOGLE_ACCOUNT_DETAILS, googleUsersDetail);
        startActivity(intent);
        finish();
    }

    public static int getRcSignIn() {
        return RC_SIGN_IN;
    }

    public void googleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, getRcSignIn());
    }

    /*
     ***********************************************************************************************
     ********************************* Activity lifecycle methods **********************************
     ***********************************************************************************************
     */
    @Override
    protected void onStart() {
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        /* Check for existing Google Sign In account, if the user is already signed in the GoogleSignInAccount will be non-null. */
        if (googleSignInAccount != null)
        {
            handleSignInResult(googleSignInAccount);
            /* updateUI(googleSignInAccount); */
        }
        LogcatUtils.informationMessage(TAG, "onStart() call");
        super.onStart();
    }
}