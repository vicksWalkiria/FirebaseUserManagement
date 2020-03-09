package com.walkiriaapps.firebaseusermanagement.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.walkiriaapps.firebaseusermanagement.R;

public class Login extends AppCompatActivity {

    private SignInButton /*facebookLoginButton,*/ googleLoginButton;
    private Button customSigninButton, customSignupButton, customSignUpButtonRegister, customSigninButtonRegister;
    private EditText emailEditText, passwordEditText, emailEditTextRegister, passwordEditText1Register, passwordEditText2Register;
    private FirebaseAuth mAuth;// ...
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;
    private LinearLayout layoutRegister, layoutSignIn;
    private String TAG = "WALKIRIA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
        bindViews();
        setListeners();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id)) //look under Client -> oauth_client -> client_id
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.getEmail().length() > 0) {
            goToNextScreen();
        }
    }


    private void goToNextScreen() {
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }

    private void setListeners() {
//        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Perform Facebook login
//            }
//        });

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        customSigninButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform custom sign in
                String email, password;

                if (testSignInParameters()) {
                    email = emailEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        goToNextScreen();
                                    } else {
                                        Toast.makeText(Login.this, getString(R.string.login_error), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

            }
        });

        customSignUpButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testRegisterParameters()) {
                    mAuth.createUserWithEmailAndPassword(emailEditTextRegister.getText().toString(), passwordEditText1Register.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        goToNextScreen();
                                    } else {
                                        Toast.makeText(Login.this, getString(R.string.signup_error), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(Login.this, getString(R.string.could_not_register), Toast.LENGTH_LONG).show();
                }
            }
        });


        customSigninButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutRegister.setVisibility(View.INVISIBLE);
                layoutSignIn.setVisibility(View.VISIBLE);
            }
        });
        customSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutRegister.setVisibility(View.VISIBLE);
                layoutSignIn.setVisibility(View.INVISIBLE);
            }
        });

    }

    private boolean testSignInParameters() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean result = true;
        emailEditText.setError(null);
        passwordEditText.setError(null);

        if (email.length() < 4 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.email_not_valid));
            result = false;
        }

        if (password.length() < 8) {
            passwordEditText.setError(getString(R.string.password_length));
            result = false;
        }
        return result;
    }

    private boolean testRegisterParameters() {
        String email = emailEditTextRegister.getText().toString();
        String password1 = passwordEditText1Register.getText().toString();
        String password2 = passwordEditText2Register.getText().toString();
        boolean result = true;

        if (email.length() < 4 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditTextRegister.setError(getString(R.string.email_not_valid));
            result = false;
        }

        if (password1.length() < 8) {
            passwordEditText1Register.setError(getString(R.string.password_length));
            result = false;
        } else {
            if (!password1.equals(password2)) {
                passwordEditText2Register.setError(getString(R.string.passwords_do_not_match));
                result = false;
            }
        }
        return result;
    }

    private void bindViews() {
//        facebookLoginButton = (Button) findViewById(R.id.facebook_login_button);
        googleLoginButton = findViewById(R.id.google_login_button);
        customSigninButton = (Button) findViewById(R.id.custom_signin_button);
        customSignupButton = (Button) findViewById(R.id.custom_signup_button);
        emailEditText = (EditText) findViewById(R.id.email_edittext);
        passwordEditText = (EditText) findViewById(R.id.password_edittext);
        customSigninButtonRegister = findViewById(R.id.custom_signin_button_register);
        customSignUpButtonRegister = findViewById(R.id.custom_signup_button_register);
        passwordEditText1Register = findViewById(R.id.password1_edittext_register);
        passwordEditText2Register = findViewById(R.id.password2_edittext_register);
        emailEditTextRegister = findViewById(R.id.email_edittext_register);
        layoutSignIn = findViewById(R.id.layout_login);
        layoutRegister = findViewById(R.id.layout_register);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("WALKIRIA", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Signed in successfully, show authenticated UI.
                            goToNextScreen();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, getString(R.string.authentication_failed), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
