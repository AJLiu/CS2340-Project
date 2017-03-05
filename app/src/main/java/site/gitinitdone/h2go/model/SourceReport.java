package site.gitinitdone.h2go.model;

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
        WASTE("Waste"), TREATABLE_CLEAR("Treatable-Clear"), TREATABLE_MUDDY("Treatable-Muddy"),
        POTABLE("Potable");

        String abbrev;

        WaterCondition(String str) {
            abbrev = str;
        }

        public String toString() {
            return abbrev;

        }
    }

    private double latitude;
    private double longitude;
    private int reportNumber;
    private UserAccount reporter;
    private long timeStamp;
    private WaterType waterType;
    private WaterCondition waterCondition;

    public SourceReport(double lat, double lon, int reportNum, UserAccount submitter,
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

    public UserAccount getReporter() {
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

}
