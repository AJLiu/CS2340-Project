package site.gitinitdone.h2go;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Random;

import site.gitinitdone.h2go.model.PurityReport;
import site.gitinitdone.h2go.model.PurityReport.OverallCondition;

/**
 * Created by Suraj Masand on 4/5/2017
 * JUnit tests for the Purity Report Class
 * Specifically, this will test the getReportStringFormatter method
 */
public class PurityReportTest {

    private PurityReport[] reports = new PurityReport[3];
    private OverallCondition[] conditions = OverallCondition.values();
    private int[] reportNums = {1, 2, 3};
    private String[] reporterNames = {"JUnit Tester 1", "JUnit 2nd Test", "3rd Test in JUnit"};
    private long[] reportTimes = {new Date().getTime(), new Date().getTime() + 12345,
                                    new Date().getTime() - 12345};

    private Random randomLatLong = new Random();
    private double[] latitudes = {  randomLatLong.nextInt(9000) / 100.0, // north
                                    ((randomLatLong.nextInt(8999) / 100.0) + 1) * -1, // south
                                    randomLatLong.nextInt(9000) / 100.0}; //north

    private double[] longitudes = { randomLatLong.nextInt(18000) / 100.0, // east
                                    randomLatLong.nextInt(18000) / 100.0, //east
                                    ((randomLatLong.nextInt(17999) / 100.0) + 1) * -1}; //west
    private int[] virusPPMs = {12345, 6, 927137};
    private int[] contaminantPPMs = {67890, 198, 10842};



    @Before
    public void setup() {

        reports[0] = new PurityReport(latitudes[0], longitudes[0], reportNums[0], reporterNames[0],
                                reportTimes[0], conditions[0], virusPPMs[0], contaminantPPMs[0]);

        reports[1] = new PurityReport(latitudes[1], longitudes[1], reportNums[1], reporterNames[1],
                                reportTimes[1], conditions[1], virusPPMs[1], contaminantPPMs[1]);

        reports[2] = new PurityReport(latitudes[2], longitudes[2], reportNums[2], reporterNames[2],
                                reportTimes[2], conditions[2], virusPPMs[2], contaminantPPMs[2]);

    }


    @Test
    public void getSafeNorthEastTest() {

        String[] actualReportString1 = reports[0].getReportStringFormatted();
        assertEquals(2, actualReportString1.length);
        assertEquals("Purity Report #" + reportNums[0], actualReportString1[0]);

        String[] messageParts = actualReportString1[1].split("\n");
        assertEquals(9, messageParts.length);

        assertEquals("Submitted On: ", messageParts[0].substring(0,14));
        assertEquals((new Date(reportTimes[0])).toString(), messageParts[0].substring(14));

        assertEquals("Submitted By: ", messageParts[1].substring(0,14));
        assertEquals(reporterNames[0], messageParts[1].substring(14));

        assertEquals("Location: ", messageParts[2].substring(0,10));

        // trim to remove white spaces (tabs) for the latitude and longitude
        // tabs were used for indentation when shown in the app
        assertEquals("Latitude: ", messageParts[3].trim().substring(0,10));
        assertEquals(latitudes[0] + " North", messageParts[3].trim().substring(10));

        assertEquals("Longitude: ", messageParts[4].trim().substring(0,11));
        assertEquals(longitudes[0] + " East", messageParts[4].trim().substring(11));


        assertEquals("Water Condition: ", messageParts[5].substring(0,17));
        assertEquals(conditions[0].toString(), messageParts[5].substring(17));

        assertEquals("Virus PPM: ", messageParts[6].substring(0,11));
        assertEquals(virusPPMs[0], Integer.parseInt(messageParts[6].substring(11)));

        assertEquals("Contaminant PPM: ", messageParts[7].substring(0,17));
        assertEquals(contaminantPPMs[0], Integer.parseInt(messageParts[7].substring(17)));

        assertEquals(" ", messageParts[8]); //extra space at end of message

    }

    @Test
    public void getTreatableSouthEastTest() {

        String[] actualReportString1 = reports[1].getReportStringFormatted();
        assertEquals(2, actualReportString1.length);
        assertEquals("Purity Report #" + reportNums[1], actualReportString1[0]);

        String[] messageParts = actualReportString1[1].split("\n");
        assertEquals(9, messageParts.length);

        assertEquals("Submitted On: ", messageParts[0].substring(0,14));
        assertEquals((new Date(reportTimes[1])).toString(), messageParts[0].substring(14));

        assertEquals("Submitted By: ", messageParts[1].substring(0,14));
        assertEquals(reporterNames[1], messageParts[1].substring(14));

        assertEquals("Location: ", messageParts[2].substring(0,10));

        // trim to remove white spaces (tabs) for the latitude and longitude
        // tabs were used for indentation when shown in the app
        assertEquals("Latitude: ", messageParts[3].trim().substring(0,10));
        assertEquals(Math.abs(latitudes[1]) + " South", messageParts[3].trim().substring(10));

        assertEquals("Longitude: ", messageParts[4].trim().substring(0,11));
        assertEquals(longitudes[1] + " East", messageParts[4].trim().substring(11));


        assertEquals("Water Condition: ", messageParts[5].substring(0,17));
        assertEquals(conditions[1].toString(), messageParts[5].substring(17));

        assertEquals("Virus PPM: ", messageParts[6].substring(0,11));
        assertEquals(virusPPMs[1], Integer.parseInt(messageParts[6].substring(11)));

        assertEquals("Contaminant PPM: ", messageParts[7].substring(0,17));
        assertEquals(contaminantPPMs[1], Integer.parseInt(messageParts[7].substring(17)));

        assertEquals(" ", messageParts[8]); //extra space at end of message

    }

    @Test
    public void getUnsafeNorthWestTest() {

        String[] actualReportString1 = reports[2].getReportStringFormatted();
        assertEquals(2, actualReportString1.length);
        assertEquals("Purity Report #" + reportNums[2], actualReportString1[0]);

        String[] messageParts = actualReportString1[1].split("\n");
        assertEquals(9, messageParts.length);

        assertEquals("Submitted On: ", messageParts[0].substring(0,14));
        assertEquals((new Date(reportTimes[2])).toString(), messageParts[0].substring(14));

        assertEquals("Submitted By: ", messageParts[1].substring(0,14));
        assertEquals(reporterNames[2], messageParts[1].substring(14));

        assertEquals("Location: ", messageParts[2].substring(0,10));

        // trim to remove white spaces (tabs) for the latitude and longitude
        // tabs were used for indentation when shown in the app
        assertEquals("Latitude: ", messageParts[3].trim().substring(0,10));
        assertEquals(latitudes[2] + " North", messageParts[3].trim().substring(10));

        assertEquals("Longitude: ", messageParts[4].trim().substring(0,11));
        assertEquals(Math.abs(longitudes[2]) + " West", messageParts[4].trim().substring(11));


        assertEquals("Water Condition: ", messageParts[5].substring(0,17));
        assertEquals(conditions[2].toString(), messageParts[5].substring(17));

        assertEquals("Virus PPM: ", messageParts[6].substring(0,11));
        assertEquals(virusPPMs[2], Integer.parseInt(messageParts[6].substring(11)));

        assertEquals("Contaminant PPM: ", messageParts[7].substring(0,17));
        assertEquals(contaminantPPMs[2], Integer.parseInt(messageParts[7].substring(17)));

        assertEquals(" ", messageParts[8]); //extra space at end of message

    }

}
