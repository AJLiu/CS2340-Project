package site.gitinitdone.h2go;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Date;

import java.util.List;

import site.gitinitdone.h2go.model.HistoricalReportCalc;
import site.gitinitdone.h2go.model.PurityReport;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.*;

/**
 * Created by shreya magesh on 4/9/17.
 */
public class HistoricalReportFilterTest {
    List<PurityReport> testList;
    List<PurityReport> actualFiltered;
    PurityReport report1;
    PurityReport report2;
    PurityReport report3;
    PurityReport report4;
    HistoricalReportCalc historicalReportCalc1;


    @Before
    public void setUp(){
        report1 = new PurityReport(0, 0, 1, "Test1", new Date().getTime(), PurityReport.OverallCondition.UNSAFE, 150, 300);
        report2 = new PurityReport(20, 20, 1, "Test2", new Date().getTime(), PurityReport.OverallCondition.UNSAFE, 150, 300);
        //report1 and report2 have the same year, but different locations
        report3 = new PurityReport(0, 0, 1, "Test3", new Date().getTime() - 50000000000L, PurityReport.OverallCondition.UNSAFE, 150, 300);
        //report 1 & 3 have the same location but different year (hopefully)
        report4 = new PurityReport(0, 0, 1, "Test1", new Date().getTime()- 3000, PurityReport.OverallCondition.UNSAFE, 150, 300);
        //report1 &4 should have the same location, and same year; should be in the filtered list together
        testList = new ArrayList<>();
        testList.add(report1);
        testList.add(report2);
        testList.add(report3);
        testList.add(report4); //adding the reports to the list of Purity Reports
        historicalReportCalc1 = new HistoricalReportCalc(0.0, 0.0, 2017, "Virus");
    }
    @Test
    public void filterTest() {
        actualFiltered = historicalReportCalc1.filter(testList);
        //check if the size of the filtered list is correct size
        assertEquals("Number of reports in filtered list is incorrect",2 ,actualFiltered.size());

        //ensure that the other reports aren't in the filtered list
        boolean inFiltered = false;
        for (PurityReport report: actualFiltered){
            if (report.equals(report2)) {
                inFiltered = true;
            }
        }
        assertTrue("Check how you filter the longitude and latitude of reports", !inFiltered);
        boolean inFiltered2 = false;
        for (PurityReport report: actualFiltered) {
            if (report.equals(report3)) {
                inFiltered2 = true;
            }
        }
        assertTrue("Check how you filter the year", !inFiltered2);

        //make sure that the correct reports are in the filtered list
        List<PurityReport> correctList = new ArrayList<>();
        correctList.add(report1);
        correctList.add(report4);
        assertEquals("The reports in filtered list are incorrect", correctList, actualFiltered);
    }

}