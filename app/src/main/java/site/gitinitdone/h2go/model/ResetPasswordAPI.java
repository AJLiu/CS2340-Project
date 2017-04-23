package site.gitinitdone.h2go.model;

import android.content.Context;
import android.os.AsyncTask;
import site.gitinitdone.h2go.R;

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

/**
 * Created by Anthony Liu (aliu77)
 */

public class ResetPasswordAPI extends AsyncTask<Void, Void, Boolean> {
    private final String username;
    private final Context context;

    public ResetPasswordAPI(String username, Context context) {
        this.username = username;
        this.context = context;
    }

    @Override protected Boolean doInBackground(Void... params) {
        URL url = null;
        try {
            url = new URL(context.getString(R.string.apiHttpPath) + "/api/users/reset");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        URLConnection con = null;
        try {
            con = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpURLConnection http = (HttpURLConnection) con;
        try {
            http.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        http.setDoOutput(true);

        String result = "";
        try {
            result = (URLEncoder.encode("username", "UTF-8") + "=" +
                    URLEncoder.encode(username, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] out = result.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        try {
            http.connect();
            try (OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

            return http.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
