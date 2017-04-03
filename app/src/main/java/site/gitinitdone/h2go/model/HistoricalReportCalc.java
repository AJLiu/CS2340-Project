package site.gitinitdone.h2go.model;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.controller.HistoryGraphActivity;
import site.gitinitdone.h2go.controller.PlottedGraphActivity;

import static android.R.attr.filter;
import static android.R.id.list;

/**
 * The HistoricalReportCalc class will calculate average virust/contaminant ppm data for each month.
 */

public class HistoricalReportCalc {
    //figure out what reports to look
    private double latitude;
    private double longitude;
    private int year;
    private String ppm;

    /**
     * A constructor to create a historical report calculation. Takes in
     * a lattitude, longitute, year, and ppm.
     */
    public HistoricalReportCalc(double lat, double lon, int yr, String Ppm) {
        latitude = lat;
        longitude = lon;
        year = yr;
        ppm = Ppm;
    }

    /**
     * A method used to access filters passed in by user for a specific report.
     */
    public String[] getFilters() {
        return new String[] {"" + latitude, "" + longitude, "" + year, ppm};
    }

    /**
     * The filter method takes in a list of Purity reports and filters out relevant water reports
     * by year and location.
     */
    public List<PurityReport> filter(List<PurityReport> purityReportList) {
        List<PurityReport> filtered = new ArrayList<>();
        for (PurityReport p: purityReportList) {
            String reportYearString = (new Date(p.getTimeStamp())).toString();
            int reportYear = Integer.parseInt(reportYearString.substring(reportYearString.length() - 4));
            if (p.getLatitude()==latitude && p.getLongitude()==longitude && reportYear == year) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    /**
     * The getAverages method calculates average ppms for each month and returns
     * an array of doubles with average ppms.
     */
    public double[] getAverages(List<PurityReport> filtered) {
        List<Integer>[] monthData = (ArrayList<Integer>[]) new ArrayList[12];

        for (int j = 0; j < 12; j++) {
            monthData[j] = new ArrayList<Integer>();
        }

        for (PurityReport p : filtered) {
            Date reportTime = (new Date(p.getTimeStamp()));
            Calendar cal = Calendar.getInstance();
            cal.setTime(reportTime);
            int index = cal.get(Calendar.MONTH);
            if (ppm.equalsIgnoreCase("Virus")) {
                monthData[index].add(p.getVirusPPM());
            } else {
                monthData[index].add(p.getContaminantPPM());
            }
        }
        double[] averages = new double[12];
        for (int i = 0; i < 12; i++) {
            double sum = 0.0;
            if (monthData[i].size() != 0) {
                for (Integer ppm : monthData[i]) {
                    sum += ppm;
                }
                averages[i] = sum / monthData[i].size();
            } else {
                averages[i] = 0.0;
            }
        }
        return averages;
    }

        //double lat, double long, int year, String ppm, getPurityreport api
}