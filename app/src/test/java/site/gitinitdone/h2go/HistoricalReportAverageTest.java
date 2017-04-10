package site.gitinitdone.h2go;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import site.gitinitdone.h2go.model.HistoricalReportCalc;
import site.gitinitdone.h2go.model.PurityReport;
import site.gitinitdone.h2go.model.PurityReport.OverallCondition;

/**
 * Created by Avanti Joshi on 4/8/2017
 * JUnit tests for the getAverages() method in the HistoricalReportCalc class
 */
public class HistoricalReportAverageTest {

    PurityReport test1;
    PurityReport test2;
    PurityReport test3;
    List<PurityReport> testList;
    HistoricalReportCalc historicalReportCalc1;
    HistoricalReportCalc historicalReportCalc2;
    HistoricalReportCalc historicalReportCalc3;
    private final long offsetTime = Long.parseLong("40150624214"); // creates an offset by 1 year

    @Before
    public void setup() {

        test1 = new PurityReport(82.0, 90.0, 1, "Avanti", new Date().getTime(), PurityReport.OverallCondition.UNSAFE, 100, 25); 
        test2 = new PurityReport(82.0, 90.0, 2, "Vinny", new Date().getTime(), PurityReport.OverallCondition.TREATABLE, 300, 75);
        test3 = new PurityReport(82.0, 90.0, 2, "Shreya", new Date().getTime() - offsetTime, PurityReport.OverallCondition.TREATABLE, 20, 35);
        testList = new ArrayList<>(); 
        testList.add(test1);
        testList.add(test2);
        testList.add(test3);
        historicalReportCalc1 = new HistoricalReportCalc(82.0, 90.0, 2017, "Virus");
        historicalReportCalc2 = new HistoricalReportCalc(82.0, 90.0, 2017, "Contaminant");
        historicalReportCalc3 = new HistoricalReportCalc(82.0, 90.0, 2016, "Virus");


    }


    @Test
    public void checkAvgTest() {
        double[] result1 = historicalReportCalc1.getAverages(testList); 
        double[] result2 = historicalReportCalc2.getAverages(testList);
        double[] result3 = historicalReportCalc3.getAverages(testList);
        assertEquals(12, result1.length); 
        assertEquals(12, result2.length);
        assertEquals(12, result3.length);

        //checking to see if Jan entry  in 2016 is 20
        assertEquals(result3[0], 20, 0);

        // checking to see if Virus average is correct for 2017
        assertEquals(result1[3], 200, 0);

        //check to see if contaminent average is correct for 2017
        assertEquals(result2[3], 50, 0);

        // making sure all of the other entries are 0 for 2017
        assertEquals(result1[4], 0, 0);
        assertEquals(result2[4], 0, 0);
        assertEquals(result1[5], 0, 0);
        assertEquals(result2[5], 0, 0);
        assertEquals(result1[6], 0, 0);
        assertEquals(result2[6], 0, 0);
        assertEquals(result1[7], 0, 0);
        assertEquals(result2[7], 0, 0);
        assertEquals(result1[8], 0, 0);
        assertEquals(result2[8], 0, 0);
        assertEquals(result1[9], 0, 0);
        assertEquals(result2[9], 0, 0);
        assertEquals(result1[10], 0, 0);
        assertEquals(result2[10], 0, 0);
        assertEquals(result1[11], 0, 0);
        assertEquals(result2[11], 0, 0);
    }

}