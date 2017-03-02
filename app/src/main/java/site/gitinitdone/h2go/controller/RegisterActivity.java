package site.gitinitdone.h2go.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.RegisterUserAPI;
import site.gitinitdone.h2go.model.UserAccount;

public class RegisterActivity extends AppCompatActivity {

    private RegisterUserAPI registerUser = null;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.content_register_progress);

        Spinner title = (Spinner) findViewById(R.id.titleSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, UserAccount.Title.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        title.setAdapter(adapter);
        title.setSelection(0);

        Spinner userType = (Spinner) findViewById(R.id.userTypeSpinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, UserAccount.AccountType.values());
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType.setAdapter(adapter2);
        userType.setSelection(0);

    }

    /**
     * Validates the data entered, submits API to create new user, and eventually switches back
     * to the Welcome activity
     *
     * @param view the view where the button that called this method resides
     */
    public void registerNewUser(View view) {

        Map<String, String> arguments = new HashMap<>();

        // Validate the username is not blank
        EditText username = (EditText) findViewById(R.id.usernameField);
        String usernameText = username.getText().toString().trim();
        if (usernameText.length() == 0) {
            showErrorOnField(username, "Username is too short.");
            username.requestFocus();
            return;
        }

        // Validate password is not blank
        EditText password = (EditText) findViewById(R.id.passwordField);
        String passwordText = password.getText().toString().trim();
        if (passwordText.length() == 0) {
            showErrorOnField(password, "Password is too short.");
            return;
        }

        // Validate the first name is not blank
        EditText firstName = (EditText) findViewById(R.id.firstNameField);
        String firstNameText = firstName.getText().toString().trim();
        if (firstNameText.length() == 0) {
            showErrorOnField(firstName, "First name is too short.");
            return;
        }

        // Validate the last name is not blank
        EditText lastName = (EditText) findViewById(R.id.lastNameField);
        String lastNameText = lastName.getText().toString().trim();
        if (lastNameText.length() == 0) {
            showErrorOnField(lastName, "Last name is too short.");
            return;
        }

        // Validate the email address has a proper format
        EditText email = (EditText) findViewById(R.id.emailField);
        String emailText = email.getText().toString().trim();
        if (email.length() == 0 || !validateEmail(emailText)) {
            showErrorOnField(email, "Email is not valid.");
            return;
        }

        EditText address = (EditText) findViewById(R.id.addressLineField);
        EditText city = (EditText) findViewById(R.id.cityField);
        EditText stateZip = (EditText) findViewById(R.id.stateZipField);

        String addressText = address.getText().toString().trim();
        String cityText = city.getText().toString().trim();
        String stateZipText = stateZip.getText().toString().trim();

        // Validate address sections are not blank
        String addressFullText = addressText + "~" + cityText + "~" + stateZipText;
        if (addressText.length() == 0) {
            showErrorOnField(address,"Address is too short.");
            return;
        }
        if (cityText.length() == 0) {
            showErrorOnField(city, "City is too short.");
            return;
        }
        if (stateZipText.length() == 0) {
            showErrorOnField(stateZip, "State / Zip Code is too short.");
            return;
        }

        Spinner title = (Spinner) findViewById(R.id.titleSpinner);
        String titleText = title.getSelectedItem().toString().trim();

        Spinner userType = (Spinner) findViewById(R.id.userTypeSpinner);
        UserAccount.AccountType userAccountType = (UserAccount.AccountType)(userType.getSelectedItem());
        String userTypeText = userAccountType.toString().trim();

        arguments.put("username", usernameText);
        arguments.put("password", passwordText);
        arguments.put("firstName", firstNameText);
        arguments.put("lastName", lastNameText);
        arguments.put("email", emailText);
        arguments.put("address", addressFullText);
        arguments.put("title", titleText);
        arguments.put("userType", userTypeText);

        // call the API to register a new user using the data / parameters in this Map
        registerUser = new LocalRegisterAPI(arguments);
        registerUser.execute((Void) null);

        showProgress(true);
    }

    /**
     * Validates the email to make sure it follows the proper format.
     *
     * @param emailText the email that needs to be checked for validity
     * @return whether or not the email string has a valid format (true if the email is valid)
     */
    private boolean validateEmail(String emailText) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(emailText);
        return m.matches();
    }

    /**
     * Sets the error message for the field on the edit form and focuses that field on the screen
     *
     * @param field the field that has the error
     * @param message the message to show on the field
     */
    private void showErrorOnField(EditText field, String message) {
        field.setError(message);
        field.requestFocus();
    }

    /**
     * Shows the progress UI and hides the registration form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous registration task used to authenticate the user.
     */
    class LocalRegisterAPI extends RegisterUserAPI {

        private Map<String, String> data;

        LocalRegisterAPI(Map<String, String> data) {
            super(data, getApplicationContext());
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            registerUser = null;
            showProgress(false);

            if (success) {
                System.out.println("Registered TRUE");
                Toast.makeText(getBaseContext(), "User has been registered. Please log in.", Toast.LENGTH_LONG).show();
                finish();

            } else {
                System.out.println("Registered FALSE");
                if (duplicateUser) {
                    Toast.makeText(getBaseContext(), "That username is already taken.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), "There was an error during registration.", Toast.LENGTH_LONG).show();
                }

            }
        }

        @Override
        protected void onCancelled() {
            registerUser = null;
            showProgress(false);
        }

    }

}