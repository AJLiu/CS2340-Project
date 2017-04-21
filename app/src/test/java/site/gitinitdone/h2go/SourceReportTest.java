package site.gitinitdone.h2go;

import org.junit.Before;
import org.junit.Test;
import site.gitinitdone.h2go.model.SourceReport;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Anthony on 4/5/2017.
 */
public class SourceReportTest {
    SourceReport report1;
    SourceReport report2;
    long time1;
    long time2;

    @Before
    public void setUp() throws Exception {
        time1 = new Date().getTime();
        report1 = new SourceReport(-1, -1, 0, "person", time1, SourceReport.WaterType.OTHER,
            SourceReport.WaterCondition.TREATABLECLEAR);


        time2 = new Date().getTime();
        report2 = new SourceReport(1, 1, 1, "otherPerson", time2, SourceReport.WaterType.SPRING,
            SourceReport.WaterCondition.TREATABLECLEAR);
    }

    @Test
    public void getReportStringFormatted() throws Exception {
        getReportStringNorthEast();
        getReportStringSouthWest();
    }

    @Test
    public void getReportStringSouthWest() throws Exception {
        String[] strings1 = report1.getReportStringFormatted();
        assertEquals(strings1[0], "Report #0");
        String[] decomposed1 = strings1[1].split("\n");
        assertEquals(decomposed1.length, 7);

        assertEquals("Submitted On: ", decomposed1[0].substring(0, 14));
        assertEquals(new Date(time1).toString(), decomposed1[0].substring(14));
        assertEquals("Submitted By: ", decomposed1[1].substring(0, 14));
        assertEquals("person", decomposed1[1].substring(14));
        assertEquals("Location:", decomposed1[2]);
        assertEquals("\tLatitude: ", decomposed1[3].substring(0, 11));
        assertEquals("1.0 South", decomposed1[3].substring(11));
        assertEquals("\tLongitude: ", decomposed1[4].substring(0, 12));
        assertEquals("1.0 West", decomposed1[4].substring(12));
        assertEquals("Water Type: ", decomposed1[5].substring(0, 12));
        assertEquals("Other", decomposed1[5].substring(12));
        assertEquals("Water Condition: ", decomposed1[6].substring(0, 17));
        assertEquals("Treatable Clear", decomposed1[6].substring(17));
    }

    @Test
    public void getReportStringNorthEast() throws Exception {
        String[] strings2 = report2.getReportStringFormatted();
        assertEquals(strings2[0], "Report #1");
        String[] decomposed2 = strings2[1].split("\n");
        assertEquals(decomposed2.length, 7);

        assertEquals("Submitted On: ", decomposed2[0].substring(0, 14));
        assertEquals(new Date(time2).toString(), decomposed2[0].substring(14));
        assertEquals("Submitted By: ", decomposed2[1].substring(0, 14));
        assertEquals("otherPerson", decomposed2[1].substring(14));
        assertEquals("Location:", decomposed2[2]);
        assertEquals("\tLatitude: ", decomposed2[3].substring(0, 11));
        assertEquals("1.0 North", decomposed2[3].substring(11));
        assertEquals("\tLongitude: ", decomposed2[4].substring(0, 12));
        assertEquals("1.0 East", decomposed2[4].substring(12));
        assertEquals("Water Type: ", decomposed2[5].substring(0, 12));
        assertEquals("Spring", decomposed2[5].substring(12));
        assertEquals("Water Condition: ", decomposed2[6].substring(0, 17));
        assertEquals("Treatable Clear", decomposed2[6].substring(17));
    }

}