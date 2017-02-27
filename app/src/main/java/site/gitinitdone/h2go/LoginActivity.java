package site.gitinitdone.h2go;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

  static final String COOKIES_HEADER = "Set-Cookie";
  static java.net.CookieManager msCookieManager = new java.net.CookieManager();


    private LoginUserAPI login = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
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
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }
    public void nextAct(View v){
        Intent i = new Intent(this, AppScreen.class);
        AutoCompleteTextView editText = (AutoCompleteTextView) findViewById(R.id.email);
        String theEmail = editText.getText().toString();
        i.putExtra("UserEmail", theEmail);
        //i.putExtra("Cookies", msCookieManager);
        startActivity(i);
    }

    public void cancelLogin(View view) {
        finish();
        Toast.makeText(getApplicationContext(), "Cancelling log in...", Toast.LENGTH_LONG).show();
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
            login = new LoginUserAPI(username, password);
            login.execute((Void) null);

            showProgress(true);

        }
    }

    private boolean isEmailValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 0;
        //return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
        //return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    // @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
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
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    class LoginUserAPI extends AsyncTask<Void, Void, Boolean> {

        private static final String COOKIES_HEADER = "Set-Cookie";
        public CookieManager cookieManager = msCookieManager;
//        private boolean actionSuccessful = false;

//    Boolean success = false;
//    AccountAPITools.UserLoginTask mAuthTask = new AccountAPITools.UserLoginTask(username, password, success);
//    mAuthTask.execute((Void) null);
//
//    return success;

        private final String mUsername;
        private final String mPassword;

        LoginUserAPI(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            System.out.println("--------1--------");
            URL url = null;
            try {
                url = new URL("http://www.gitinitdone.site/api/users/login");
            } catch (MalformedURLException e) {
                System.out.println("--- Error Here 1 ---");
                e.printStackTrace();
            }
            URLConnection con = null;
            try {
                con = url.openConnection();
            } catch (IOException e) {
                System.out.println("--- Error Here 2 ---");
                e.printStackTrace();
            }
            HttpURLConnection http = (HttpURLConnection) con;
            try {
                http.setRequestMethod("POST"); // PUT is another valid option
                System.out.println("--- Reached Here 3 ---");

            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            http.setDoOutput(true);

            System.out.println("End of Part 1");

            Map<String, String> arguments = new HashMap<>();
            arguments.put("username", mUsername);
            arguments.put("password", mPassword);
            String result = "";
            for (Map.Entry<String, String> entry : arguments.entrySet())
                try {
                    result += "&" + (URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                            + URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    System.out.println("--- Error Here 4 ---");
                    e.printStackTrace();
                }
            result = result.substring(1);
            byte[] out = result.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            System.out.println("End of Part 2");

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            try {
                http.connect();
            } catch (IOException e) {
                System.out.println("--- Error Here 5 ---");
                e.printStackTrace();
            }
            try {
                try (OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }
            } catch (IOException e) {
                System.out.println("--- Error Here 6 ---");
                e.printStackTrace();
            }

            System.out.println("End of Part 3");

            // Do something with http.getInputStream()

            BufferedInputStream bis = null;

            try {
                if (http.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    bis = new BufferedInputStream(http.getInputStream());
                    Map<String, List<String>> headerFields = http.getHeaderFields();
                    List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                    if (cookiesHeader != null) {
                        for (String cookie : cookiesHeader) {
                            cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                        }
                    }
                } else {
                    bis = new BufferedInputStream(http.getErrorStream());
                }
            } catch (IOException e) {
                System.out.println("--- Error Here 7 ---");
                e.printStackTrace();
            }

            byte[] contents = new byte[1024];

            int bytesRead = 0;
            String response = "";
            try {
                while ((bytesRead = bis.read(contents)) != -1) {
                    response += new String(contents, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("--------2--------");
            System.out.println(response);
            System.out.println("--------3--------");

            System.out.println(!response.equals("Unauthorized"));
            return (!response.equals("Unauthorized"));
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

