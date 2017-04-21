package site.gitinitdone.h2go.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.LoginUserAPI;
import site.gitinitdone.h2go.model.SoundEffects;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private LocalLoginAPI login = null;
    private SharedPreferences.Editor mySharedPrefEditor;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_login_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.username);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ((id == (R.id.login)) || (id == (EditorInfo.IME_NULL))) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                SoundEffects.playClickSound(view);
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void nextAct(View v){
        Intent i = new Intent(this, AppScreenActivity.class);
        AutoCompleteTextView editText = (AutoCompleteTextView) findViewById(R.id.username);
        String theUsername = editText.getText().toString();

        mySharedPrefEditor = getSharedPreferences(getString(R.string.sharedPrefName), MODE_PRIVATE)
                                .edit();
        mySharedPrefEditor.putString(getString(R.string.prompt_username), theUsername);
        mySharedPrefEditor.apply();

        startActivity(i);
        Toast.makeText(getApplicationContext(), R.string.login_success,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showProgress(false);
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        //if (mAuthTask != null) {
        //  return;
        //}

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(username)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            login = new LocalLoginAPI(username, password);
            login.execute((Void) null);

            showProgress(true);
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_login_toolbar);
            setSupportActionBar(myToolbar);
            ActionBar ab = getSupportActionBar();
            ab.hide();


        }
    }

    private boolean isEmailValid(String username) {
        return !username.isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return !password.isEmpty();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    // @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    class LocalLoginAPI extends LoginUserAPI {

        LocalLoginAPI(String username, String password) {
            super(username, password, getApplicationContext());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            login = null;
            showProgress(false);

            if (success) {
                System.out.println("Done TRUE");
                finish();
                nextAct(findViewById(android.R.id.content));
            } else {
                System.out.println("Done FALSE");
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            login = null;
            showProgress(false);
        }

    }

}

