package site.gitinitdone.h2go.controller;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.EditUserAPI;
import site.gitinitdone.h2go.model.GetUserAPI;
import site.gitinitdone.h2go.model.UserAccount;

import java.util.HashMap;
import java.util.Map;

/**
 * This activity allows the user to edit their profile data and submit the changes
 */
public class EditUserActivity extends AppCompatActivity {

    private LocalGetUserAPI getUserInfo = null;  // the AsyncTask object to get user's current data
    private LocalEditUserAPI editUserInfo = null; // the AsyncTask object to submit edits

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_edit_user_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Get the current data associated with the account of whoever is logged in
        getUserInfo = new LocalGetUserAPI();
        getUserInfo.execute((Void) null);
    }

    /**
     * Switches to the AppScreen activity after all changes made in the Edit form
     * are submitted to the online database using the API
     *
     * @param view the view where the button that called this method resides
     */
    public void submitEdits(View view) {

        Map<String, String> arguments = new HashMap<>();

        // Validating the First Name
        EditText firstName = (EditText) findViewById(R.id.firstNameFieldEdit);
        String firstNameText = firstName.getText().toString().trim();
        if (firstNameText.length() == 0) {
            showErrorOnField(firstName, "First name is too short.");
            return;
        }

        // Validating the Last Name
        EditText lastName = (EditText) findViewById(R.id.lastNameFieldEdit);
        String lastNameText = lastName.getText().toString().trim();
        if (lastNameText.length() == 0) {
            showErrorOnField(lastName, "Last name is too short.");
            return;
        }

        // Validating the Email address
        EditText email = (EditText) findViewById(R.id.emailFieldEdit);
        String emailText = email.getText().toString().trim();
        if (!validateEmail(emailText)) {
            showErrorOnField(email, "Email is not valid.");
            return;
        }

        // Validating the home address
        EditText address = (EditText) findViewById(R.id.addressLineFieldEdit);
        EditText city = (EditText) findViewById(R.id.cityFieldEdit);
        EditText stateZip = (EditText) findViewById(R.id.stateZipFieldEdit);

        String addressText = address.getText().toString().trim();
        String cityText = city.getText().toString().trim();
        String stateZipText = stateZip.getText().toString().trim();

        // Validating the Address fields
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


        // Obtains the title from the spinner
        Spinner title = (Spinner) findViewById(R.id.titleSpinnerEdit);
        String titleText = title.getSelectedItem().toString().trim();

        // Puts all the new data into a Map
        arguments.put("firstName", firstNameText);
        arguments.put("lastName", lastNameText);
        arguments.put("email", emailText);
        arguments.put("address", addressFullText);
        arguments.put("title", titleText);

        // Call the API to make these edits and use this new data for the user account
        editUserInfo = new LocalEditUserAPI(arguments);
        editUserInfo.execute((Void) null);
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
     * Represents an asynchronous getUserInfo task used to get the data in the user profile.
     */
    class LocalGetUserAPI extends GetUserAPI {

        public LocalGetUserAPI() {
            super(getApplicationContext());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            //showProgress(false);

            if (success) {
                populateFields(userAccount);
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }
    }

    /**
     * This method populates the form with the data that is currently stores on the database
     *
     * @param accountInfo a UserAccount object that holds the data of the user before editing it
     */
    private void populateFields(UserAccount accountInfo) {


        EditText firstName = (EditText) findViewById(R.id.firstNameFieldEdit);
        firstName.setText(accountInfo.getFirstName());

        EditText lastName = (EditText) findViewById(R.id.lastNameFieldEdit);
        lastName.setText(accountInfo.getLastName());

        EditText email = (EditText) findViewById(R.id.emailFieldEdit);
        email.setText(accountInfo.getEmail());

        String[] addressParts = accountInfo.getAddress().split("~");
        EditText address = (EditText) findViewById(R.id.addressLineFieldEdit);
        address.setText(addressParts[0]);

        EditText city = (EditText) findViewById(R.id.cityFieldEdit);
        city.setText(addressParts[1]);

        EditText stateZip = (EditText) findViewById(R.id.stateZipFieldEdit);
        stateZip.setText(addressParts[2]);

        Spinner title = (Spinner) findViewById(R.id.titleSpinnerEdit);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, UserAccount.Title.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        title.setAdapter(adapter);
        title.setSelection(accountInfo.getTitle().ordinal());

        TextView username = (TextView) findViewById(R.id.formUsernameFieldEdit);
        String usernameText = "Username: " + accountInfo.getUsername();
        username.setText(usernameText);

        TextView userType = (TextView) findViewById(R.id.formUserTypeFieldEdit);
        String userTypeText = "User Account Type: " + accountInfo.getUserType().toString();
        userType.setText(userTypeText);
    }


    /**
     * Represents an asynchronous edit user profile task used to edit the user's profile data.
     */
    class LocalEditUserAPI extends EditUserAPI {

        private Map<String, String> data;

        LocalEditUserAPI(Map<String, String> data) {
            super(data, getApplicationContext());
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            editUserInfo = null;
            //showProgress(false);

            if (success) {
                System.out.println("Edited Data TRUE");
                Toast.makeText(getBaseContext(), "User profile has been saved.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                System.out.println("Edited Data FALSE");
                Toast.makeText(getBaseContext(), "There was an error during editing.", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {
            editUserInfo = null;
            //showProgress(false);
        }


    }

}