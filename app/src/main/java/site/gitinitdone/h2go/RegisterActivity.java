package site.gitinitdone.h2go;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private RegisterUserAPI registerUser = null;
    private UserAccount userAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }





    public void registerNewUser(View view) {

        Map<String, String> arguments = new HashMap<>();

        System.out.println("Register New User 1");

        EditText username = (EditText) findViewById(R.id.usernameField);
        String usernameText = username.getText().toString().trim();
        if (usernameText.length() == 0) {
            showErrorOnField(username, "Username is too short.");
            username.requestFocus();
            return;
        }

        EditText password = (EditText) findViewById(R.id.passwordField);
        String passwordText = password.getText().toString().trim();
        if (passwordText.length() == 0) {
            showErrorOnField(password, "Password is too short.");
            return;
        }

        System.out.println("Register New User 2");

        EditText firstName = (EditText) findViewById(R.id.firstNameField);
        String firstNameText = firstName.getText().toString().trim();
        if (firstNameText.length() == 0) {
            showErrorOnField(firstName, "First name is too short.");
            return;
        }

        EditText lastName = (EditText) findViewById(R.id.lastNameField);
        String lastNameText = lastName.getText().toString().trim();
        if (lastNameText.length() == 0) {
            showErrorOnField(lastName, "Last name is too short.");
            return;
        }

        System.out.println("Register New User 3");

        EditText email = (EditText) findViewById(R.id.emailField);
        String emailText = email.getText().toString().trim();
        if (!validateEmail(emailText)) {
            showErrorOnField(email, "Email is not valid.");
            return;
        }

        EditText address = (EditText) findViewById(R.id.addressLineField);
        EditText city = (EditText) findViewById(R.id.cityField);
        EditText stateZip = (EditText) findViewById(R.id.stateZipField);

        String addressText = address.getText().toString().trim();
        String cityText = city.getText().toString().trim();
        String stateZipText = stateZip.getText().toString().trim();

        System.out.println("Register New User 4");

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


        System.out.println("Register New User 5");

        Spinner title = (Spinner) findViewById(R.id.titleSpinner);
        String titleText = title.getSelectedItem().toString().trim();


        Spinner userType = (Spinner) findViewById(R.id.userTypeSpinner);
        UserAccount.AccountType userAccountType = (UserAccount.AccountType)(userType.getSelectedItem());
        String userTypeText = userAccountType.toString().trim();

        System.out.println("Register New User 6");

        arguments.put("username", usernameText);
        arguments.put("password", passwordText);
        arguments.put("firstName", firstNameText);
        arguments.put("lastName", lastNameText);
        arguments.put("email", emailText);
        arguments.put("address", addressFullText);
        arguments.put("title", titleText);
        arguments.put("userType", userTypeText);

        System.out.println("Register New User 7");

        registerUser = new RegisterUserAPI(arguments);
        registerUser.execute((Void) null);

    }

    private boolean validateEmail(String emailText) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(emailText);
        return m.matches();
    }

    private void showErrorOnField(EditText field, String message) {
        field.setError(message);
        field.requestFocus();
    }




    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    class RegisterUserAPI extends AsyncTask<Void, Void, Boolean> {

//    private boolean actionSuccessful = false;

//    Boolean success = false;
//    AccountAPITools.UserLoginTask mAuthTask = new AccountAPITools.UserLoginTask(username, password, success);
//    mAuthTask.execute((Void) null);
//
//    return success;

        private Map<String, String> data;

        RegisterUserAPI(Map<String, String> data) {
            this.data = data;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            System.out.println("--------1--------");
            URL url = null;
            try {
                url = new URL("http://www.gitinitdone.site/api/users/register");
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


            String result = "";
            for (Map.Entry<String, String> entry : data.entrySet())
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
//                    Map<String, List<String>> headerFields = http.getHeaderFields();
//                    List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);
//
//                    if (cookiesHeader != null) {
//                        for (String cookie : cookiesHeader) {
//                            cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
//                        }
//                    }
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

            System.out.println(response.toLowerCase().contains("user registered"));
            if (response.toLowerCase().contains("is already registered")) {
                Toast.makeText(getBaseContext(), "That username is already taken.", Toast.LENGTH_LONG).show();
            }

            return (response.toLowerCase().contains("user registered"));
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            registerUser = null;
            //showProgress(false);

            if (success) {
                System.out.println("Registered TRUE");
                Toast.makeText(getBaseContext(), "User has been registered. Please log in.", Toast.LENGTH_LONG).show();
                finish();
                //nextAct(findViewById(android.R.id.content));
            } else {
                System.out.println("Registered FALSE");
                Toast.makeText(getBaseContext(), "There was an error during registration.", Toast.LENGTH_LONG).show();
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            registerUser = null;
            //showProgress(false);
        }


        /*

        EditText firstName = (EditText) findViewById(R.id.firstNameField);
        firstName.setText(accountInfo.getFirstName());

        EditText lastName = (EditText) findViewById(R.id.lastNameField);
        lastName.setText(accountInfo.getLastName());

        EditText email = (EditText) findViewById(R.id.emailField);
        email.setText(accountInfo.getEmail());

        String[] addressParts = accountInfo.getAddress().split("~");
        EditText address = (EditText) findViewById(R.id.addressLineField);
        address.setText(addressParts[0]);

        EditText city = (EditText) findViewById(R.id.cityField);
        city.setText(addressParts[1]);

        EditText stateZip = (EditText) findViewById(R.id.stateZipField);
        stateZip.setText(addressParts[2]);


         */

    }

}