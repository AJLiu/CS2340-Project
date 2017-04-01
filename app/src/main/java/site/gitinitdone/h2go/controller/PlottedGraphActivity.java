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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plotted_graph);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GraphView graph = (GraphView) findViewById(R.id.graph);
        initGraph(graph);
    }

    public void initGraph(GraphView graph) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 7),
                new DataPoint(2, 5),
                new DataPoint(3, 3),
                new DataPoint(4, 4),
                new DataPoint(5, 8),
                new DataPoint(6, 12),
                new DataPoint(7, 13),
                new DataPoint(8, 7),
                new DataPoint(9, 3),
                new DataPoint(10, 7),
                new DataPoint(11, 7),
                new DataPoint(12, 9)
        });
        graph.addSeries(series);

        // set manual x bounds to have nice steps
//        graph.getViewport().setMinX(1);
//        graph.getViewport().setMaxX(12);
//        graph.getViewport().setXAxisBoundsManual(true);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Month");
        gridLabel.setVerticalAxisTitle("PPM");

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
