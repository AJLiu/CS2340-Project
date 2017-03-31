package site.gitinitdone.h2go.controller;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.HistoricalReportCalc;

import static site.gitinitdone.h2go.model.HistoricalReportCalc.getAverageData;

public class HistoryGraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RadioButton virusType = (RadioButton)findViewById(R.id.historyGraphViewVirus);
        virusType.setSelected(true);

        // set default of lat and long to 0.0000
    }

    public static void finishedDataCalc() {
        double[] averageData = HistoricalReportCalc.getAverageData();
        //show graph using the data
    }

}