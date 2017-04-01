package site.gitinitdone.h2go.controller;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import site.gitinitdone.h2go.R;
import site.gitinitdone.h2go.model.HistoricalReportCalc;


public class PlottedGraphActivity extends AppCompatActivity {
    private double[] data;
    private String ppmType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plotted_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        data = getIntent().getDoubleArrayExtra("data");
        ppmType = getIntent().getStringExtra("ppm");

        DataPoint[] dataPoints = new DataPoint[12];

        for (int i = 0; i < 12; i++) {
            dataPoints[i] = new DataPoint((i + 1), data[i]);
        }

        GraphView graph = (GraphView) findViewById(R.id.graph);
        initGraph(graph, dataPoints);
    }

    public void initGraph(GraphView graph, DataPoint[] dataPoints) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
        graph.addSeries(series);

        // set manual x bounds to have nice steps
//        graph.getViewport().setMinX(1);
//        graph.getViewport().setMaxX(12);
//        graph.getViewport().setXAxisBoundsManual(true);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Month");
        gridLabel.setVerticalAxisTitle(ppmType + " PPM");

        // use static labels for horizontal and vertical labels
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        gridLabel.setLabelFormatter(staticLabelsFormatter);


//        System.out.println("Data is @@@@@@@@@@");
//        for (double d:data) {
//            System.out.println(d);
//        }
    }
}
