package site.gitinitdone.h2go.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import site.gitinitdone.h2go.R;

/**
 * Represents and asynchronous task that is used to submit a new source report to the backend database
 */
public class SubmitSourceReportAPI extends AsyncTask<Void, Void, Boolean> {

        private CookieManager cookieManager;

        private final Context context;

        private Map<String, String> data;

        public SubmitSourceReportAPI(Map<String, String> data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            cookieManager = LoginUserAPI.cookieManager;

            URL url = null;
            try {
                url = new URL(context.getString(R.string.apiHttpPath) + "/api/reports/source/submit");
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
                System.out.println("Reached here 1");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            if (cookieManager.getCookieStore().getCookies().size() > 0) {
                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                http.setRequestProperty("Cookie",
                        TextUtils.join(";",  cookieManager.getCookieStore().getCookies()));
            }
            http.setDoOutput(true);
            System.out.println("Reached here 2");
            JSONObject jsonObjToSend = new JSONObject();
            JSONObject location = new JSONObject();
            try {
                location.put("lat", Double.parseDouble(data.get("lat")));
                location.put("long", Double.parseDouble(data.get("long")));
                System.out.println("Reached here 3");
            } catch (JSONException e) {
                System.out.println("Error creating location sub-JSON");
                e.printStackTrace();
            }

            try {
                jsonObjToSend.put("location", location);
                System.out.println("Reached here 4");
            } catch (JSONException e) {
                System.out.println("Error inserting location sub-JSON into main JSON");
                e.printStackTrace();
            }

            try {
                jsonObjToSend.put("waterType", data.get("waterType"));
                jsonObjToSend.put("waterCondition", data.get("waterCondition"));
                System.out.println("Reached here 5");
            } catch (JSONException e) {
                System.out.println("Error inserting water type and condition into main JSON");
                e.printStackTrace();
            }

            String result = jsonObjToSend.toString();
            System.out.println("Result string to send \n" + result);
            System.out.println("Reached here 6");


            byte[] out = result.getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            System.out.println("Reached here 7");
            try {
                http.connect();
            } catch (IOException e) {
                System.out.println("--- Error Here 5 ---");
                e.printStackTrace();
            }
            try {
                try (OutputStream os = http.getOutputStream()) {
                    os.write(out);
                    System.out.println("Reached here 8");
                }
            } catch (IOException e) {
                System.out.println("--- Error Here 6 ---");
                e.printStackTrace();
            }

            // Reading the response after the attempted submission
            BufferedInputStream bis = null;

            try {
                if (http.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    bis = new BufferedInputStream(http.getInputStream());
                } else {
                    bis = new BufferedInputStream(http.getErrorStream());
                    System.out.println("Reached here 9");
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
                System.out.println("Error at the end of the submitting Do in Background.");
            }

            return (response.toLowerCase().contains("report submitted"));
        }


}
