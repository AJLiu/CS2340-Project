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


    @Before
    public void setup() {

        test1 = new PurityReport(82.0, 90.0, 1, "Avanti", new Date().getTime(), PurityReport.OverallCondition.UNSAFE, 100, 25); 
        test2 = new PurityReport(82.0, 90.0, 2, "Vinny", new Date().getTime(), PurityReport.OverallCondition.TREATABLE, 300, 75);
        test3 = new PurityReport(82.0, 90.0, 2, "Shreya", new Date(2017, 1, 16), PurityReport.OverallCondition.TREATABLE, 100, 200);
        testList = new ArrayList<>(); 
        testList[0] = test1;
        testList[1] = test2; 
        testList[2] = test3; 
        historicalReportCalc1 = new HistoricalReportCalc(82.0, 90.0, 2017, "Virus");
        historicalReportCalc2 = new HistoricalReportCalc(82.0, 90.0, 2017, "Contaminant")


    }


    @Test
    public void checkAvgTest() {
        double[] result1 = historicalReportCalc1.getAverages(testList); 
        double[] result2 = historicalReportCalc2.getAverages(testList); 
        assertEquals(12, result1.length); 
        assertEquals(12, result2.length); 
        assertEquals(result1[3], 200);
        assertEquals(result2[3], 50);
        assertEquals(result1[0], 100);
        assertEquals(result2[0], 200);
        assertEquals(result1[4], 0);
        assertEquals(result2[4], 0); 
    }

}