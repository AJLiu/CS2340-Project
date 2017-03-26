package site.gitinitdone.h2go.model;

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

}
