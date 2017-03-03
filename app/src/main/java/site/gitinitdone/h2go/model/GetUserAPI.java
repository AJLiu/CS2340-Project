package site.gitinitdone.h2go.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import site.gitinitdone.h2go.R;

/**
 * Represents an asynchronous getUserInfo task used to get the data in the user profile.
 */
public class GetUserAPI extends AsyncTask<Void, Void, Boolean> {

    private CookieManager cookieManager;
    protected UserAccount userAccount = null;      // holds the original user data before any edits

    private final Context context;

    public GetUserAPI(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        cookieManager = LoginUserAPI.cookieManager;

        URL url = null;
        try {
            url = new URL(context.getString(R.string.apiHttpPath) + "/api/users");
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
        http.setRequestProperty("Cookie",
                TextUtils.join(";", cookieManager.getCookieStore().getCookies()));

        try {
            http.setRequestMethod("GET"); // PUT is another valid option
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        try {
            http.connect();
        } catch (IOException e) {
            System.out.println("--- Error Here 5 ---");
            e.printStackTrace();
        }

        BufferedInputStream bis = null;
        System.out.println(http.getRequestMethod());
        try {
            if (http.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                bis = new BufferedInputStream(http.getInputStream());
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

        System.out.println("Response = " + response);

        boolean validData = !response.contains("Must be logged in");
        if (validData) {
            JSONObject json = null;
            UserAccount currentUser = null;
            try {
                json = new JSONObject(response);
                String username = json.getString("username");
                String firstName = json.getString("firstName");
                String lastName = json.getString("lastName");
                String address = json.getString("address");
                String email = json.getString("email");

                String titleString = json.getString("title").toUpperCase();
                UserAccount.Title title = UserAccount.Title.valueOf(titleString.substring(0, titleString.length() - 1));

                UserAccount.AccountType type = UserAccount.AccountType.valueOf(json.getString("userType").toUpperCase());

                currentUser = new UserAccount(username, title, firstName, lastName, address, email, type);
                saveAccountInfo(currentUser);
                return true;

            } catch (JSONException e) {
                System.out.println("Failed converting response to JSON!!!");
                e.printStackTrace();
            }

        } else {
            System.out.println("Log in cookie did not work. GET request did not work.");
        }
        return false;
    }

    private void saveAccountInfo(UserAccount account) {
        userAccount = account;
    }

}
