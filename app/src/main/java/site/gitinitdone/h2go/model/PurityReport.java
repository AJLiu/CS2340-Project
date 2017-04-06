package site.gitinitdone.h2go.model;

import java.util.Date;

/**
 * This class will be used to store the Purity Reports when getting them back from the database
 * and will store all the data so that the history graph can find the report info based on their
 * location and the virus / contaminant ppm.
 */
public class PurityReport {

    /**
     * An enum for the different water conditions that are part of the Water Purity Report
     * This is used to ensure a valid Overall Condition is entered by the user
     */
    public enum OverallCondition {
        SAFE, TREATABLE, UNSAFE;

        public String toString() {
            // Capital first letter, followed by all lower case letters
            return this.name().charAt(0) + this.name().substring(1).toLowerCase();
        }
    }

    private double latitude;
    private double longitude;
    private int reportNumber;
    private String reporter;
    private long timeStamp;
    private OverallCondition waterCondition;
    private int virusPPM;
    private int contaminantPPM;

    public PurityReport(double lat, double lon, int reportNum, String submitter,
                        long time, OverallCondition condition, int virus, int contaminant) {
        latitude = lat;
        longitude = lon;
        reportNumber = reportNum;
        reporter = submitter;
        timeStamp = time;
        waterCondition = condition;
        virusPPM = virus;
        contaminantPPM = contaminant;
    }

    // ************************************************************
    // * The following methods are the getter methods             *
    // * for each of these properties of the user account         *
    // * There is no need for setters since we store everything   *
    // * in the online database                                   *
    // ************************************************************

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public String getReporter() {
        return reporter;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public OverallCondition getWaterCondition() {
        return waterCondition;
    }

    public int getVirusPPM() {
        return virusPPM;
    }

    public int getContaminantPPM() {
        return contaminantPPM;
    }

    public String[] getReportStringFormatted() {

        String date = (new Date(timeStamp)).toString();

        // Handles if the direction of latitude is North or South based on negative sign
        String latitudeString = "";
        if (latitude < 0) {
            latitudeString = (latitude * -1) + " South";
        } else {
            latitudeString = latitude + " North";
        }

        // Handles if the direction of longitude is East or West based on negative sign
        String longitudeString = "";
        if (longitude < 0) {
            longitudeString = (longitude * -1) + " West";
        } else {
            longitudeString = longitude + " East";
        }

        // Aggregates all the relevant fields into a nicely formatted string to show on screen
        String reportNum = "Purity Report #" + reportNumber;
        String submitDate = "Submitted On: " + date + "\n";
        String submitUser = "Submitted By: " + reporter + "\n";
        String locationString = "Location: \n \t Latitude: " + latitudeString + " \n \t Longitude: "
                + longitudeString + "\n";
        String conditionString = "Water Condition: " + waterCondition + "\n";
        String virusString = "Virus PPM: " + virusPPM + "\n";
        String contaminantString = "Contaminant PPM: " + contaminantPPM + "\n \n";

        return new String[] {reportNum, submitDate + submitUser + locationString + conditionString
                                + virusString + contaminantString};
    }

}
