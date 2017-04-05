package site.gitinitdone.h2go.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

import site.gitinitdone.h2go.R;

/**
 * Represents an asynchronous task to get all the purity reports in the backend database.
 */
public class GetPurityReportsAPI extends AsyncTask<Void, Void, Boolean> {

    private CookieManager cookieManager;
    protected List<PurityReport> purityReportList = null; // holds all reports from the GET request

    private final Context context;

    public GetPurityReportsAPI(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        cookieManager = LoginUserAPI.cookieManager;
        purityReportList = new ArrayList<>();

        URL url = null;
        try {
            url = new URL(context.getString(R.string.apiHttpPath) + "/api/reports/purity");
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

        // Parsing all the data from the JSON response to save as individual SourceReport objects
        if (validData) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    // get a single report as a jsonObject
                    JSONObject jsonReport = jsonArray.getJSONObject(i);
                    JSONObject jsonSubmitter = jsonReport.getJSONObject("submitter");
                    JSONObject jsonLocation = jsonReport.getJSONObject("location");

                    double latitude = jsonLocation.getDouble("lat");
                    double longitude = jsonLocation.getDouble("long");
                    int reportNum = jsonReport.getInt("reportNumber");

                    // get info about the submitter of the report
                    String name = jsonSubmitter.optString("firstName", "Test") + " "
                            + jsonSubmitter.optString("lastName", "Account");

                    // end of submitter info

                    long timeStamp = jsonReport.getLong("timestamp");

                    String waterConditionString = jsonReport.getString("waterCondition");
                    waterConditionString = waterConditionString.toUpperCase();
                    PurityReport.OverallCondition waterCondition = PurityReport.OverallCondition
                            .valueOf(waterConditionString);

                    int virusPPM = jsonReport.getInt("virusPPM");
                    int contaminantPPM = jsonReport.getInt("contaminantPPM");

                    // All the fields have been collected for a single water report
                    // Now we will create a new SourceReport object
                    // and add it to the sourceReportList
                    PurityReport report = new PurityReport(latitude, longitude, reportNum, name,
                                timeStamp, waterCondition, virusPPM, contaminantPPM);
                    System.out.println(report.getReportNumber());
                    addNewReport(report);
                }

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

    private void addNewReport(PurityReport report) {
        purityReportList.add(report);
    }


}
