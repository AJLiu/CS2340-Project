package site.gitinitdone.h2go.model;

import java.util.Date;

/**
 * This class will be used to store the Source Reports when getting them back from the database
 * and will store all the data so that the map can find the report info based on their location.
 */
public class SourceReport {

    /**
     * An enum for the different water types that are part of the Water Source Report
     * This is used to ensure a valid Water Type is entered by the user
     */
    public enum WaterType {
        BOTTLED, WELL, STREAM, LAKE, SPRING, OTHER;

        public String toString() {
            // Capital first letter, followed by all lower case letters
            return this.name().charAt(0) + this.name().substring(1).toLowerCase();
        }
    }

    /**
     * An enum for the different water conditions that are part of the Water Source Report
     * This is used to ensure a valid Water Condition is entered by the user
     */
    public enum WaterCondition {
        WASTE("Waste"), TREATABLECLEAR("Treatable Clear"), TREATABLEMUDDY("Treatable Muddy"),
        POTABLE("Potable");

        String abbrev;

        WaterCondition(String str) {
            abbrev = str;
        }

        public String toString() {
            return abbrev;

        }
    }

    private final double latitude;
    private final double longitude;
    private final int reportNumber;
    private final String reporter;
    private final long timeStamp;
    private final WaterType waterType;
    private final WaterCondition waterCondition;

    public SourceReport(double lat, double lon, int reportNum, String submitter,
                        long time, WaterType type, WaterCondition condition) {
        latitude = lat;
        longitude = lon;
        reportNumber = reportNum;
        reporter = submitter;
        timeStamp = time;
        waterType = type;
        waterCondition = condition;
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

    public WaterType getWaterType() {
        return waterType;
    }

    public WaterCondition getWaterCondition() {
        return waterCondition;
    }

    public String[] getReportStringFormatted() {

        int reportNum = this.getReportNumber();
        String date = (new Date(this.getTimeStamp())).toString();
        String submitter = this.getReporter();

        // Handles if the direction of latitude is North or South based on negative sign
        String latitude = "";
        if (this.getLatitude() < 0) {
            latitude = (this.getLatitude() * -1) + " South";
        } else {
            latitude = this.getLatitude() + " North";
        }

        // Handles if the direction of longitude is East or West based on negative sign
        String longitude = "";
        if (this.getLongitude() < 0) {
            longitude = (this.getLongitude() * -1) + " West";
        } else {
            longitude = this.getLongitude() + " East";
        }

        WaterType waterType = this.getWaterType();
        WaterCondition waterCondition = this.getWaterCondition();

        // Aggregates all the relevant fields into a nicely formatted string to show on screen
        String reportTitle = "Report #" + reportNum;
        String submitDate = "Submitted On: " + date + "\n";
        String reporter = "Submitted By: " + submitter + "\n";
        String location = "Location:\n\tLatitude: " + latitude + "\n\tLongitude: "
                + longitude + "\n";
        String waterTypeString = "Water Type: " + waterType + "\n";
        String waterConditionString = "Water Condition: " + waterCondition;

        return new String[]{reportTitle, submitDate + reporter + location
                                    + waterTypeString + waterConditionString + "\n"};
    }

}
