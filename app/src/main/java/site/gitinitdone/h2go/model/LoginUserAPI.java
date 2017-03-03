package site.gitinitdone.h2go.model;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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

import site.gitinitdone.h2go.R;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class LoginUserAPI extends AsyncTask<Void, Void, Boolean> {

    private static final String COOKIES_HEADER = "Set-Cookie";
    public static java.net.CookieManager cookieManager = new java.net.CookieManager();

    private final String mUsername;
    private final String mPassword;
    private final Context context;

    public LoginUserAPI(String username, String password, Context context) {
        mUsername = username;
        mPassword = password;
        this.context = context;
    }


    @Override
    protected Boolean doInBackground(Void... params) {

        System.out.println("--------1--------");
        URL url = null;
        try {
            url = new URL(context.getString(R.string.apiHttpPath) + "/api/users/login");
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
}

