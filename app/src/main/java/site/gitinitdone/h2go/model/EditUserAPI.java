package site.gitinitdone.h2go.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import site.gitinitdone.h2go.R;

/**
 * Represents an asynchronous edit user profile task used to edit the user's profile data.
 */

public class EditUserAPI extends AsyncTask<Void, Void, Boolean> {

    private CookieManager cookieManager;

    private final Context context;

    private Map<String, String> data;

    public EditUserAPI(Map<String, String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        cookieManager = LoginUserAPI.cookieManager;

        URL url = null;
        try {
            url = new URL(context.getString(R.string.apiHttpPath) + "/api/users/edit");
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

        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        if (cookieManager.getCookieStore().getCookies().size() > 0) {
            // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
            http.setRequestProperty("Cookie",
                    TextUtils.join(";",  cookieManager.getCookieStore().getCookies()));
        }
        http.setDoOutput(true);

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
        System.out.println("My result \n" + result);
        byte[] out = result.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

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

        BufferedInputStream bis = null;

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

        if (response.toLowerCase().contains("unauthorized")) {
            System.out.println("Error at the end of the editing Do in Background.");
        }

        return (!response.toLowerCase().contains("unauthorized"));
    }


}
