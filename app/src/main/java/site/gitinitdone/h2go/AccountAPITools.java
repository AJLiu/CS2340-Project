package site.gitinitdone.h2go;

import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by surajmasand on 2/25/17.
 */

public class AccountAPITools {

    private static final String COOKIES_HEADER = "Set-Cookie";
    private static CookieManager cookieManager = new java.net.CookieManager();
    private static UserAccount userAccount = null;


    public static boolean loginUser(String username, String password) {

        UserLoginTask mAuthTask = new UserLoginTask(username, password);
        mAuthTask.execute((Void) null);

        return true;
    }


    public static boolean registerUser(UserAccount user, String password) {

        return true;
    }

    public static UserAccount getUserInfo(final CookieManager manager) {

        cookieManager = manager;

        AccountAPITools.UserGetInfoTask mAuthTask = new UserGetInfoTask();
        mAuthTask.execute((Void) null);

        cookieManager = null;

        return null;
    }






    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public static class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
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
                while((bytesRead = bis.read(contents)) != -1) {
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
            //mAuthTask = null;
            //showProgress(false);

            if (success) {
                //finish();
                //nextAct(findViewById(android.R.id.content));
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }
    }








    // ************************************************************
    // ************************************************************
    // ************************************************************

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private static class UserGetInfoTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            System.out.println("--------1--------");
            URL url = null;
            try {
                url = new URL("http://www.gitinitdone.site/api/users");
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
                    TextUtils.join(";",  cookieManager.getCookieStore().getCookies()));

            try {
                http.setRequestMethod("GET"); // PUT is another valid option
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            System.out.println("--- Reached Here 3 ---");

            //http.setDoOutput(true);

            System.out.println("End of Part 1");

            System.out.println("End of Part 2");

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
                while((bytesRead = bis.read(contents)) != -1) {
                    response += new String(contents, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("--------2--------");
            System.out.println(response);
            System.out.println("--------3--------");

            boolean validData = !response.equals("Unauthorized");
            if (validData) {
                JSONObject json = null;
                UserAccount currentUser = null;
                try {
                    json = new JSONObject(response);
                    String username = json.getString("username");
                    String title = json.getString("title");
                    String firstName = json.getString("firstName");
                    String lastName = json.getString("lastName");
                    String address = json.getString("address");
                    String email = json.getString("email");
                    UserAccount.AccountType type = UserAccount.AccountType.valueOf(json.getString("userType").toUpperCase());

                    userAccount = new UserAccount(username, title, firstName, lastName, address, email, type);
                    return true;

                } catch (JSONException e) {
                    System.out.println("Failed converting response to JSON!!!");
                    e.printStackTrace();
                }

            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //mAuthTask = null;
            //showProgress(false);

            if (success) {
                // finish();
                //  nextAct(findViewById(android.R.id.content));
            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            //mAuthTask = null;
            //showProgress(false);
        }
    }




}
